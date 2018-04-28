package base

/**
 * 声明类时不需要加public关键字，默认是public
 * 一个类文件可以声明多个类
 * object修饰静态类
 */
class Person {
  //val变量是只读的，相当于只有getter无setter
  val id: Int = 100
  //var变量是可写的，既有getter又有setter
  var name: String = _
  //private修饰的变量，属于类私有变量，只有本类和伴生对象才能访问
  private var age: Int = _
  //private[this]修饰的变量，属于对象私有变量，只有本类才能访问
  private[this] var addr: String = _
}

//Person类的伴生对象
object Person {
  def main(args: Array[String]) {
    val p = new Person()
    println(p.id)
    //val变量不可写
    //    println(p.id = 123)
    p.name = "liyubin"
    println(p.name)
    println(p.age)
    //    println(p.addr)
  }
}

object Test {
  def main(args: Array[String]) {
    val p = new Person()
    p.name = "good"
    println(p.name)
    //无法访问
    //    println(p.age)
    //    println(p.addr)
  }
}

//有构造器的类
//类一行的是主构造器
//age:Int只能在本类调用，相当于private[this] val
class Construct(val id:Int, var name:String, age:Int = 26){
  var gender:String = _

  //辅助构造器
  def this(id:Int, name:String, age:Int, gender:String)={
    this(id, name, age) //第一行必须先调用主构造器
    this.gender = gender
  }

  def getAge():Int={
    age
  }
}

object Construct{
  def main(args: Array[String]) {
    val con = new Construct(1,"yubin",27)
    println(con.name)
//    con.id=10
    con.name="liyubin"
    println(con.name)
//    println(con.age)
    println(con.getAge())

    val con2 = new Construct(2,"li",28,"男")
    println(con2.gender)
  }
}

