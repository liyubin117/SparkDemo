package base

import java.io.{File, PrintWriter}

import scala.io.Source

/**
 * Created by liyubin on 2018/5/21 0021.
 */
object FileDemo extends App{
  //读文件
  val file = Source.fromFile("file/text1.txt")
  file.getLines().foreach(println)
  //读网络资源
  val webFile = Source.fromURL("http://www.baidu.com")
  webFile.getLines().foreach(println)

  //写文件
  val out = new PrintWriter(new File("file/out.txt"))
  for(i <- 1 to 100){
    out.println(i)
  }
  out.close()

  //输入
  print("Please enter your input:")
  val line = Console.readLine()
  println("Your input is: "+line)
}
