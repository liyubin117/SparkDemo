package base

/**
 * Created by Administrator on 2018/4/27 0027.
 */
class Apply(val id:Int, var name:String) {

}

object Apply{
  //注入
  def apply(id:Int, name:String, gender:String):Apply={
    new Apply(id,name)
  }
  //提取
  def unapply(apply:Apply)={
    if(apply == null){
      None
    }else{
      Some(apply.id, apply.name, "M")
    }
  }
}

object TestApply extends App{
  val apply = Apply(1,"liyubin", "男") //隐式调用了apply方法
  println(apply.name)

  apply match { //隐式调用了unapply方法
    case Apply(1,name,m) => println(name+" "+m)
    case _ => println("No Match")
  }
}
