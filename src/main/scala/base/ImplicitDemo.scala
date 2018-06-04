package base

import scala.io.Source

/**
 * Created by liyubin on 2018/6/4
 */
class RichFile(path:String){
  def read():String={
    Source.fromFile(path).mkString("")
  }
}

class PoorFile(val path:String)

object ImplicitDemo extends App{
  val file = new RichFile("file/text1.txt")
  println(file.read())
  println("------------")

  //隐式方法
  implicit def PoorToRich(poorFile: PoorFile):RichFile = new RichFile(poorFile.path)
  val file2 = new PoorFile("file/text1.txt")
  println(file2.read())
  println("------------")

  //隐式值：隐式变量、隐式参数
//  implicit val str2 = "implicit string"  //必须无歧义，当有多个变量的类型满足隐式转换时，会发生歧义而无法执行
  def printStr(implicit str:String="hello")=println(str)
  printStr
  printStr()
  implicit val str = "implicit string"
  printStr
  printStr("good")
  println("------------")

  //隐式类
  case class Rectangle(width: Int, height: Int) {
    override def toString="[width:"+width+", height:"+height+"]"
  }
  implicit class RectangleMaker(width: Int) {  //此隐式类自动转换为：implicit def RectangleMaker(width: Int) = new RectangleMaker(width)，将Int转换为RectangleMaker，可调用X或Y
    def X(height: Int) = Rectangle(width, height)
    def Y(height: Int) = Rectangle(height, width)
  }
  println(2 X 3)
  println(2 Y 3)
}
