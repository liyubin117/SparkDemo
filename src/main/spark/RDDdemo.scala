import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by Administrator on 2018/4/28 0028.
 */
object RDDdemo extends App{
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  val sc = new SparkContext(new SparkConf().setAppName("BroadAcc").setMaster("local"))

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
}
