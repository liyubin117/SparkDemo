package base

import scala.collection.mutable.{ArrayBuffer, _}

object Collection {
  def main(args: Array[String]) {
    //数组动态初始化，下标从0开始
    val arr1 = new Array[Int](8); println(arr1.toBuffer)
    val arr2 = new Array[String](8); println(arr2.toBuffer)
    //数组静态初始化
    val arr3 = Array("one", "two", "three"); println(arr3.toBuffer); println(arr3(1))
    //变长数组
    val arr = ArrayBuffer[Int]()
    //追加元素
    arr += 1; arr += (2,3); arr ++= Array(1,2,3); println(arr)
    //插入元素，在param1前插入param2、param3...
    arr.insert(0, -2, -1); println(arr)
    //删除元素，从param1开始删param2个
    arr.remove(0,1); println(arr)
    //遍历
    for(i <- (0 until arr.length).reverse) print(arr(i)+" "); println()
    //拉链操作
    val arr4 = Array(1,2,3)
    val arr5 = Array("li","yu","bin")
    val arr6 = arr4.zip(arr5)
    println(arr6)
    //常用方法
    println("和："+arr.sum)
    println("排序后："+arr.sorted)

    //元组，下标从1开始
    val tuple1 = (1,2,("one","two"),3.14,Map(1->"a",2->"b"),Array(10,20))
    println(tuple1._3._1)

    //映射
    //两种创建形式
    val map1 = Map(1->"scala", 2->"java", 3->"python")
    val map2 = Map((1,"scala"), (2,"java"), (3,"python"))
    println(map1(1))
    //默认Map是不可变的，若要修改需要引入可变Map
    map1(1)="scala2"
    println(map1.keys)
    println(map1.getOrElse(4,-1))
    //array->map
    println(arr6.toMap)

    //List 不可变Seq
    val list1 = List(1,2); val list2 = List(3,4,("one","two"))
    println(list1 ::: list2)
    println(10 +: list1)
    println(list1 :+ 10)
    //ListBuffer 可变List
    val listbuffer = ListBuffer(1,2,3)
    println(listbuffer += 10)
    listbuffer.append(10); println(listbuffer)
    val listbuffer2 = ListBuffer(4,5,6)
    listbuffer ++ listbuffer2; println(listbuffer)
    println(listbuffer ++= listbuffer2)

  }
}
