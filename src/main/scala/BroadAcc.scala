import org.apache.log4j.{Logger, Level}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 共享变量
 * 广播变量和累加变量
 */
object BroadAcc {
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  def main(args: Array[String]) {
    val sc = new SparkContext(new SparkConf().setAppName("BroadAcc").setMaster("local"))

    println("广播变量")
    val broad = sc.broadcast(List("a","b","c"))
    println("value："+broad.value)
    println("第二个值："+broad.value(1))
    println("c的位置："+broad.value.indexOf("c"))
    println("value.size："+broad.value.size)
    println("value.length："+broad.value.length)

    println("累加变量")
    val acc = sc.accumulator(10,"My Accumulator")
    acc += 20
    println("value："+acc.value)
  }
}
