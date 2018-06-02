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

object GenericType extends App{
  val upperInt = new UpperBound[Integer]
  println(upperInt.greater(1,2))

  val upperGoddess = new UpperBound[Goddess]
  val mrs1 = new Goddess("yang",90)
  val mrs2 = new Goddess("wang", 100)
  println(upperGoddess.greater(mrs1, mrs2))

  import MyImplicit.selectGirl
  val viewGirl = new ViewBound[Girl]
  val g1 = new Girl("zhou",100,20)
  val g2 = new Girl("wu",120,30)
  println(viewGirl.greater(g1,g2))
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