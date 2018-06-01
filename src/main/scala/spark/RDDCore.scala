package spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.scheduler.Stage
import org.apache.spark.util.CallSite
import org.apache.spark.{MapOutputTracker, ShuffleDependency, SparkContext, SparkConf}

import scala.collection.mutable.{HashMap, Stack, HashSet}

/**
 * Created by liyubin on 2018/6/1 0001.
 * RDD核心组成：分区、依赖、计算
 * RDD优化：分区位置、分区算法
 */
object RDDCore extends App{
  //只输出ERROR日志
  Logger.getLogger("org").setLevel(Level.ERROR)
  Logger.getLogger("akka").setLevel(Level.ERROR)

  val conf = new SparkConf().setMaster("local").setAppName("wordcount")
  val sc = new SparkContext(conf)

  val lines = sc.parallelize(List("a c b","b a b","c a e"))
  val words: RDD[(String, Int)] = lines.flatMap(_.split(" ")).map((_,1))
  val cnt = words.reduceByKey(_+_).sortByKey(true).reduceByKey(_+_)

  println("lineage:\n"+cnt.toDebugString)
  for(dep <- cnt.dependencies){
    println(dep)
  }

  println("----getParentStages----")
  println(getParentStages(cnt))

  //类似于org.apache.spark.scheduler.DAGScheduler
  def getParentStages(rdd: RDD[_]): List[RDD[_]] ={
//    val parents = new HashSet[Stage]
    val visited = new HashSet[RDD[_]]
    val waitingForVisit = new Stack[RDD[_]]
    def visit(r: RDD[_]) {
      if (!visited(r)) {
        visited += r
        for (dep <- r.dependencies) {
          dep match {
            case shufDep: ShuffleDependency[_, _, _] =>
//              parents += getShuffleMapStage(shufDep, jobId)
                println("这是一个ShuffleDependency:"+dep)
            case _ =>
              waitingForVisit.push(dep.rdd)
          }
        }
      }
    }
    waitingForVisit.push(rdd)
    while (!waitingForVisit.isEmpty) {
      visit(waitingForVisit.pop())
    }
//    parents.toList
    visited.toList
  }
}
