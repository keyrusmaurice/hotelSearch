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
                lead : String = "N", age : String = "" 
               ){

  def toXml : Node = {
    val ptype : String= if (passengerType == Adult) "A" else if (passengerType == Child) "C" else "I"
    var xml = <Passenger Index={index.toString} Title={title} FirstName={firstName} LastName={lastName} Type={ptype} />
    
    if (lead == "Y") xml = xml % Attribute(None, "Lead", Text("Y"), Null)
    if (age != "")  xml = xml % Attribute(None, "Age", Text(age), Null)

    xml    
  }
}

class  PassengerList(passengers : List[Passenger]){
  def toXml : NodeSeq = {
    <PassengerList>{ passengers.map(p => p.toXml) }</PassengerList>
  }  
}


/* PaxAllocation
XML List
XML List of passenger allocation.
SpecialRequest (Optional)
String
Special request. (Maximum 30 characters)
SplitOfferList (Optional)
XML List
List of split offers with date range.
*/

class HotelBook(actionCode :String = "", index : Int, hotelCode : String,
                roomTypeCode : String, mealType : String, units : Int, 
                serviceDateRange : ServiceDateRange,guestCount : GuestCount, extra : Extra,price : Price){
  
  def toXml ={
    <HotelList>
     <Hotel HotelCode="O-DXA173" RoomTypeCode="DLX" Units="2">
      <ServiceDateRange CheckIn="2010-05-25" CheckOut="2010-05-27" />
       <GuestCount Adults="1" Children="0" />
       <Extra Guest="1" RollAway="1" Crib="0" />
       <Price MealPlan="BB" Amount="4500" />
       <PaxAllocation>
        <Unit No="1" PassengerIndex="1,2" />
        <Unit No="2" PassengerIndex="3,4" />
       </PaxAllocation>
       <SpecialRequest>Need a first floor room</SpecialRequest>
    </Hotel>
    </HotelList>
  </RequestDetails>
</Request>
  }


}

class HotelBookList{
  def toXml : NodeSeq = {
    null
  }  
}

//TODO
class ServiceDateRange{

}

//TODO
class Price{

}

//TODO 
class GuestCount{

}

//TODO
class Extra{

}