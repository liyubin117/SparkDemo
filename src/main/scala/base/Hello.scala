package base

/**
 * Created by Administrator on 2018/4/20 0020.
 */
class Hello {

}

object Hello{
  def main(args: Array[String]) {
    println("hello")

    //内部范围创建与外部范围同名的变量。scala允许，但java不允许
    val a = 1;
    {
      var a = 2;
      println(a)
    }
    println(a)
  }
}
