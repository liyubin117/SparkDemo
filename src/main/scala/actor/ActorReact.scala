package actor

import scala.actors.Actor
import scala.actors.Actor._

/**
 * Created by liyubin on 2018/5/17 0017.
 */
object ActorReact extends App{
  def react_create={
    loop{
      react{
        case "Hello" => println(Thread.currentThread().getId+"very good"); sender ! Thread.currentThread().getId
        case "Hi" => println("good")
        case "other" => println("other")
      }
    }
  }
  def receive_create={
    loop{
      receive{
        case "Hello" => println(Thread.currentThread().getId+"very good"); sender ! Thread.currentThread().getId
        case "Hi" => println("good")
        case "other" => println("other")
      }
    }
  }

  var listActor:List[Actor] = List.empty
  var setThreadId:Set[Any] = Set.empty

  for(i<-1 to 100){
    listActor = listActor :+ actor(react_create) //使用react处理消息，多个actor能复用同一个线程
//    listActor = listActor :+ actor(receive_create) //使用receive处理消息
  }

  for(i<-0 until 100){
    val res = listActor(i) !! "Hello"
    var flag=true
    while(flag){
      if(res.isSet){
        setThreadId += res.apply()
        flag=false
      }
    }
  }

  Thread.sleep(2000)
  println("线程id有："+setThreadId)

}
