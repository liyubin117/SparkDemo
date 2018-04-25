import org.apache.spark.{SparkContext, SparkConf}

object Wordcount {
  def main(args: Array[String]) {
    val conf = new SparkConf().setMaster("local").setAppName("wordcount")
    val sc = new SparkContext(conf)

    val lines = sc.parallelize(List("a c b","b a b","c a e"))
    val cnt = lines.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).sortByKey(true)
    println(cnt.collect().toBuffer)
  }
}
