package actor
import scala.actors.Actor
import scala.actors.Actor._  //创建匿名Actor需要用到actor方法
/**
 * Created by Administrator on 2018/4/27 0027.
 */
object ActorDemo1 extends Actor{
  //重写act方法
  override def act():Unit = {
    for(i <- 1 to 10){
      println("actor1:"+i)
      Thread.sleep(1000)
    }
  }
}
object ActorDemo2 extends Actor{
  override def act():Unit = {
    for(i <- 1 to 10){
      println("actor2:"+i)
      Thread.sleep(1000)
    }
  }
}
object TestActor extends App{
  //调用start方法启动actor
  ActorDemo1.start()
  ActorDemo2.start()

  //创建匿名actor，不需要start即可自动执行
  val actorDemo3 = actor
  {
    for(i<- List("a","b","c"))
      println("actor3:"+i)
  }
}
