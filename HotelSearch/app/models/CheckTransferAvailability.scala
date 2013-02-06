package models

import scala.xml._
import java.util.Date

class  RequestCheckTransferAvailability( sessionID : String, date : Date, 
                          pickUpPoint  : String = "", dropOffPoint :String="", 
                          adults : Int = 1, children : Int = 0 ) extends Request {
  private val dateStr  = Constants.dateFormat.format(date) 
  private val adultsStr = adults.toString
  private val childrenStr = /*if (children == 0) "" else */children.toString

  private val xml =
    <Request>
     <Function>CheckTransferAvailability</Function>
      <SessionID>{sessionID}</SessionID>
       <RequestDetails>
	      <Date>{dateStr}</Date>
	      <PickUpPoint>{pickUpPoint}</PickUpPoint>
	      <DropOffPoint>{dropOffPoint}</DropOffPoint>
	      <Adults>{adultsStr}</Adults>
	      <Children>{childrenStr}</Children>        
       </RequestDetails>
    </Request>
  
  override def toString = xml.toString   
}

class ResponseCheckTransferAvailability(xmlString : String) extends Response(xmlString) { 
  private val sess = xml \ "SessionID"
  val sessionID = sess.text
  private val env = xml \ "Environment"
  val environment = env.text
  
  private val checkTransferAvailability = xml \ "CheckTransferAvailability"

  val date = Constants.dateFormat.parse( (checkTransferAvailability \ "Date").text)
  val pickUpPoint = (checkTransferAvailability \ "PickUpPoint").text  
  val dropOffPoint = (checkTransferAvailability \ "DropOffPoint").text
  val currency = (checkTransferAvailability \ "Currency").text

  private val transferListXml = (checkTransferAvailability \ "TransferList" \ "Transfer").toList 
  val transferList : List[Transfer] = transferListXml.map(new Transfer(_))
}

class Transfer(xml : scala.xml.Node) extends XmlResponse(xml){
  val code = (xml \ "Code").text
  val name = (xml \ "Name").text
  val amount = (xml \ "Amount").text.toInt
  val remarks = (xml \ "Remarks").text
}