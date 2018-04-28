package base

/**
 * Scala通过使用object实现：
 * 1、静态方法、变量
 * 2、单例对象
 * 3、与同名class使用时是伴生对象，可互相访问私有类变量
 */
class Singleton {
  private val one = Singleton.one
  def getOne(): Int ={
    1
  }
}

object Singleton{
  //静态方法
  def staticGetTwo():Int={
    2
  }
  //静态变量
  val hello = "hello"
  private var one = 1

  def main(args: Array[String]) {
    val s = new Singleton()
    println(s.getOne())
    println(s.one)

    println(Singleton.staticGetTwo())
    println(Singleton.hello)

//    val o = new SingleObject //无法新建，因为是单例对象
  }
}

object SingleObject{

}