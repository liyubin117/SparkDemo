/**
 * Created by Administrator on 2018/4/20 0020.
 */
object Func {
  def main(args: Array[String]) {
    //method
    def func(s:String){println(s)}; func("hello func")
    def add(x:Int, y:Int):Unit = println(x+y); add(1,2)

    //function
    val f1 = (x:Int,y:Int) => println(x+y); f1(1,2)

    //function-style
    def m1(f: (Int,Int)=>Unit) = f(3,4)
    m1(f1)
    //method->function
    val f2 = add _
    m1(f2)


  }
}
