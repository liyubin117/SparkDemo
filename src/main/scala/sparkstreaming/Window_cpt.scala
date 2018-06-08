package sparkstreaming

import org.apache.spark.streaming.dstream._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

/**
 * 窗口聚合
 */
object Window_cpt extends App{
  val cptDirectory = "file/stream.window.ckp"

  //使用新检查点
  def createContextUsingNewCpt()={
    val sc = new SparkContext(new SparkConf().setAppName("Window_cpt"))
    sc.setLogLevel("ERROR")
    val ssc = new StreamingContext(sc, Seconds(5))
    ssc
  }
  //若无指定检查点，则使用新检查点
  val ssc = StreamingContext.getOrCreate(cptDirectory, createContextUsingNewCpt _)
  val stream = ssc.socketTextStream("localhost", 8899)

  val tuples:DStream[(String,Int)] = stream.flatMap(_.split(" ")).map((_,1))

  //批次间隔5秒，窗口长度15秒，滑动长度10秒
  val func = (a:Int,b:Int) => a+b
  val res:DStream[(String,Int)] = tuples.reduceByKeyAndWindow(func,Seconds(15),Seconds(10))

  //默认只显示10条
  res.print(50)
  //开启流处理
  ssc.start()
  ssc.awaitTermination()
}
