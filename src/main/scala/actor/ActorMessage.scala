package actor

import scala.actors.Actor

/**
 * Created by Administrator on 2018/4/27 0027.
 * 用Actor实现同步/异步消息的发送、接收
 */
class ActorMessage extends Actor{
  override def act(): Unit = {
    while(true){
      //偏函数
      receive{
        case "start" => println("starting...")
        case AsynMsg(id, msg)=>{
          println(s"id:$id, msg:$msg")
          Thread.sleep(2000)
          sender ! ReplyMsg(5, "asyn success")
        }
        case SyncMsg(id, msg)=>{
          println(s"id:$id, msg:$msg")
          Thread.sleep(2000)
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

}