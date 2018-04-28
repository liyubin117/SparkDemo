package base.org.pac1

/**
 * Created by Administrator on 2018/4/27 0027.
 */
//只有org.pac1这个包有访问此类的权限
private[pac1] class Private private (val id:Int,var name:String){
  //只有本类和伴生对象有访问权限
  private val hello="hello"
  //只有本类有访问权限
  private[this] var hi="hi"

}

object Private extends App{
  val p = new Private(1,"liyubin")
  println(p.hello)
//  println(p.hi) //不能访问private[this]
}

object TestPrivate extends App{
//  val p = new Private(2,"No.1")
//  println(p.hello)  //不能访问private
  //  println(p.hi) //不能访问private[this]
}
