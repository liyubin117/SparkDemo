package actor;

import scala.actors.{TIMEOUT, Actor, Future}
import scala.collection.mutable.ListBuffer
import scala.collection.parallel.Task
import scala.io.Source

/**
  * 使用线程的方式计算单词出现的频率
  */
case class CountTask(fn:String)
case object StopTask

class WordCountByActorTask extends Actor {
  override def act(): Unit = {
    loop {
      receiveWithin(3000) {
        case CountTask(f) => {
          //通过Source.fromFile(f)读取文件，获取每行并且把它转换成List
          val lines : List[String] = Source.fromFile(f).getLines().toList;
          //将lines list里面的内容合并，然后并且将它split
          val words : List[String] = lines.flatMap(_.split(" "))
          //将word这些单词变成map的并且是元组类型的，每个都是1，----,记着对他们进行过分组，接着就是计算单词
          val result: Map[String, Int]= words.map((_, 1)).groupBy(_._1).mapValues(_.size)
          sender ! result
        }
        case StopTask => println("结束任务");exit()
        case TIMEOUT => println("3秒后退出");exit()
      }
    }
  }
}

object WordCountByActor {
  def main(args: Array[String]): Unit = {
    val replys = new ListBuffer[Future[Any]]
    val results = new ListBuffer[Map[String,Int]]
    val files = Array("file/text1.txt","file/text2.txt")
    //每个文件启动一个actor,并将每个actor处理后得到的结果存储到List中
    for(f <- files) {
      val t = new WordCountByActorTask
      t.start()
      val reply:Future[Any] = t !! CountTask(f)
      //将处理到的结果放到ListBuffer中
      replys += reply

    }

    //对获得的replay的值进行计算。
    while (replys.size > 0) {
      val dones: ListBuffer[Future[Any]] = replys.filter(_.isSet)
      for(f <- dones) {
        //从reply中拿值，并将它转成map
        results += f.apply().asInstanceOf[Map[String, Int]]
        //计算完成之后，将这个reply移除掉
        replys -= f
      }
//      Thread.sleep(500)
    }
    println(results.flatten.sortBy(_._1).groupBy(_._1).mapValues(_.foldLeft(0)(_+_._2)).toList.sortBy(_._1))
  }
}