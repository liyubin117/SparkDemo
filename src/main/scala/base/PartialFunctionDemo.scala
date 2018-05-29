package base

//偏函数: 常用于输入模式匹配
//是PartialFunction[A,B]的实例，A是参数类型，B是返回类型
object PartialFunctionDemo extends App{
  def m3(pf:PartialFunction[String,Int]):Int={
    val result = pf.apply("one")
    result
  }


  def m1: PartialFunction[String, Int] = {
    case "one" => println("case 1"); 1
    case "two" => println("case 2"); 2
    case _ => println("case other"); -1
  }

  //m3即一个参数为偏函数的方法
   m3 {
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
