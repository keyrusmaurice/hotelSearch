package models

import scala.xml._
import java.util.Date

class  HotelAvailabilityRequest( sessionID : String, checkIn : Date, checkOut : Date,
                          hotelCode  : String = "", hotelName :String="", categoryCode : String = "",
                          hotelCityCode : String = Constants.hotelCityCodes("Dubai"), units : Int = 1, adults : Int = 1, children : Int = 0 ) extends Request {
  private val checkInStr  = Constants.dateFormat.format(checkIn)
  private val checkOutStr = Constants.dateFormat.format(checkOut)
  private val unitsStr = units.toString
  private val adultsStr = adults.toString
  private val childrenStr = if (children == 0) "" else children.toString

  private val xml =
    <Request>
     <Function>CHECK_AVAILABILITY</Function>
      <SessionID>{sessionID}</SessionID>
       <RequestDetails>
        <ServiceDateRange CheckIn={checkInStr} CheckOut={checkOutStr} />
        <HotelInfo HotelCode={hotelCode} HotelName={hotelName} CategoryCode={categoryCode} HotelCityCode={hotelCityCode} Units={unitsStr} />
        <GuestCount Adults={adultsStr} Children={childrenStr} />
       </RequestDetails>
    </Request>
  
  override def toString = xml.toString   
}

class MoreHotels(sessionID : String) extends Request {

  private val xml =
   <Request>
    <Function>CHECK_AVAILABILITY</Function>
    <SessionID>{sessionID}</SessionID>
    <RequestDetails>
      <MoreHotels>TRUE</MoreHotels>
    </RequestDetails>
   </Request>

   override def toString = xml.toString 
}

class ResponseHotelAvailability(xmlString : String) extends Response(xmlString) { 
  private val sess = xml \ "SessionID"
  val sessionID = sess.text
  private val env = xml \ "Environment"
  val environment = env.text
  
  private val checkHotelAvailability = xml \ "CheckHotelAvailability"

  private val serviceDateRange = checkHotelAvailability \ "ServiceDateRange"
  val checkin = (serviceDateRange \\ "@CheckIn").text
  val checkout = (serviceDateRange \\ "@CheckIn").text
  
  val hotelCityCode = (checkHotelAvailability \ "HotelCityCode").text
  val cityName = (checkHotelAvailability \ "CityName").text
  val currency = (checkHotelAvailability \ "Currency").text
  val moreHotels = (checkHotelAvailability \ "MoreHotels").text

  private val hotelsXml = (checkHotelAvailability \ "HotelList" \ "Hotel").toList 
  val hotels : List[Hotel] = hotelsXml.map(new Hotel(_))
}

class Hotel(xml : scala.xml.Node) extends XmlResponse(xml) {
  private val hotelInfo = xml \ "HotelInfo"
  val hotelCode = (hotelInfo \\ "@HotelCode").text
  val hotelName = (hotelInfo \\ "@HotelName").text
  val categoryCode = (hotelInfo \\ "@CategoryCode").text
  val categoryName = (hotelInfo \\ "@CategoryName").text

  val hotelRemarks = (xml \ "HotelRemarks").text
  val cancellationPolicy = (xml \ "CancellationPolicy").text
  val hotelAmenities = (xml \ "HotelAmenities").text
  
  val rateRange = (xml \ "RateRange")
  val minRate = (rateRange \\ "@MinRate").text.toInt  
  val maxRate = (rateRange \\ "@MaxRate").text.toInt
  val descriptionRate = (rateRange \\ "@Description").text

  private val roomListXml = (xml \ "RoomList")
  private val roomsXml = (roomListXml \ "Room").toList
  val rooms = roomsXml.map(new Room(_))
}

class Room(xml : scala.xml.Node) extends XmlResponse(xml) {  

  val code = (xml \\ "@Code").text
  val name = (xml \\ "@Name").text
  val cancellationPolicy = (xml \\ "@CancellationPolicy").text
  val cancellationAllowed = if ((xml \\ "@CancellationAllowed").text.toUpperCase == "N") false else true
  val occupancy = (xml \\ "@Occupancy").text
   
  private val roomPriceList = (xml \ "RoomPriceList" \ "Price").toList
  val priceList = roomPriceList.map(new RoomPriceList(_))

  val extras = (xml \ "Extras").toList.head

  val (roomExtra, roomExtraCharges) = if ((extras \ "Extra").toList != Nil) {
    val roomExtra_ = new RoomExtra((extras \ "Extra").toList.head) 
    val roomExtraCharges_ = new RoomExtraCharges((extras \ "ExtraCharges").toList.head) 
    (roomExtra_, roomExtraCharges_)    
  }
  else{
    (null,null)
  }

  private val splitOfferXml = (xml \ "SplitOfferList" \ "SplitOffer").toList
  val splitOffers : List[SplitOffer] = splitOfferXml.map(new SplitOffer(_))
}

class SplitOffer(xml : scala.xml.Node) extends XmlResponse(xml) {
  private val splitRoom = (xml \ "SplitRoom")
  val code = (splitRoom \ "@Code").text
  val name = (splitRoom \ "@Name").text
  val checkIn = (splitRoom \ "@CheckIn").text
  val checkOut = (splitRoom \ "@CheckOut").text

  private val roomPriceList = (xml \ "RoomPriceList" \ "Price").toList
  val priceList = roomPriceList.map(new RoomPriceList(_))
}

class RoomPriceList(xml : scala.xml.Node) extends XmlResponse(xml) {  
  val mealPlan = (xml \\ "@MealPlan").text
  val ratePerNight = (xml \\ "@RatePerNight").text
  val totalAmount = (xml \\ "@TotalAmount").text.toInt
  val tax = (xml \\ "@Tax").text.toInt
}

class RoomExtra(xml : scala.xml.Node) extends XmlResponse(xml) {  
  val extraPersonsAllowed = (xml  \\ "@ExtraPersonsAllowed").text.toInt
  val crib = (xml \\ "@Crib").text.toInt   
}

class RoomExtraCharges(xml : scala.xml.Node) extends XmlResponse(xml){  
  val extraPerson = (xml  \\ "@ExtraPerson").text.toFloat
  val rollAway = (xml \\ "@RollAway").text.toFloat
  val crib = (xml \\ "@Crib").text.toFloat
}
