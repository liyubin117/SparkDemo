package sparkstreaming

import java.sql.DriverManager

import kafka.serializer.StringDecoder
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Seconds, Duration, StreamingContext}
import org.apache.spark.streaming.kafka.{HasOffsetRanges, OffsetRange, KafkaUtils}

object KafkaSparkDemoMain {
  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.ERROR)

    val topics = Set("test") //我们需要消费的kafka数据的topic
    val kafkaParam = Map(
        "metadata.broker.list" -> "spark:9092", // kafka的broker list地址
        "group.id" -> "test_group",
        "auto.offset.reset" -> "latest"  //从最新的offset开始接收数据
//        "enable.auto.commit" -> (false: java.lang.Boolean),  //禁用自动更新offset，因为consumer接收到数据时，计算结果不一定输出即尚未完成幂等输出，此时若提交将无法保证原子性，也就无法保证exactly-once消费
      )
    val cptDirectory = "file/KafkaWC"

    //使用新检查点
    def createContextUsingNewCpt()={
      val sc = new SparkContext(new SparkConf().setAppName("kafka-spark-demo").setIfMissing("spark.master","local[2]"))
      val ssc = new StreamingContext(sc, Seconds(5))

      /**
       * 创建一个从kafka获取数据的流.
       * scc           spark streaming上下文
       * kafkaParam    kafka相关配置
       * topics        需要消费的topic集合
       */
      val stream: InputDStream[(String, String)] = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParam, topics)
//      val dstream: DStream[(String, Int)] = stream.map(_._2)      // 取出value
//        .flatMap(_.split(" ")) // 将字符串使用空格分隔
//        .map(r => (r, 1))      // 每个单词映射成一个pair
//        .updateStateByKey[Int](updateFunc)  // 用当前batch的数据区更新已有的数据
//      //默认情况下，checkpoint 时间间隔会取 steaming 程序数据处理间隔或者 10 秒两者中较大的那个。官方推荐的间隔是 streaming 程序数据处理间隔的 5-10 倍
//      dstream.checkpoint(Seconds(25))

      var offsetRanges = Array[OffsetRange]()  //记录offset
      val dstream = stream.transform{rdd=>
        offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges  //获取每个batch对应的offset
        rdd
      }.map(_._2)      // 取出value
        .flatMap(_.split(" ")) // 将字符串使用空格分隔
        .map(r => (r, 1))      // 每个单词映射成一个pair
        .updateStateByKey[Int](updateFunc)  // 用当前batch的数据区更新已有的数据
      dstream.checkpoint(Seconds(25))
      /**
       * 持久化到mysql
       * create database test default character set utf8;
       * create table test.wordcount(word varchar(100), cnt bigint(20),primary key(word)) character set utf8;
       */

      def createConnection()={
        Class.forName("com.mysql.jdbc.Driver")
        DriverManager.getConnection("jdbc:mysql://spark:3306/test","root","PASSWD123")
      }
      dstream.foreachRDD(rdd => {
        for(o <- offsetRanges){
          println(s"${o.topic} ${o.partition} ${o.fromOffset} ${o.untilOffset}")  //打印offset
        }
        //kafka 0.10、sparkstreaming2.x集成的spark-streaming-kafka-0-10 提供的新功能，输出结果后更新offset
//        dstream.asInstanceOf[CanCommitOffsets].commitAsync()
        rdd.foreachPartition(partitionOfRecord=>{
          val connection = createConnection() //获取mysql的链接
          partitionOfRecord.foreach{record =>
            val sql = "replace into wordcount values ('" + record._1 + "'," + record._2 + ");"
            connection.createStatement().execute(sql)
          }
          connection.close()
        })
      })

      dstream.print() // 打印前10个数据
      ssc.checkpoint(cptDirectory)
      ssc
    }
    //若无指定检查点，则使用新检查点  因为使用到了updateStateByKey,所以必须要设置checkpoint
    val ssc = StreamingContext.getOrCreate(cptDirectory, createContextUsingNewCpt _)
    ssc.start() // 真正启动程序
    ssc.awaitTermination() //阻塞等待
  }

  val updateFunc = (currentValues: Seq[Int], preValue: Option[Int]) => {
    val curr = currentValues.sum
    val pre = preValue.getOrElse(0)
    Some(curr + pre)
  }

}