package algorithm

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 维护两个数据集：一个由（pageID，linkList）的元素组成，包含每个页面的相邻页面的列表；另一个由（pageID，rank）元素组成，包含每个页面的当前排序值。它按如下步骤进行计算。
将每个页面的排序值初始化为1.0。
在每次迭代中，对页面p，向其每个相邻页面（有直接链接的页面）发送一个值为rank(p)/numNeighbors(p)的贡献值。
将每个页面的排序值设为0.15 + 0.85 * contributionsReceived。（此处其实不够精确，应该是0.15/网页数）
最后两个步骤会重复几个循环，在此过程中，算法会逐渐收敛于每个页面的实际PageRank值。在实际操作中，收敛通常需要大约10轮迭代。
 */
object SparkPageRank {

  Logger.getLogger("org").setLevel(Level.ERROR)
  Logger.getLogger("com").setLevel(Level.ERROR)

  def showWarning() {
    System.err.println(
      """WARN: This is a naive implementation of PageRank and is given as an example!
        |Please use the PageRank implementation found in org.apache.spark.graphx.lib.PageRank
        |for more conventional use.
      """.stripMargin)
  }

  def main(args: Array[String]) {
    if (args.length < 1) {
      System.err.println("Usage: SparkPageRank <file> <iter>")
      System.exit(1)
    }

    showWarning()

    val conf = new SparkConf().setAppName("SparkPageRank").setMaster("local[*]")
    val spark = new SparkContext(conf)

    val iters = if (args.length > 1) args(1).toInt else 10
    val lines = spark.textFile(args(0))
    val website = lines.map{ s =>
      val parts = s.split("\\s+")
      (parts(0), parts(1))
    }.distinct()
    val links = website.groupByKey().cache()
    val webcnt = website.count()
    var ranks = links.mapValues(v => 1.0)


    println("ranks: "+ranks.collect.mkString(","))
    println("links: "+links.collect.mkString(","))
    println(links.join(ranks).values.collect.mkString(","))
    println(links.join(ranks).collect.mkString(","))

    val test: Array[(Iterable[String], Double)] = links.join(ranks).values.collect

    val test2: RDD[(Iterable[String], Double)] = links.join(ranks).values

    for (i <- 1 to iters) {
      val contribs: RDD[(String, Double)] = links.join(ranks).values.flatMap({ case (urls, rank) =>
        val size = urls.flatMap(_.split("\\-")).size
        urls.flatMap(url => url.split("\\-")).map((_,rank/size))
      })
      ranks = contribs.reduceByKey(_ + _).mapValues(0.15/webcnt + 0.85 * _)
    }

    val output = ranks.collect()
    output.foreach(tup => println(s"${tup._1} has rank:  ${tup._2} ."))

    spark.stop()
  }
}