package spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

/**
 * Created by Administrator on 2018/4/28 0028.
 */
object RDDdemo extends App{
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  val sc = new SparkContext(new SparkConf().setAppName("BroadAcc").setMaster("local[3]").set("spark.default.parallelism","2"))
  sc.setLogLevel("ERROR")

  //join
  val rdd1 = sc.parallelize(List((1,"a"),(2,"b"),(1,"e"),(1,"d"),(5,"e"),(5,"f")))
  val rdd2 = sc.parallelize(List((1,"c"),(3,"d"),(3,"e"),(3,"f"),(5,"g"),(5,"h")))
  println(rdd1.join(rdd2).collect.mkString(","))
  println(rdd1.leftOuterJoin(rdd2).collect.mkString(","))
  println(rdd1.rightOuterJoin(rdd2).collect.mkString(","))
  //分拆join（可用于优化数据倾斜）
  //rdd1中key较多的1、5拆分出来
  val rdd11 = sc.parallelize(List((1,"a"),(1,"e"),(1,"d"),(5,"e"),(5,"f")))
  val rdd12 = sc.parallelize(List((2,"b")))
  //rdd2中key较多的3、5拆分出来
  val rdd21 = sc.parallelize(List((5,"g"),(5,"h"),(3,"d"),(3,"e"),(3,"f")))
  val rdd22 = sc.parallelize(List((1,"c")))
  //rdd2中拆分出rdd1 key较多的1、5
  val rdd23 = sc.parallelize(List((1,"c"),(5,"g"),(5,"h")))
  //完全join
  println(rdd11.join(rdd21).union(rdd12.join(rdd22)).collect.mkString(","))
  //拆分后与原结果不一样
  println(rdd11.join(rdd2).union(rdd12.join(rdd2)).collect.mkString(","))
  //拆分后与原结果一样，所以拆分时要先找出一个rdd中key较多的，再从另一个rdd中把这些key也拆出来
  println(rdd11.join(rdd23).union(rdd12.join(rdd23)).collect.mkString(","))

  //sample(采样时有无放回, 比例)
  val sam1 = rdd1.sample(true, 0.5f)
  println(sam1.collect.mkString(","))

  println("-----persist cache（缓存与持久化） lazy操作-----")
  val persist = sc.textFile("file/webrecord.txt").persist(StorageLevel.MEMORY_AND_DISK_SER_2)
  println(persist.count)
  //unpersist（释放缓存） eager操作
  persist.unpersist()

  println("----------聚合----------")
  val input = sc.parallelize(1 to 10,3)
  println("每个分区的元素："+input.mapPartitionsWithIndex{
    (partIdx,iter) => {
      val part_map = scala.collection.mutable.Map[String,List[Int]]()
      while(iter.hasNext){
        val part_name = "part_" + partIdx
        var elem = iter.next()
        if(!part_map.contains(part_name)) {
          part_map(part_name) = List[Int]{elem}
        } else {
          part_map(part_name) ::= elem
        }
      }
      part_map.iterator
    }
  }.collect.mkString(","))
  //aggregate eager操作
  val aggre = input.aggregate(10)((x,y)=>x+y, (a,b)=>a*b); println(aggre)
  //reduce eager操作
  val reduce = input.reduce(_+_); println(reduce)
  //fold eager操作
  val fold = input.fold(9)((x,y) => if(x>y) x else y); println(fold)
  val fold_aggre = input.aggregate(9)((x,y)=>if(x>y) x else y,(a,b)=>if(a>b) a else b)
  println(fold_aggre) //fold是aggregate seqOp、combOp相同的简化版本
  println("fold的过程：")
  val add = (x: Int, y: Int) => {
    println(x + "\t" + y)
    x + y
  }
  println(sc.parallelize(List(1, 2, 3, 4, 5, 6, 7, 8), 1).fold(10)(add)) //56
  println(sc.parallelize(List(1, 2, 3, 4, 5, 6, 7, 8), 2).fold(10)(add)) //66
  //cogroup lazy操作
  println(rdd1.cogroup(rdd2).collect.mkString(","))
  println(rdd1.groupBy(_._1).collect.mkString(","))


  //RDD间操作
  val r1 = sc.parallelize(List("A","B","C"),2)
  val r2 = sc.parallelize(List("1","1","2","3"),3)
  //笛卡尔积 lazy
  println(r1.cartesian(r2).collect.mkString(","))
  //union 不去重，分区合并 lazy
  val union = r1.union(r2)
  println(union.collect.mkString(",")+" union后的分区数："+union.partitions.length)

  //PairRDD聚合
  //combineByKey 是PairRDD聚合的底层实现
  val rdd = sc.parallelize(List((1, "www"), (1, "iteblog"), (1, "com"), (2, "bbs"), (2, "iteblog"), (2, "com"), (3, "good")))
  println("combineByKey："+rdd.combineByKey(List(_), (x: List[String], y: String) => y :: x, (x: List[String], y: List[String]) => x ::: y).collect.mkString(",")) // Array((1, List(www, iteblog, com)), (2, List(bbs, iteblog, com)), (3, List(good)))

  val m1 = sc.parallelize(List(1->"a",1->"b",2->"b",3->"c",3->"c",3->"c",3->"c"),2)
  println(m1.aggregateByKey("zeroValue")((x,y)=>x+" "+y,(a,b)=>a+"="+b).collect.mkString(","))
  println(m1.reduceByKey((x,y)=>x+" "+y).collect.mkString(","))

  //排序
  val s1 = sc.parallelize(List((3, "a"), (7, "b"), (5, "c"), (3, "b"), (6, "c"), (9, "d")), 3)
  //分区内排序
  val sorted = s1.repartitionAndSortWithinPartitions(new HashPartitioner(3))
  println("每个分区有哪些元素："+
    sorted.mapPartitionsWithIndex{
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
  println(sorted.collect.mkString(","))

}
