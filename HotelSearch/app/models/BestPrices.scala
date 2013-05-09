package models
import scala.xml._
import Constants._
import java.util.Date

case class Departure(iataDep : String, isoCntyDep : String, txtDepCnty : String, txtDepTwn : String) {}
case class Arrival(iataArr : String, isoCntyArr :String, txtArrCnty : String, txtArrTwn : String)
case class Company(code : String, fileName : String, name :String)
case class Flight(zoneId : Int, arptDep : String, arptDest :String, adultPriceTTC : Float, dateDeparture : Date, dateReturn : Date, hasStopOver : Boolean, haulType :String, refreshDate : Date, tripDuration : Float, companyCode :String)

class BestPricesByCityIata(xmlString :String) extends Response(xmlString) { 
  private val depsXml = (xml \\ "REQUEST" \\ "SEARCH" \\ "DEPARTURES" \\ "DEPARTURE").toList
  val departures = depsXml.map(tag => Departure( (tag \ "@iataDep").text, (tag \ "@isoCntyDep").text,  (tag \ "@txtDepCnty").text,  (tag \ "@txtDepTwn").text))
  private val arrvialsXml = (xml \\ "REQUEST" \\ "SEARCH" \\ "ARRIVALS" \\ "ARRIVAL").toList
  val arrivals = arrvialsXml.map(tag => Arrival( (tag \ "@iataArr").text, (tag \ "@isoCntyArr").text,  (tag \ "@txtArrCnty").text,  (tag \ "@txtArrTwn").text))
  private val zonesXml = (xml \\ "REQUEST" \\ "OFFERS" \\ "ZONE").toList 
  val compagnies :  scala.collection.mutable.Map[String, Company] = scala.collection.mutable.Map.empty[String, Company]
  private val someflightList : scala.collection.mutable.MutableList[Flight] = new scala.collection.mutable.MutableList()

  for (zone <- zonesXml) {
    val zoneId = (zone \ "@zoneid").text.toInt
    val flightsXml = (zone \\ "FLIGHT").toList

    for(flightXml <- flightsXml ) {
      val companyXml = flightXml \\ "COMPAGNY"
      val company = Company( (companyXml \ "@cod").text, (companyXml \ "@fileName").text, (companyXml \ "@name").text )
      compagnies += ((company.code, company))
      val arptDep = (flightXml \ "@ArptDep").text.toUpperCase
      val arptDest =  (flightXml \ "@ArptDest").text.toUpperCase
      val adultPriceTTC = (flightXml \ "@adultPriceTTC").text.replace(",",".").toFloat
      val dateDeparture = dateFormat.parse((flightXml \ "@dteDep").text)
      val dateReturn = dateFormat.parse((flightXml \ "@dteRet").text)
      val hasStopOver =  if ((flightXml \ "@hasStopOver").text == "") false else true
      val haulType = (flightXml \ "@haulType").text
      val refreshDate = refreshDateFormat.parse((flightXml \ "@refreshDate").text)
      val tripDuration = (flightXml \ "@tripDuration").text.toFloat
      val companyCode = company.code
      val flight= Flight(zoneId, arptDep, arptDest, adultPriceTTC, dateDeparture, dateReturn, hasStopOver, haulType, refreshDate, tripDuration, companyCode)
      someflightList += flight
    }   
  }
  var flights = someflightList.toList 
}

object BestPricesByCityIata {
  val zones = Map(1 -> "Afrique/ocean indien", 2 -> "Antilles", 4 -> "Asie", 8 -> "Antartique",16 -> "Amerique centrale",
   32 -> "Europe", 64 -> "Amérique du nord", 128 -> "Océanie", 256 -> "Amérique du sud", 512 -> "Maghreb/Moyen-Orient", 1024 -> "Vols Intérieurs" 
   )
}


