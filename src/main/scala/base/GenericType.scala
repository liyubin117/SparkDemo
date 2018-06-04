package base

/**
 * Created by liyubin on 2018/6/2 0002.
 */
class UpperBound[T <: Comparable[T]]{  //上界
  def greater(first:T, second:T):T={
    if(first.compareTo(second) > 0) first else second
  }
}

class ViewBound[T <% Ordered[T]]{ //ViewBound，此处T类型被隐式转换为Ordered[T]，从而具备 > 方法
  def greater(first:T, second:T):T={
    if(first > second) first else second
  }
}

class ContextBoundBase[T](implicit e:T){ //ContextBound原理
  def print = println("this is " + e)
}

class ContextBound[T : Ordering]{  //ContextBound，用于在当前作用域查找指定类型
  def greater(first:T, second:T):T={
    val ord=implicitly[Ordering[T]]
    if (ord.gt(first,second)) first else second
  }
}

object GenericType extends App{
  println("----UpperBound-----")
  val upperInt = new UpperBound[Integer]
  println(upperInt.greater(1,2))

  val upperGoddess = new UpperBound[Goddess]
  val mrs1 = new Goddess("yang",90)
  val mrs2 = new Goddess("wang", 100)
  println(upperGoddess.greater(mrs1, mrs2))

  println("----ViewBound-----")
  import MyPredef.selectGirl
  val viewGirl = new ViewBound[Girl]
  val g1 = new Girl("zhou",100,20)
  val g2 = new Girl("wu",120,30)
  println(viewGirl.greater(g1,g2))

  println("----ContextBound-----")
  implicit val x = 10
//  implicit val y = 20 //定义多个满足类型的隐式值时会因歧义而报错
  val cb = new ContextBoundBase[Int]
  cb.print
//  implicit val d = 120.5.toDouble
//  val cb2 = new ContextBoundBase[Double]
//  cb2.print  //无d会报错，因为在上下文找不到Double隐式值

  val contextGirl = new ContextBound[Girl]
  println(contextGirl.greater(g1,g2))

}

class Goddess(val name:String, val faceValue:Int) extends Comparable[Goddess]{
  override def compareTo(goddess: Goddess):Int={
    if(this.faceValue >= goddess.faceValue){
      return 1
    }else if(this.name >= goddess.name){
      return 1
    } else {
      return -1
    }
  }
  override def toString():String={
    return "["+this.name+","+this.faceValue+"]"
  }
}

class Girl(val name:String, val faceValue:Int, val age:Int){
  override def toString():String={
    return "["+this.name+","+this.faceValue+","+this.age+"]"
  }
}