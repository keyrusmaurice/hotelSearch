package models
import scala.xml._
import play.api.libs.json._


class GetAvailableDepByMarket(xmlString :String) extends Response(xmlString) { 
  private val depsXml = (xml \\ "MODEL" \\ "DATAS" \\ "dep").toList
  val deps = depsXml.map(tag => Deps( (tag \ "@iata").text, (tag \ "@label").text))   
}

case class Deps(iata : String, label : String) {}

object Deps {
    implicit object MyDepsFormat extends Format[Deps] {
        def writes(o: Deps): JsValue = JsObject(
            List("iata" -> JsString(o.iata),
                "label" -> JsString(o.label)
            )
        )

        def reads(json: JsValue): Deps = Deps(
            (json \ "iata").as[String],
            (json \ "label").as[String]
        )
    }
}

class ZonesResponse(xmlString :String) extends Response(xmlString) { 
  private val depsXml = (xml \\ "MODEL" \\ "DATAS" \\ "zone").toList
  val zones = depsXml.map(tag => Zone( (tag \ "@id").text, (tag \ "@name").text))   
}

case class Zone(id : String, name : String) {}
