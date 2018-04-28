package base

/**
 * Created by Administrator on 2018/4/23 0023.
 */
class Lazy{
  def init():Unit={
    println("C1.init()")
  }
}

object Lazy {
  def main(args: Array[String]) {
    //lazy 只有在实际用到时才会执行
    val v1 = new Lazy().init

    println("lazyV1 before create")
    lazy val lazyV1 = new Lazy().init
    println("lazyV1 after create")
    lazyV1
  }
}
