package sparkstreaming

import kafka.serializer.StringDecoder
import org.apache.log4j.{Level, Logger}
import org.apache.spark.streaming.dstream.InputDStream

import scala.reflect.runtime.universe
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext, Time}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.dstream.{DStream, InputDStream}

/**
 * 每个批次内的pv、ips、uv
 */
object DapLogStreaming {

  def main (args : Array[String]) {

    val cptDir="file/receivedStreaming"
    val zookeeper = "spark:2181"
    val group_id = "test_group"

    Logger.getLogger("org").setLevel(Level.ERROR)
    /**
     * 基于Receiver获取Kafka流，offset由zookeeper管理，可能导致重复消费
     */
    def createContextUsingNewCpt()={
      val sparkConf = new SparkConf().setMaster("local[2]").setAppName("DapLogStreaming")
      //每60秒一个批次
      val ssc = new StreamingContext(sparkConf, Duration(5000))

      val stream = KafkaUtils.createStream(
        ssc,
        zookeeper, //Kafka集群使用的zookeeper
        group_id, //该消费者使用的group.id
        Map[String, Int]("test" -> 3), //日志在Kafka中的topic及其分区，键是topic名，值是topic的分区数
        StorageLevel.MEMORY_AND_DISK_SER)
        .map(x => x._2.split("\\|~\\|", -1))  //日志以|~|为分隔符

      stream.foreachRDD((rdd: RDD[Array[String]], time: Time) => {
        val sqlContext = SQLContextSingleton.getInstance(rdd.sparkContext)
        //      val sqlContext = new SQLContext(rdd.sparkContext)
        import sqlContext.implicits._
        //构造case class: DapLog,提取日志中相应的字段
        val logDataFrame = rdd.filter(_.length==7).map(w => DapLog(w(0).substring(0, 10),w(2),w(6))).toDF()
        //注册为tempTable
        logDataFrame.registerTempTable("daplog")
        //查询该批次的pv,ip数,uv
        val logCountsDataFrame =
          sqlContext.sql("select date_format(current_timestamp(),'yyyy-MM-dd HH:mm:ss') as time,count(1) as pv,count(distinct ip) as ips,count(distinct cookieid) as uv from daplog")
        //打印查询结果
        logCountsDataFrame.show()
      })
      ssc.checkpoint(cptDir)
      ssc
    }

    val ssc = StreamingContext.getOrCreate(cptDir,createContextUsingNewCpt _)

    /**
     * 直接读取获取Kafka流，offset由sparkstreaming的检查点管理，可以实现exactly-once消费
     */
//    val stream: DStream[Array[String]] = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, Map("metadata.broker.list" -> "spark:9092"), Set("test"))
//                  .map(x => x._2.split("\\|~\\|", -1))  //日志以|~|为分隔符


    ssc.start()
    ssc.awaitTermination()
  }
}

case class DapLog(day:String, ip:String, cookieid:String)

object SQLContextSingleton {
  @transient private var instance: SQLContext = _
  def getInstance(sparkContext: SparkContext): SQLContext = {
    if (instance == null) {
      instance = new SQLContext(sparkContext)
    }
    instance
  }
}