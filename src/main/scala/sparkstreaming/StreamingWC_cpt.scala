package sparkstreaming

import org.apache.spark.streaming.dstream._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

/**
 * 支持检查点容错的按批次准实时累加
 * 使用updateStateByKey可以实现统计每个单词全局次数
 */
object StreamingWC_cpt extends App{
  val cptDirectory = "file/stream.StreamingWC_cpt.ckp"

  //使用新检查点
  def createContextUsingNewCpt()={
    val sc = new SparkContext(new SparkConf().setAppName("StreamingWC_cpt"))
    sc.setLogLevel("ERROR")
    val ssc = new StreamingContext(sc, Seconds(5))
    ssc
  }
  //若无指定检查点，则使用新检查点
  val ssc = StreamingContext.getOrCreate(cptDirectory, createContextUsingNewCpt _)
  val stream = ssc.socketTextStream("localhost", 8899)

  val tuples:DStream[(String,Int)] = stream.flatMap(_.split(" ")).map((_,1))
  //参数是元组迭代器，第一个是单词，第二个是当前批次此单词出现的次数（有几个数就是几次），第三个是历史次数
  val func = (it: Iterator[(String, Seq[Int], Option[Int])]) => {
    it.map(t => (t._1, t._2.sum + t._3.getOrElse(0)))
  }
  val res: DStream[(String, Int)] = tuples.updateStateByKey(func, new HashPartitioner(ssc.sparkContext.defaultParallelism), false)

  //默认只显示10条
  res.print(50)
  //开启流处理
  ssc.start()
  ssc.awaitTermination()
}
