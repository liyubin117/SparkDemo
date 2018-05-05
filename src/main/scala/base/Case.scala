package base

import scala.util.Random

//模式匹配

//匹配字符串
object MatchStr extends App{
  val arr = Array("li","yu","bin","else")
  val res = arr(Random.nextInt(arr.length))
  println(res)

  res match {
    case "li" => println("李")
    case "yu" => println("宇")
    case "bin" => println("彬")
    case _ => println("其他")
  }
}

//匹配类型
class MatchType

object MatchType {
  def main(args: Array[String]) {
    //匹配类型
    val arr = Array("a", 123, 3.14, MatchType,(1,2))
    val res = arr(Random.nextInt(arr.length))
    println(res)

    res match {
      case str:String => println(s"string $str")
      case int:Int => println(s"int  $int")
      case double:Double => println(s"double $double")
      case matchtype:MatchType => println(s"matchtype $matchtype")
      case _ => println("other type")
    }

    //匹配数组
    val array = Array(3,2,"a",100)
    array match {
      case Array(4,x,y,z) => println("4开头，共4个")
      case Array(3,_,_) => println("3开头，共3个")
      case Array(3,_,_,_) => println("3开头，共4个")
    }

    //匹配元组
    val tuple = (3,2,"a",100)
    tuple match {
      //个数必须与元组一致
      case (4,x,y,z) => println("4开头，共4个")
      case (3,_,_,_) => println("3开头，共4个")
      case _ => println("其他")
    }

    //匹配list
    val list = List(1,5,"a")
    list match {
      case 1 :: 2 :: Nil => println("1 2")
      case List(_,_,"a") => println("3个元素")
    }
  }
}