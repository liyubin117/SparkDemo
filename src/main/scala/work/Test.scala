package work

import scala.collection.mutable.ListBuffer
import scala.io.Source

/**
 * Created by liyubin on 2018/5/24 0024.
 */
object Test extends App {
  val in = Source.fromFile("file/test.txt")
//  var listBuffer=ListBuffer[String]
  val listBuffer=ListBuffer[String]()
  in.getLines().foreach(line => listBuffer+=line)
  for (file <- listBuffer){
    val text =
    print(s"""
/data/app/mysql/bin/mysql -h 127.0.0.1 -ubbqq_oss -p8iholjsx5jnh --local-infile=1 bbqqcn_data_log -e "LOAD DATA LOCAL INFILE '/home/dba/testrsync/tab_hero_up/${file}' replace into table tab_hero_up FIELDS TERMINATED BY ',' LINES TERMINATED BY '\\n' (zid,record_time,account_type,account_id,user_id,iuin,device_os,hero_id,prelv,afterlv,cnt)"""")
  }



}
