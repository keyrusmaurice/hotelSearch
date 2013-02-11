package models

import scala.xml._
import java.util.Date
import TypePassager._
import Salutation._

class RequestCreateBooking(sessionID: String, clientReference: String, currency: String = "USD",
  passengerList: PassengerList, hotelBookList: HotelBookList) extends Request {

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

  override def toString = xml.toString 
}

