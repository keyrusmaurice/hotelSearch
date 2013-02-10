package models

import scala.xml._
import java.util.Date
import TypePassager._
import Salutation._

object r {
  val xml = <Response>
              <Environment>TEST</Environment><SessionID>oiwxz5ouYu6+I6tap5Ntaw==</SessionID>
              <Booking>
                <ClientReference>BKG001</ClientReference>
                <BookingNumber>1255488</BookingNumber><BookingDate>26-Jan-2012</BookingDate>
                <BookingAmount Amount="4500" Currency="USD"/>
                <BookingStatus>Complete</BookingStatus>
                <PassengerList>
                  <Passenger Index="1" Title="Mr" FirstName="Paul" LastName="Smith" Type="A" Lead="Y" Age="33"/>
                  <Passenger Index="2" Title="Mrs" FirstName="Kate" LastName="Smith" Type="A" Age="30"/>
                  <Passenger Index="3" Title="Mstr" FirstName="John" LastName="Smith" Type="C" Age="12"/>
                  <Passenger Index="4" Title="Inf" FirstName="Kathy" LastName="Smith" Type="I" Age="1"/>
                </PassengerList>
                <HotelList>
                  <Hotel Index="1">
                    <ServiceReference ServiceReferenceNumber="100201" ServiceStatus="Confirmed"/>
                    <HotelInfo HotelCode="O-DXA173" HotelName="Atlantis - The Palm Dubai" RatingCode="005" RatingName=" 5* Dubai City Hotels " HotelCityCode="DXB" CityName="Dubai"/>
                    <RoomInfo RoomTypeCode="DLX" RoomName="Deluxe Room" CancellationAllowed="Y" Units="2"/>
                    <CancellationPolicy>Cancel 10 - 20 days prior to arrival : 80% will be charged. || Cancel 21 - 30 days prior to arrival : 4night stay will be charged. || Cancel 31 - 40 days prior to arrival : 30% will be charged.</CancellationPolicy><ServiceDateRange CheckIn="2010-05-25" CheckOut="2010-05-27"/>
                    <Price MealPlan="BB" TotalAmount="4500" Info="Bed and Breakfast"/>
                    <GuestCount Adults="2" Children="0"/><Extra Guest="1" RollAway="1" Crib="0"/>
                    <PaxAllocation> <Unit No="1" PassengerIndex="1,2"/> <Unit No="2" PassengerIndex="3,4"/> </PaxAllocation>
                    <SpecialRequest>Want a first floor room</SpecialRequest>
                  </Hotel>
                </HotelList>
                <TransferList>
                  <Transfer>
                    <Index>2</Index><Amount>100.00</Amount><Status>I</Status><Adults>1</Adults><Children>0</Children><Date>2010-05-25</Date><Time>2330</Time><PickUpPoint>DXBAPT</PickUpPoint><PickUpPointName>Dubai Airport</PickUpPointName><DropOffPoint>DXA173</DropOffPoint><DropOffPointName>Atlantis - The Palm Dubai</DropOffPointName>
                    <TransferType>SOMPOW</TransferType><TransferTypeName>Private Car/One Way</TransferTypeName>
                    <CancellationAllowed>Y</CancellationAllowed>
                    <PassengerIndices> <PassengerIndex>1</PassengerIndex> </PassengerIndices>
                    <Remarks></Remarks>
                  </Transfer>
                </TransferList>
                <ExcursionList> <Excursion>
                                  <Index>1</Index><Amount>100.00</Amount><Status>I</Status>
                                  <Adults>1</Adults><Children>0</Children><Date>2010-04-18</Date>
                                  <PickUpPoint>DXA173</PickUpPoint>
                                  <PickUpPointName>Atlantis - The Palm Dubai</PickUpPointName>
                                  <PickUpTime>1030</PickUpTime>
                                  <DropOffPoint>DXA173</DropOffPoint>
                                  <DropOffPointName>Atlantis - The Palm Dubai</DropOffPointName><DropOffTime>2330</DropOffTime>
                                  <ExcursionCode>XDX001</ExcursionCode><ExcursionName>Sundowner</ExcursionName>
                                  <CancellationAllowed>Y</CancellationAllowed><PassengerIndices>
                                                                              </PassengerIndices>
                                  <Remarks></Remarks>
                                </Excursion> </ExcursionList>
                <FlightList>
                  <Flight>
                    <Index>1</Index><Status>I</Status><Class>E</Class><Adults>2</Adults><Children>0</Children>
                    <Date>2010-05-25</Date>
                    <Time>1530</Time><DepartureAirport>LHR</DepartureAirport>
                    <DepartureAirportName>LONDON HEATHROW</DepartureAirportName>
                    <ArrivalAirport>DXB</ArrivalAirport><ArrivalAirportName>DUBAI - U.A.E. - T3</ArrivalAirportName>
                    <FlightNo>EK001</FlightNo><CancellationAllowed>Y</CancellationAllowed>
                    <PassengerIndices>
                      <PassengerIndex>1</PassengerIndex>
                      <PassengerIndex>2</PassengerIndex>
                    </PassengerIndices>
                  </Flight>
                </FlightList>
                <CarHireList>
                  <CarHire>
                    <Index>1</Index><Amount>100.00</Amount>
                    <Status>I</Status><Adults>1</Adults><Children>0</Children><StartDate>2010-04-16</StartDate>
                    <EndDate>2010-04-19</EndDate><PickUpPoint>DXBAPT</PickUpPoint>
                    <PickUpPointName>Dubai Airport</PickUpPointName><PickUpTime>1030</PickUpTime>
                    <DropOffPoint>DXBAPT</DropOffPoint><DropOffPointName>Dubai Airport</DropOffPointName>
                    <DropOffTime>2330</DropOffTime><CarCode>ACP</CarCode><CarName>GROUP A+CDW+PAI Car</CarName>
                    <CancellationAllowed>Y</CancellationAllowed>
                    <PassengerIndices>
                      <PassengerIndex>1</PassengerIndex>
                    </PassengerIndices><Remarks></Remarks>
                  </CarHire>
                </CarHireList>
              </Booking>
            </Response>

}

class ResponseCreateBooking(xmlString: String) extends Response(xmlString) {
  private val sess = xml \ "SessionID"
  val sessionID = sess.text
  private val env = xml \ "Environment"
  val environment = env.text

  val booking = xml \ "Booking"
  val clientReference = (booking \ "ClientReference").text
  val bookingNumber = (booking \ "BookingNumber").text
  val bookingDate = (booking \ "BookingDate").text
  val bookingAmount = (booking \ "BookingAmount" \ "@Amount")
  val bookingCurrency = (booking \ "BookingAmount" \ "@Currency")
  val bookingStatus = (booking \ "BookingmStatus").text
  val passengerList = PassengerList.fromXml((booking \\ "PassengerList").head)
}
