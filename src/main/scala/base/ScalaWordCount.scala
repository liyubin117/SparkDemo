package base


object ScalaWordCount {
  def main(args: Array[String]) {
    val lines = List("a c b","b c a","d a c")

    val cnt = lines.map(_.split(" ")).flatten.map((_,1)).groupBy(_._1).mapValues(_.size).toList.sortBy(_._1)
    println(cnt)


    //单词元组
    val tuples = lines.flatMap(_.split(" ")).map((_,1))
    //对单词分组
    val grouped = tuples.groupBy(_._1)
    //分组后对值map size
    val size = grouped.mapValues(_.size)
    //排序
    val sorted = size.toList.sortBy(_._1)
    println(sorted)

  }
}
