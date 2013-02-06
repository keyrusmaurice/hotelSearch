package models

import scala.xml._
import java.util.Date

class  RequestCheckExcursionAvailability( sessionID : String, city  : String = "",
                          startDate : Date , endDate : Date ,
                          adults : Int = 1, children : Int = 0 ) extends Request {

  private val startDateStr  = Constants.dateFormat.format(startDate)
  private val endDateStr  = Constants.dateFormat.format(endDate)

  private val adultsStr = adults.toString
  private val childrenStr = /*if (children == 0) "" else */children.toString

  private val xml =
    <Request>
      <Function>CheckExcursionAvailability</Function>
      <SessionID>{sessionID}</SessionID>
      <RequestDetails>
        <StartDate>{startDateStr}</StartDate >
        <EndDate>{endDateStr}</EndDate>
        <City>{city}</City>
        <Adults>{adultsStr}</Adults>
        <Children>{childrenStr}</Children>
      </RequestDetails>
    </Request>
  
  override def toString = xml.toString   
}

class ResponseCheckExcursionAvailability(xmlString : String) extends Response(xmlString) { 
  private val sess = xml \ "SessionID"
  val sessionID = sess.text
  private val env = xml \ "Environment"
  val environment = env.text
  
  private val checkExcursionAvailability = xml \ "CheckExcursionAvailability"
  val date = Constants.dateFormat.parse( (checkExcursionAvailability \ "Date").text )
  private val excusionsXml = (checkExcursionAvailability \ "ExcursionList" \ "Excursion").toList 
  val excusions = excusionsXml.map(new Excusions(_)) 
}

class Excusions(xml : scala.xml.Node) extends XmlResponse(xml){
  val code = (xml \ "Code").text
  val name = (xml \ "Name").text 
  val remarks = (xml \ "Remarks").text
  val currency = (xml \ "Currency").text
  private val availableXml = (xml \ "Availability" \ "Day").toList
  val availabilityXml = if (availableXml == Nil) (xml \ "Day").toList else (xml \ "Availability" \ "Day").toList
  val availability = availabilityXml.map(new Availability(_))
}

class Availability (xml : scala.xml.Node) extends XmlResponse(xml){
  val date = Constants.dateFormat.parse((xml \ "@Date").text)
  val available = (xml \ "Available").text
  val amount = (xml \ "Amount").text.toInt
}
