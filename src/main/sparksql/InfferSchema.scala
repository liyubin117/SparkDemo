import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by lixin on 2018/5/3.
 */
object InfferSchema extends App{
  val sc = new SparkContext(new SparkConf().setAppName("InfferSchema").setMaster("local[*]"))
  sc.setLogLevel("ERROR")
  val sqlContext = new SQLContext(sc)

  //装载数据
  val lineRDD = sc.textFile("file/person.txt").map(_.split(","))

  //通过反射定义模型
  case class Person(id:Int, name:String, age:Int)
  val schemaRDD = lineRDD.map(x => Person(x(0).toInt, x(1), x(2).toInt))

  //转换成DataFrame
  import sqlContext.implicits._
  val df = schemaRDD.toDF()

  //sql方式转换数据
  //注册临时表
  df.registerTempTable("person")
  //转换
  val res = sqlContext.sql("select name,count(1) from person where age>25 group by name order by name desc")
  res.show()
  
}