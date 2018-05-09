package spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

/**
 * Created by Administrator on 2018/5/2 0002.
 */
object Partiton extends App{
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  val sc = new SparkContext(new SparkConf().setAppName("BroadAcc").setMaster("local[1]").set("spark.default.parallelism","2"))
  sc.setLogLevel("ERROR")

  println("默认分区数"+sc.defaultParallelism)
  val part = sc.parallelize(List((1,"a"),(2,"b"),(3,"c"),(4,"d"),(5,"e"),(6,"f")),3)
  //分区数优先级：spark.setMaster、spark.default.parallelism、sc.parallelize
  println("分区数："+part.partitions.length+" 分区方法："+part.partitioner)
  //partitionBy重分区（不光分区数改变，分区方法也改变）
  val part2 = part.partitionBy(new HashPartitioner(4))
  println("partitionBy重分区后的分区数："+part2.partitions.length+" 分区方法："+part2.partitioner.toString)
  //coalesce重分区（分区方法None）
  val part3 = part.coalesce(3,false)
  val part4 = part.coalesce(5,false)  //当重分区后的分区数比原来小时，shuffle必须设为true，否则重分区无效
  val part5 = part.coalesce(5,true)
  println("coalesce重分区后的分区数："+part3.partitions.length+" 分区方法："+part3.partitioner.toString)
  println("coalesce重分区后的分区数："+part4.partitions.length+" 分区方法："+part4.partitioner.toString)
  println("coalesce重分区后的分区数："+part5.partitions.length+" 分区方法："+part5.partitioner.toString)

  println("每个分区有哪些元素："+
    part2.mapPartitionsWithIndex{
    (partIdx,iter) => {
      val part_map = scala.collection.mutable.Map[String,List[(Int,String)]]()
      while(iter.hasNext){
        val part_name = "part_" + partIdx
        var elem = iter.next()
        if(!part_map.contains(part_name)) {
          part_map(part_name) = List[(Int,String)]{elem}
        } else {
          part_map(part_name) ::= elem
        }
      }
      part_map.iterator
    }
   }.collect.mkString(",")
  )
  println("每个分区的元素数量："+
    part2.mapPartitionsWithIndex{
      (partIdx,iter) => {
        val part_map = scala.collection.mutable.Map[String,Int]()
        while(iter.hasNext){
          val part_name = "part_" + partIdx
          var elem = iter.next()
          if(!part_map.contains(part_name)) {
            part_map(part_name) = 1
          } else {
            part_map(part_name) += 1
          }
        }
        part_map.iterator
      }
    }.collect.mkString(",")
  )

}
