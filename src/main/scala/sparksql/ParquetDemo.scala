package sparksql

import java.io.File

import org.apache.spark.sql.types.{StringType, IntegerType, StructField, StructType}
import org.apache.spark.sql.{Row, DataFrame, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by Administrator on 2018/5/8 0008.
 */
object ParquetDemo extends App {
  val sc = new SparkContext(new SparkConf().setAppName("ParquetDemo").setMaster("local"))
  sc.setLogLevel("ERROR")
  val sqlContext = new SQLContext(sc)
  //读取Parquet文件
  val parquet:DataFrame = sqlContext.read.parquet("file/write_person2.parquet")
  println("Schema信息：")
  parquet.printSchema()
  //使用
  parquet.registerTempTable("person")
  val res = sqlContext.sql("select * from person")
  res.show()

  //写Parquet文件
  //装载数据
  val lineRDD = sc.parallelize(List("1,li,10","2,yu,20","3,bin,30")).map(_.split(","))
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
  //输出成parquet文件
  df.printSchema()
  val url2="file/write_person2.parquet"
  val dir2 = new File(url2)
  if(dir2.exists()){
    println("删除旧文件")
    for(f<-dir2.listFiles()){
      f.delete()
    }
    println(dir2.delete())
  }
  df.write.parquet(url2)

}
