package sparkstreaming

import org.apache.spark.streaming.flume._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by lixin on 2018/8/26.
 */
object FlumePullWC{
  def main(args:Array[String]): Unit ={
    val conf = new SparkConf().setMaster("local[2]").setAppName("FlumePushWC")
    val ssc = new StreamingContext(new SparkContext(conf), Seconds(5))
    val flumeStream = FlumeUtils.createPollingStream(ssc, "spark", 9888)
    val wordcount = flumeStream.map(x => new String(x.event.getBody.array()).trim).flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)
    wordcount.print()

    ssc.start()
    ssc.awaitTermination()
  }
}
