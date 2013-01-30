package models

import scala.xml._

abstract class Response(xmlString : String) {
  protected val xml = XML.loadString(xmlString)
}

case class  ResponseLogin(xmlString :String) extends Response(xmlString) { 
  private val sess = xml \ "SessionID"
  val sessionID = sess.text
  private val env = xml \ "Environment"
  val environment = env.text
}