import models._

object Test{
	def main(args : Array[String]) : Unit = {
	/*	val test = 
<Response>
 <Environment>PRODUCTION</Environment>
 <SessionID>OIWXZ5OUYU6+I6TAP5NTAW==</SessionID>
 <CheckHotelAvailability>
  <ServiceDateRange CheckIn="2012-04-12" CheckOut="2012-04-15" />
   <HotelCityCode>DXB</HotelCityCode>
   <CityName>Dubai</CityName>
   <Currency>USD</Currency>
    <MoreHotels>TRUE</MoreHotels>
     <HotelList>
      <Hotel>
       <HotelInfo HotelCode="O-DXAA05" HotelName="JUMEIRAH BEACH HOTEL" CategoryCode="5" CategoryName="5* Hotels" />
        <HotelRemarks>CHECK IN 1200 CHECK OUT 1400</HotelRemarks>
        <CancellationPolicy>CANCEL BEFORE 1 DAY OF ARRIVAL ELSE 1 DAY PENALTY</CancellationPolicy>
         <HotelAmenities>GOLF;POOL;HIGH SPEED INTERNET;</HotelAmenities>
          <RateRange MinRate="500" MaxRate="6000" Description="Contracted Rate" />
           <RoomList>
            <Room Code="B2ARCC" Name="DELUXE ROOM/KING BED" CancellationPolicy="Cancel 3 days prior to arrival" CancellationAllowed="N" Occupancy="2A/1C;2A/2C;">
             <RoomPriceList>
              <Price MealPlan="BB" RatePerNight="2200" TotalAmount="4500" Tax="100" Info="Bed and Breakfast" />
              <Price MealPlan="HB" RatePerNight="2500" TotalAmount="5200" Tax="200" Info="Half Board" />
             </RoomPriceList>
              <Extras>
               <Extra ExtraPersonsAllowed="1" Crib="0" />
               <ExtraCharges ExtraPerson="10.00" RollAway="10.00" Crib="10.00" />
              </Extras>
            </Room>
           </RoomList>
      </Hotel>
    </HotelList>
  </CheckHotelAvailability>
</Response>*/

  val test = <Response> <Environment>PRODUCTION</Environment> <SessionID>OIWXZ5OUYU6+I6TAP5NTAW==</SessionID> <CheckHotelAvailability> <ServiceDateRange CheckIn="2012-04-12" CheckOut="2012-04-15" /> <HotelCityCode>DXB</HotelCityCode> <CityName>Dubai</CityName> <Currency>USD</Currency> <MoreHotels>TRUE</MoreHotels> <HotelList> <Hotel> <HotelInfo HotelCode="O-DXAA82" HotelName="Towers Rotana Hotel" CategoryCode="5" CategoryName="5 star beach hotel" /> <HotelRemarks>CHECK IN 1200 CHECK OUT 1400</HotelRemarks> <CancellationPolicy>CANCEL BEFORE 1 DAY OF ARRIVAL ELSE 1 DAY PENALTY</CancellationPolicy> <HotelAmenities /> <RateRange MinRate="500" MaxRate="6000" Description="Contracted Rate" /> <RoomList> <Room Code="DLX" Name="DELUXE ROOM" CancellationAllowed="N" Occupancy="2A/1C;2A/2C;"> <RoomPriceList> <Price MealPlan="BB" RatePerNight="2200" TotalAmount="4500" Tax="0" Info="Bed and Breakfast" /> <Price MealPlan="HB" RatePerNight="2500" TotalAmount="5200" Tax="0" Info="Half Board" /> </RoomPriceList> <Extras /> <SplitOfferList> <SplitOffer>
    <SplitRoom Code="DLX1" Name="Sub room on DLX" CheckIn="2010-04-10" CheckOut="2010-04-12" /> <RoomPriceList> <Price MealPlan="BB" RatePerNight="1000" TotalAmount="2000" Tax="0" Info="Bed and Breakfast" /> <Price MealPlan="HB" RatePerNight="1500" TotalAmount="2100" Tax="0" Info="Half Board" /> </RoomPriceList> </SplitOffer> <SplitOffer> <SplitRoom Code="DLX" Name="DELUX Room" CheckIn="2010-04-12" CheckOut="2010-04-15" /> <RoomPriceList> <Price MealPlan="BB" RatePerNight="1000" TotalAmount="2500" Tax="0" Info="Bed and Breakfast" /> <Price MealPlan="HB" RatePerNight="1500" TotalAmount="3100" Tax="0" Info="Half Board" /> </RoomPriceList> </SplitOffer> </SplitOfferList> </Room> </RoomList> </Hotel> </HotelList> </CheckHotelAvailability> </Response>
  
    val ca = new ResponseHotelAvailability(test.toString)
    val hotels = ca.hotels    
    for(hotel <- hotels;room <- hotel.rooms) { room.splitOffers.foreach(x=>println(x.code)) }
	}
}