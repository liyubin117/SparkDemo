package base

/**
 * Created by Administrator on 2018/4/27 0027.
 */
//特质
trait Flyable{
  val distance:Int = 300
  //未实现的方法
  def fight
  //已实现的方法
  def fly = println("I can fly")
}
//抽象类
abstract class Animal {
  //无值字段
  val name:String
  //未实现的方法
  def run
  //已实现的方法
  def climb = println("I can climb")
}

class Human extends Animal with Flyable{
  override val distance = 100
  override def fight = println("I can fight")

  override val name = "liyubin"
  override def run = println("I can run")
  override def climb = println("override climb")
}

object TestHuman extends App{
  val h = new Human
  h.climb
}