package models

import scala.xml._

abstract class Response(xmlString : String) {
  protected val xml = XML.loadString(xmlString)
}

abstract class XmlResponse(xml : scala.xml.Node){}

