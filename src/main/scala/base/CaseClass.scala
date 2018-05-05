package base

import scala.util.Random

//模式匹配类
object CaseClass extends App{
  val arr = Array(Man(27,"li"),Superman)

  arr(Random.nextInt(arr.length)) match {
    case Man(18,"a") => println("Man a")
    case Man(a,b) => println(s"Man $a,$b")
    case Superman => println("a superman")
  }
}

//case class是多例样类，case object是单例样类。会自动生成apply函数，即不需要new
case class Man(id:Int,name:String)
case object Superman
