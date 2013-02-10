package models

import scala.xml._
import java.util.Date

class TransferToHotel( val index : Int, val amount : Float, val status :String,  val adults : Int,
                val children : Int, val  date : String, val time : String, val pickUpPoint  : String,
                val pickUpPointName : String,  val dropOffPoint : String, val dropOffPointName : String, val transferType : String,
                val transferTypeName : String, val cancellationAllowed : Boolean, val passengerIndices : List[Int]
              ){
}

object TransferToHotel{

  def fromXml( transferXml : scala.xml.Node) = {

    val index = (transferXml \\ "Index").head.text.toInt
    val amount = (transferXml \\ "Amount").head.text.toFloat
    val status = (transferXml \\ "Status").head.text
    val adults = (transferXml \\ "Adults").head.text.toInt
    val children = (transferXml \\ "Index").head.text.toInt
    val date = (transferXml \\ "Date").head.text
    val time = (transferXml \\ "Time").head.text
    val pickUpPoint = (transferXml \\ "PickUpPoint").head.text
    val pickUpPointName = (transferXml \\ "PickUpPointName").head.text
    val dropOffPoint = (transferXml \\ "DropOffPoint").head.text
    val dropOffPointName = (transferXml \\ "DropOffPointName").head.text
    val transferType = (transferXml \\ "TransferType").head.text
    val transferTypeName = (transferXml \\ "TransferTypeName").head.text
    val cancellationAllowed = if( (transferXml \\ "CancellationAllowed").head.text == "Y") true else false
    val x = (transferXml \\ "PassengerIndices").head
    val passengerIndices = (x \\ "PassengerIndex").toList.map( y => (y \\ "PassengerIndex").text.toInt)
    new TransferToHotel( index , amount, status, adults, children,date,time,pickUpPoint,
                pickUpPointName,dropOffPoint, dropOffPointName, transferType, transferTypeName,cancellationAllowed,
                passengerIndices )    
  }
}