package base

/**
 * Created by Administrator on 2018/4/20 0020.
 */
object Func {
  def main(args: Array[String]) {
    //方法
    def func(s:String){println(s)}; func("hello func")
    def add(x:Int, y:Int):Unit = println(x+y); add(1,2)
    def simple1 = "simple1"  //无参方法可以去掉括号

    //函数
    val f1 = (x:Int,y:Int) => println(x+y); f1(1,2)

    //函数式编程：函数可作为参数使用
    def m1(f: (Int,Int)=>Unit) = f(3,4)
    m1(f1)
    //方法->函数
    val f2 = add _
    m1(f2)
    m1(add)

    //变长参数
    println("------变长参数------")
    def args(x:Int,y:Int*)=println(y.length)
    args(1)
    args(1,2,3)
    def args2(x:Int*)=println(x.length)  //变长参数可接受空参
    args2()

    //柯里化
    println("--------柯里化----------")
    def currying(x:Int)(y:Int):Int=x*y
    println(currying(3)(4))
    def currying1 = currying(3) _
    println(currying1(5))
    def curryingImplicit(x:Int)(implicit y:Int=5)=x*y
    println(curryingImplicit(2))
    println(curryingImplicit(2)(8))
    //执行时会在上文中找隐式定义变量，只要类型符合就行，当有多个符合时，则会报错
    implicit val x = 100
    println(curryingImplicit(3))

    //函数式编程例子
    println("--------函数式编程例子---------")
    val list0 = List(0,8,5,10,2)
    val arr = Array(1,8,9,10,2,9,15,20,18)
    //元素都乘2倍
    println(list0.map(_ * 2)); println(list0.map((_,1)))
    //对map的value进行map
    println(Map("a"->1,"b"->2,"c"->3).mapValues(_*2))

    val a2 = Array(("B",(List("A","D"),1.0)),("D",(List("B","C"),1.0)),("A",(List("B","C","D"),1.0)),("C",(List("C"),1.0)))
    a2.map(_._2).map(x=>(x._1,x._2/x._1.size)).foreach(println)

    a2.map(_._2).map(x=>(x._1,x._2/x._1.size)).flatMap(_._1).foreach(println)

    a2.map(_._2).flatMap({ case (urls, rank) =>
        val size = urls.size
        urls.map(url => (url,rank/size))
    }).foreach(println)

    println(List("A","D").size)

    val a3: Array[String] = "A-B-C".split("-")



    println("排序")
    println(List((1,"a"),(3,"b"),(2,"c")).sortBy(_._1))
    println(List(1,2,3).sortBy(_.toString))

    //取偶数元素
    println(list0.filter(_ % 2 == 0))
    //求并集
    println(list0.union(List(2,3,5)))
    //求交集
    println(list0.intersect(List(5,8,9)))
    //求差集
    println(list0 diff List(2,10))

    println("4个元素一组生成新Iterator集合")
    println(list0.grouped(2).toBuffer)

    //Iterator->List
    println(list0.grouped(2).toList)
    //将list[List]扁平化
    println(list0.grouped(2).toList.flatten)

    //以空格分开再扁平化
    println("扁平化")
    println(List("a d e","e a f","f a d").map(_.split(" ")).flatten)
    println(List("a d e","e a f","f a d").flatMap(_.split(" ")))
    //聚合
    println("聚合")
    println(List(List(1,2,3),List(4,5),List(6)).aggregate(0)(_+_.sum, _+_))
    println(List(List(1,2,3),List(4,5),List(6)).flatten.reduce(_+_))
    //fold
    val arr2 = Array(("a",1),("b",2),("c",3))
    println(arr2.foldLeft(0)(_+_._2))
    val tuple = Array(1,2,3)
    println(tuple.reduceLeft(_+_))

    //并行计算求和
    println(arr.par.sum)
    //按特定顺序求和
    println(arr.reduce(_+_))
    println(arr.par.reduce(_-_))

    println("分组")
    println(List(("a",1),("b",1),("a",2),("c",3)).groupBy(_._1))

  }
}
