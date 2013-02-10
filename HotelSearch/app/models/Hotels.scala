package models

import scala.xml._
import java.util.Date

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

object HotelBook{
  def fromXml( hotelXml : scala.xml.Node) = {
    val index = (hotelXml \\ "@Index").text
    val serviceReference = ServiceReference.fromXml((hotelXml \\ "ServiceReference").head)  
    val hotelInfo = HotelInfo.fromXml((hotelXml \\ "HotelInfo").head)   
    val roomInfo = RoomInfo.fromXml((hotelXml \\ "RoomInfo").head)
    val cancellationPolicy = (hotelXml \\ "CancellationPolicy").head.text
    val serviceDateRange = ServiceDateRange.fromXml((hotelXml \\ "ServiceDateRange").head)
    val price = Price.fromXml((hotelXml \\ "Price").head)
    val guestCount = GuestCount.fromXml((hotelXml \\ "GuestCount").head)
    val extra = Extra.fromXml((hotelXml \\ "Extra").head)
    val paxAllocation = PaxAllocations.fromXml((hotelXml \\ "PaxAllocation").head)
    val SpecialRequest = (hotelXml \\ "GuestCount").text

    //todo splitoffer
  }
}

object RoomInfo{
  def fromXml (roomInfoXml : scala.xml.Node) = {
    val roomTypeCode = (roomInfoXml \\ "@RoomTypeCode").text
    val roomName = (roomInfoXml \\ "@RoomName").text
    val cancellationAllowed = if ( (roomInfoXml \\ "@CancellationAllowed").text == "" ) false else true
    val units = (roomInfoXml \\ "@Units").text.toInt
    new RoomInfo(roomTypeCode, roomName, cancellationAllowed, units)     
  }
}

class RoomInfo(val roomTypeCode :String,val roomName :String, val cancellationAllowed : Boolean, val units : Int){

}

object HotelInfo{
  def fromXml ( hotelInfoXml : scala.xml.Node) = {
    val hotelCode = (hotelInfoXml \\ "@HotelCode").text
    val hotelName = (hotelInfoXml \\ "@HotelName").text
    val ratingCode = (hotelInfoXml \\ "@RatingCode").text
    val ratingName = (hotelInfoXml \\ "@RatingName").text
    val hotelCityCode = (hotelInfoXml \\ "@HotelCityCode").text
    val cityName = (hotelInfoXml \\ "@CityName").text
    new HotelInfo(hotelCode, hotelName, ratingCode, ratingName, hotelCityCode, cityName)       
  }
}

class HotelInfo(val hotelCode : String, val hotelName  : String, val ratingCode : String ,
                val ratingName : String, val hotelCityCode : String, val cityName : String){
}

object ServiceReference {
  def fromXml(serviceReferenceXml : Node ) = {
    val serviceReferenceNumber=  (serviceReferenceXml \\ "@ServiceReferenceNumber").text
    val serviceStatus = (serviceReferenceXml \\ "@ServiceStatus").text   
    new ServiceReference(serviceReferenceNumber,serviceStatus)
  }
}

class ServiceReference(val serviceReferenceNumber : String , val serviceStatus : String){   
}

object HotelBookList {
   def fromXml( hotelBookListXml : scala.xml.Node) = {
      val hotelBookList = hotelBookListXml \ "Hotel"
      hotelBookList.map(hotelBookListXml => HotelBook.fromXml(hotelBookListXml) )
   }
}
class HotelBookList(hotels: List[HotelBook]) {
  def toXml = {
    <HotelList>{ hotels.map(h => h.toXml) }</HotelList>
  }
}

object ServiceDateRange {
   def fromXml(serviceDateRangeXml : scala.xml.Node) = {
    val checkIn = (serviceDateRangeXml \\ "@CheckIn").text
    val checkOut = (serviceDateRangeXml \\ "@CheckOut").text
    new ServiceDateRange(checkIn, checkOut)
   }  
}

class ServiceDateRange(val checkIn: String, val checkOut: String) {
  def toXml = {
    <ServiceDateRange CheckIn={ checkIn } CheckOut={ checkOut }/>
  }
}

class GuestCount(adults: Int = 1, children: Int = 0) {
  def toXml = {
    <GuestCount Adults={ adults.toString } Children={ children.toString }/>
  }
}

object GuestCount{
  def fromXml(guestCountXml : scala.xml.Node) = {
    val adults = (guestCountXml \\ "@Adults").text
    val children = (guestCountXml \\ "@Children").text
    new GuestCount(adults.toInt, children.toInt)
  }
}

class Extra(val guest: Int, val rollAway: Int, val crib: Int) {
  def toXml = {
    <Extra Guest={ guest.toString } RollAway={ rollAway.toString } Crib={ crib.toString }/>
  }
}

object Extra{
  def fromXml(guestCountXml : scala.xml.Node) = {   
    val guest = (guestCountXml \\ "@Guest").text   
    val rollAway = (guestCountXml \\ "@RollAway").text
    val crib = (guestCountXml \\ "@Crib").text    
    new Extra(guest.toInt, rollAway.toInt, crib.toInt)
  }
}

class Price(val mealPlan: String, val amount: Int,val info : String= "") {
  def toXml = {
    <Price MealPlan={ mealPlan } Amount={ amount.toString }/>
  }
}

object Price {
  def fromXml(priceXml : scala.xml.Node) = {
    val mealPlan = (priceXml \\ "@MealPlan").text
    val amount = (priceXml \\ "@TotalAmount").text
    val info = (priceXml \\ "@Info").text
    new Price(mealPlan, amount.toInt, info)
   } 
}

class PaxAllocations(paxAllocations : List[PaxAllocation]) {
  def toXml = {
    <PaxAllocation>{ paxAllocations.map(h => h.toXml) }</PaxAllocation>
  }
}

object PaxAllocations{
  def fromXml(paxAllocationXml : scala.xml.Node) = {
      val units = (paxAllocationXml \\ "Unit")      
      units.map(x => PaxAllocation.fromXml(x))
  }
}

object PaxAllocation {
   def fromXml(paxAllocationXml : scala.xml.Node) = {
      val unitNo = (paxAllocationXml \\ "@No").text.toInt
      val l = (paxAllocationXml \\ "@PassengerIndex").text
      val passengerIndex = l.split(",").toList.map(_.toInt)
      new PaxAllocation(unitNo,passengerIndex)
   }
}
class PaxAllocation(val unitNo : Int = 1, val passengerIndex : List[Int]){
  def toXml = {
    <Unit No={unitNo.toString} PassengerIndex={passengerIndex.mkString(",")} />
  }
}

class SplitOfferList(splitOfferList : List[SplitOffer2]) {
  def toXml = {
    <SplitOfferList>{splitOfferList.map(x => x.toXml)}</SplitOfferList>
  }
}

class SplitOffer2(code : String, checkIn : String, checkOut : String){
  def toXml ={
    <SplitOffer> <SplitRoom Code={code} CheckIn={checkIn} CheckOut={checkOut} /> </SplitOffer>
  }
}