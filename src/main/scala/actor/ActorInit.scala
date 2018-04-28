package actor
import scala.actors.Actor
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
}
