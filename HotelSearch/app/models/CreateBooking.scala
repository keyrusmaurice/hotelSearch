package models

import scala.xml._
import java.util.Date
import TypePassager._
import Salutation._

class  RequestCreateBooking( sessionID : String, clientReference : String, currency : String = "USD",
                             passengerList : PassengerList, hotelBookList : HotelBookList

                           )
{
   private val xml =
    <Request>
     <Function></Function>
      <SessionID>{sessionID}</SessionID>
       <RequestDetails>
        <ActionCode>NEW</ActionCode>
        <BookingNumber></BookingNumber>
        <ClientReference>{clientReference}</ClientReference>
        <Currency>{currency}</Currency>
        {passengerList.toXml}
        {hotelBookList.toXml}        
       </RequestDetails>
    </Request>  
}

class Passenger(index : Int = 1, title : String, salutation : Salutation = Mr,
                firstName : String , lastName  : String,  passengerType : TypePassager = Adult,
                lead : String = "N", age : Either[Int,String]
               ){

  def toXml : Node = {

    val ptype : String= if (passengerType == Adult) "A" else if (passengerType == Child) "C" else "I"    
    val leadAttr = if (lead == "Y") "Lead=\"Y\"" else ""
    
    <Passenger Index={index.toString} Title={title} FirstName={firstName} LastName={lastName} Type={ptype} 
    />
  }

}

class  PassengerList(){
  def toXml : NodeSeq = {
    null
  }  
}

class HotelBook{  
}

class HotelBookList{
  def toXml : NodeSeq = {
    null
  }  
}
