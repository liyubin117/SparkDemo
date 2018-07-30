package base

/**
 * Created by Administrator on 2018/4/27 0027.
 */
object StringDemo extends App{
  //s"$变量名"  显示某变量值
  val name = "liyubin No.1"
  println(s"name: ${name}")

  //split将一个字符串按指定间隔符生成一个Array
  val list: Array[String] = name.split(" ")
  println(list.toBuffer)
}
