package sparkstreaming

import org.apache.spark.streaming.dstream._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 简单
 */
object BaseStreaming extends App{
  //必须至少有2个线程，一个负责接收，一个负责处理
  val sc = new SparkContext(new SparkConf().setAppName("SparkWC"))
  sc.setLogLevel("ERROR")
  //5秒钟一个批次
  val ssc = new StreamingContext(sc, Seconds(5))
  val stream = ssc.socketTextStream("localhost", 8899)

  val tuples:DStream[(String,Int)] = stream.flatMap(_.split(" ")).map((_,1))

  //默认只显示10条
  tuples.print(50)
  //开启流处理
  ssc.start()
  ssc.awaitTermination()
}
