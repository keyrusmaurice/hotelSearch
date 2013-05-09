import play.api._

object Global extends GlobalSettings {
  
  var destinations : Map[String, String] = Map()

  override def onStart(app: Application) {
    destinations = InitialData.load()
  }  
}

object InitialData { 
  import scala.io.Source._
  val map = scala.collection.mutable.Map.empty[String,String]
  def load() : Map[String,String] = {   
    val lines = fromFile("./public/VillesIATA.csv").getLines.toList
    for(line <- lines) {
      val tokens = line.split(",")
      map += ((tokens(0),tokens(1)))
    }
    map.toMap[String,String]
  }  
}

package object globals {
  val destinations = Global.destinations
}