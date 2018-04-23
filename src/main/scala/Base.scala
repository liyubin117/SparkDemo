/**
 * Created by Administrator on 2018/4/23 0023.
 */
class C1{
  def init():Unit={
    println("C1.init()")
  }
}

object Base {
  def main(args: Array[String]) {
    //lazy 只有在实际用到时才会执行
    val v1 = new C1().init

    println("lazyV1 before create")
    lazy val lazyV1 = new C1().init
    println("lazyV1 after create")
    lazyV1
  }
}
