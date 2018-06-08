package base

/**
 * Created by liyubin on 2018/6/2 0002.
 */
object MyPredef {

//用于演示泛型的ViewBound
//  implicit val selectGirl = (g:Girl) => new Ordered[Girl]{  //隐式方法
//   override def compare(that: Girl):Int={
//     if(g.faceValue == that.faceValue){
//       that.age - g.age
//     }else {
//       g.faceValue - that.faceValue
//     }
//   }
//  }

  implicit def selectGirl(g:Girl):Ordered[Girl]={  //隐式函数
    new Ordered[Girl]{
      override def compare(that: Girl):Int={
        if(g.faceValue == that.faceValue){
          that.age - g.age
        }else {
          g.faceValue - that.faceValue
        }
      }
    }
  }

//  implicit object selectGirl2 extends Ordering[Girl]{  //隐式对象
//      override def compare(g:Girl, that: Girl):Int={
//        if(g.faceValue == that.faceValue){
//          that.age - g.age
//        }else {
//          g.faceValue - that.faceValue
//        }
//      }
//  }

  implicit val selectGirl3 = new Ordering[Girl]{  //隐式值
    override def compare(g:Girl, that: Girl):Int={
      if(g.faceValue == that.faceValue){
        that.age - g.age
      }else {
        g.faceValue - that.faceValue
      }
    }
  }
}
