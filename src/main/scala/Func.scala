/**
 * Created by Administrator on 2018/4/20 0020.
 */
object Func {
  def main(args: Array[String]) {
    //方法
    def func(s:String){println(s)}; func("hello func")
    def add(x:Int, y:Int):Unit = println(x+y); add(1,2)

    //函数
    val f1 = (x:Int,y:Int) => println(x+y); f1(1,2)

    //函数式编程：函数可作为参数使用
    def m1(f: (Int,Int)=>Unit) = f(3,4)
    m1(f1)
    //方法->函数
    val f2 = add _
    m1(f2)

    //函数式编程例子
    val list0 = List(0,8,5,10,2)
    val arr = Array(1,8,9,10,2,9,15,20,18)
    //元素都乘2倍
    println(list0.map(_ * 2)); println(list0.map((_,1)))
    //取偶数元素
    println(list0.filter(_ % 2 == 0))
    //求并集
    println(list0.union(List(2,3,5)))
    //4个元素一组生成新Iterator集合
    println(list0.grouped(2).toBuffer)
    //Iterator->List
    println(list0.grouped(2).toList)
    //将list[List]扁平化
    println(list0.grouped(2).toList.flatten)
    //以空格分开再扁平化
    println(List("a d e","e a f","f a d").map(_.split(" ")).flatten)
    println(List("a d e","e a f","f a d").flatMap(_.split(" ")))
    //并行计算求和
    println(arr.par.sum)
    //按特定顺序求和
    println(arr.reduce(_+_))
    println(arr.par.reduce(_-_))

  }
}
