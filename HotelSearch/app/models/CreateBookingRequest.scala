package models

import scala.xml._
import java.util.Date
import TypePassager._
import Salutation._

class RequestCreateBooking(sessionID: String, clientReference: String, currency: String = "USD",
  passengerList: PassengerList, hotelBookList: HotelBookList) {
  private val xml =
    <Request>
      <Function></Function>
      <SessionID>{ sessionID }</SessionID>
      <RequestDetails>
        <ActionCode>NEW</ActionCode>
        <BookingNumber></BookingNumber>
        <ClientReference>{ clientReference }</ClientReference>
        <Currency>{ currency }</Currency>
        { passengerList.toXml }
        { hotelBookList.toXml }
      </RequestDetails>
    </Request>
}

class Passenger(index: Int = 1, title: String, salutation: Salutation = Mr,
  firstName: String, lastName: String, passengerType: TypePassager = Adult,
  lead: String = "N", age: String = "") {

  def toXml: Node = {
    val ptype: String = if (passengerType == Adult) "A" else if (passengerType == Child) "C" else "I"
    var xml = <Passenger Index={ index.toString } Title={ title } FirstName={ firstName } LastName={ lastName } Type={ ptype }/>

    if (lead == "Y") xml = xml % Attribute(None, "Lead", Text("Y"), Null)
    if (age != "") xml = xml % Attribute(None, "Age", Text(age), Null)

    xml
  }
}

class PassengerList(passengers: List[Passenger]) {
  def toXml: NodeSeq = {
    <PassengerList>{ passengers.map(p => p.toXml) }</PassengerList>
  }
}

class HotelBook(actionCode: String = "", index: Int, hotelCode: String,
  roomTypeCode: String, mealType: String, units: Int, serviceDateRange: ServiceDateRange,
  guestCount: GuestCount, extra: Extra, price: Price, paxAllocation: PaxAllocation,
  specialRequest: String,splitOfferList : SplitOfferList) {

  def toXml = {
    <Hotel HotelCode={ hotelCode } RoomTypeCode={ roomTypeCode } Units={ units.toString }>
      { serviceDateRange.toXml }
      { guestCount.toXml }
      { extra.toXml }
      { price.toXml }
      { paxAllocation.toXml }
      { if (specialRequest != "") <SpecialRequest>{ specialRequest }</SpecialRequest> }
      { splitOfferList.toXml }
    </Hotel>
  }
}

class HotelBookList(hotels: List[HotelBook]) {
  def toXml = {
    <HotelList>{ hotels.map(h => h.toXml) }</HotelList>
  }
}

class ServiceDateRange(checkIn: String, checkOut: String) {
  def toXml = {
    <ServiceDateRange CheckIn={ checkIn } CheckOut={ checkOut }/>
  }
}

class GuestCount(adults: Int = 1, children: Int = 0) {
  def toXml = {
    <GuestCount Adults={ adults.toString } Children={ children.toString }/>
  }
}

class Extra(guest: Int, rollAway: Int, crib: Int) {
  def toXml = {
    <Extra Guest={ guest.toString } RollAway={ rollAway.toString } Crib={ crib.toString }/>
  }
}

class Price(mealPlan: String, amount: Int) {
  def toXml = {
    <Price MealPlan={ mealPlan } Amount={ amount.toString }/>
  }
}

class PaxAllocations(paxAllocations : List[PaxAllocation]) {
  def toXml = {
    <PaxAllocation>{ paxAllocations.map(h => h.toXml) }</PaxAllocation>
  }
}

class PaxAllocation(unitNo : Int = 1, passengerIndex : List[Int]){
  def toXml = {
    <Unit No={unitNo.toString} PassengerIndex={passengerIndex.mkString(",")} />
  }
}

class SplitOfferList(splitOfferList : List[SplitOffer]) {
  def toXml = {
    <SplitOfferList>{splitOfferList.map(x => x.toXml)}</SplitOfferList>
  }
}

class SplitOffer(code : String, checkIn : String, checkOut : String){
  def toXml ={
    <SplitOffer> <SplitRoom Code={code} CheckIn={checkIn} CheckOut={checkOut} /> </SplitOffer>
  }
}