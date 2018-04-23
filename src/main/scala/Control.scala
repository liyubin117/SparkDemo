/**
 * Created by Administrator on 2018/4/20 0020.
 */

object Control {
  def main(args: Array[String]) {
    //判断
    val x=10
    val a=if(x<1) 1 else if(x<10) 9 else 10
    println(a)

    //循环
    for(i <- 1 to 2) println(i)
    val arr = Array("one","two")
    for(i <- arr) println(i)
    for(i <- 1 to 3;j <- 1 to 3 if(i != j)) println(i*10 + j)
    val res = for(i <- 1 until 10) yield i; println(res)
  }
}
