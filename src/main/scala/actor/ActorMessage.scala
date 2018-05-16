package actor

import scala.actors.{Future, Actor}

/**
 * Created by Administrator on 2018/4/27 0027.
 * 用Actor实现同步/异步消息的发送、接收
 * ! 异步且不要返回值
 * !! 异步且需要返回值
 * !? 同步且等待返回值
 */
class ActorMessage extends Actor{
  override def act(): Unit = {
    while(true){
      //偏函数
      receive{
        case "start" => println("starting...")
        case AsynMsg(id, msg)=>{
          println(s"id:$id, msg:$msg")
          Thread.sleep(1000)
          sender ! ReplyMsg(5, "asyn success")
        }
        case SyncMsg(id, msg)=>{
          println(s"id:$id, msg:$msg")
          Thread.sleep(1000)
          sender ! ReplyMsg(5, "sync success")
        }
      }
    }
  }
}

//异步消息
case class AsynMsg(id: Int, msg: String)
//同步消息
case class SyncMsg(id: Int, msg: String)

case class ReplyMsg(id:Int, msg:String)

object ActorMessage extends App{
  val act = new ActorMessage
  act.start()
  println("发送异步消息前")
  val a = act ! AsynMsg(1,"liyubin")
//  if(a.isSet)   //可以看出 ! 后不会有返回值，也就不会有isSet、apply方法
//    println(a.apply())
  println("发送异步消息后")

  Thread.sleep(3000)
  println("---等待3秒---")

  println("发送异步消息且需要返回值前")
  val future: Future[Any] = act !! AsynMsg(2,"li")
  Thread.sleep(2000)
  //获取异步返回值
  if(future.isSet)
    println(future.apply())
  println("发送异步消息且需要返回值后")

  Thread.sleep(3000)
  println("---等待3秒---")

  println("发送同步消息前")
  act !? SyncMsg(3,"first")
  println("发送同步消息后")

}