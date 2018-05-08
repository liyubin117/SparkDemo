import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by Administrator on 2018/5/8 0008.
 */
object ParquetDemo extends App {
  val sc = new SparkContext(new SparkConf().setAppName("ParquetDemo").setMaster("local"))
  sc.setLogLevel("ERROR")
  val sqlContext = new SQLContext(sc)
  //读取Parquet文件
  val parquet:DataFrame = sqlContext.read.parquet("file/person.parquet")
  println("Schema信息：")
  parquet.printSchema()
  //使用
  parquet.registerTempTable("person")
  val res = sqlContext.sql("select * from person")
  res.show()
  //写Parquet文件
//  parquet.write.parquet("file/write_person.parquet")  //windows上无法执行
}
