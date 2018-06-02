package base

/**
 * Created by liyubin on 2018/6/2 0002.
 */
object MyImplicit {

  //用于演示泛型的ViewBound
  implicit val selectGirl = (g:Girl) => new Ordered[Girl]{
   override def compare(that: Girl):Int={
     if(g.faceValue == that.faceValue){
       that.age - g.age
     }else {
       g.faceValue - that.faceValue
     }
   }
  }
}
