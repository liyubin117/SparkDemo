package sparkstreaming

import org.apache.spark.streaming.dstream._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

/**
 * 按批次准实时累加
 * 使用updateStateByKey可以实现统计每个单词全局次数
 */
object StreamingWC extends App{
  //必须至少有2个线程，一个负责接收，一个负责处理
  //检查点文件默认存在hdfs的/user/<osname>/
  //设置spark.streaming.stopGracefullyOnShutdown可以优雅地关掉sparkstreaming
  val sc = new SparkContext(new SparkConf().setAppName("StreamingWC").setMaster("local[2]")
//    .set("spark.streaming.stopGracefullyOnShutdown","true")
  )
  sc.setLogLevel("ERROR")
  //5秒钟一个批次
  val ssc = new StreamingContext(sc, Seconds(5))
  ssc.checkpoint("file/stream.SparkWC.ckp")
  val stream = ssc.socketTextStream("localhost", 8899)

  val tuples:DStream[(String,Int)] = stream.flatMap(_.split(" ")).map((_,1))
  //初始RDD
  val initialRDD = ssc.sparkContext.parallelize(List(("key", 1), ("value", 1)))

  //第一种
  // updateStateByKey[S: ClassTag]( updateFunc: (Iterator[(K, Seq[V], Option[S])]) => Iterator[(K, S)], partitioner: Partitioner, rememberPartitioner: Boolean ): DStream[(K, S)]
  //参数是元组迭代器，第一个是单词，第二个是当前批次此单词出现的序列（有几个数就是几次），第三个是历史次数
  val func = (it: Iterator[(String, Seq[Int], Option[Int])]) => {
    it.map(t => (t._1, t._2.sum + t._3.getOrElse(0)))
  }
  val res: DStream[(String, Int)] = tuples.updateStateByKey(func, new HashPartitioner(ssc.sparkContext.defaultParallelism), false, initialRDD)

  //第二种
  //updateStateByKey[S: ClassTag]( updateFunc: (Seq[V], Option[S]) => Option[S], partitioner: Partitioner ): DStream[(K, S)]
  val add = (currValues:Seq[Int], prevValueState:Option[Int]) => {
    // 通过Spark内部的reduceByKey按key规约。然后这里传入某key当前批次的Seq/List,再计算当前批次的总和
    val currCount = currValues.sum
    // 历史累加的值
    val prevCount = prevValueState.getOrElse(0)
    // 返回累加后的结果。是一个Option[Int]类型
    Some(currCount + prevCount)
  }
  val res2 = tuples.updateStateByKey(add, new HashPartitioner(ssc.sparkContext.defaultParallelism), initialRDD)


  //默认只显示10条
  res2.print(50)
  //开启流处理
  ssc.start()
  ssc.awaitTermination()
}
