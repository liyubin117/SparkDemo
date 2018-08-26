package sparkstreaming

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.flume._

/**
 * Created by lixin on 2018/8/26.
 */
object FlumePushWC extends App{
  val ssc = new StreamingContext(new SparkContext(new SparkConf().setMaster("local[2]").setAppName("FlumePushWC")), Seconds(5))
  val flumeStream = FlumeUtils.createStream(ssc, "spark", 9888)
  val wordcount = flumeStream.map(x => new String(x.event.getBody.array()).trim).flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)
  wordcount.print()

  ssc.start()
  ssc.awaitTermination()
}
