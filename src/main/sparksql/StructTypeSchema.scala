import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.sql.types.{StringType, IntegerType, StructField, StructType}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by lixin on 2018/5/3.
 */
object StructTypeSchema extends App{
  val sc = new SparkContext(new SparkConf().setAppName("StructTypeSchema").setMaster("local[*]"))
  sc.setLogLevel("ERROR")
  val sqlContext = new SQLContext(sc)

  //装载数据
  val lineRDD = sc.textFile("file/person.txt").map(_.split(","))

  //通过StructType定义模型
  val schema = StructType(
    List(
      StructField("id", IntegerType, false),
      StructField("name", StringType, false),
      StructField("age", IntegerType, true)
    )
  )
  val schemaRDD = lineRDD.map(x => Row(x(0).toInt,x(1),x(2).toInt))


  //转换成DataFrame
  val df = sqlContext.createDataFrame(schemaRDD, schema)

  //sql方式转换数据
  //注册临时表
  df.cache()
  df.registerTempTable("person")

  //转换
  val res = sqlContext.sql("select name,count(1) from person where age>25 group by name order by name desc")
  res.show()

}