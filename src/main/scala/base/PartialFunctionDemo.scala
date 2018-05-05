package base

//偏函数: 常用于输入模式匹配
object PartialFunctionDemo extends App{
  def m1: PartialFunction[String, Int] = {
    case "one" => println("case 1"); 1
    case "two" => println("case 2"); 2
    case _ => println("case other"); -1
  }

  //与m1等效
  def m2(str: String):Int={
    str match {
      case "one" => println("case 1"); 1
      case "two" => println("case 2"); 2
      case _ => println("case other"); -1
    }
  }

  println(m1("one"))
  println(m2("two"))
}
