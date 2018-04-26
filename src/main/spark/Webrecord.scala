import org.apache.spark.{SparkContext, SparkConf}
import org.apache.log4j.{Logger, Level}

/**
 * 移动终端上网数据分析
 */
object Webrecord {
  //关闭日志输出
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  def main(args: Array[String]) {
    val conf = new SparkConf().setMaster("local").setAppName("webrecord")
    val sc = new SparkContext(conf)

    //加载数据
    val webrecord = sc.textFile("file/webrecord.txt")

    //预处理
    val fields = sc.broadcast(List("nodeid", "ci", "imei", "app", "time", "uplinkbytes", "downlinkbytes"))
    val mobile = webrecord.map(_.split(",")).filter(_.length == fields.value.length)
    println("预处理的结果：")
    for(i <- mobile.collect()){
      println(i.toBuffer)
    }

    //统计App访问次数
    val web_cnt = mobile.map(line => (line(fields.value.indexOf("app")),1)).reduceByKey(_+_).sortBy(_._2,false)
    println("App访问次数："+web_cnt.collect().toBuffer)

    //统计DAU
    val dau = mobile.map(line => line(fields.value.indexOf("imei"))+":"+line(fields.value.indexOf("time")))
      .distinct().map(line => (line.split(":")(1),line.split(":")(0)))
      .groupBy(_._1).mapValues(_.size).sortBy(_._1)
    println("DAU："+dau.collect().toBuffer)

    //统计MAU
    val mau = mobile.map{line =>
      val time = line(fields.value.indexOf("time"))
      val month = time.substring(0, time.lastIndexOf("-"))
      line(fields.value.indexOf("imei"))+":"+month
    }
      .distinct().map(line => (line.split(":")(1),line.split(":")(0)))
      .groupBy(_._1).mapValues(_.size).sortBy(_._1)
    println("DAU："+mau.collect().toBuffer)

    //统计app上下行字节数
    val bytes = mobile.map{line =>
      val uplinkbytes:Int = line(fields.value.indexOf("uplinkbytes")).toInt
      val downlinkbytes:Int = line(fields.value.indexOf("downlinkbytes")).toInt
      (line(fields.value.indexOf("app")),(uplinkbytes,downlinkbytes))
    }.reduceByKey((x,y) => (x._1+y._1,x._2+y._2))
    println(bytes.collect.toBuffer)

  }
}
