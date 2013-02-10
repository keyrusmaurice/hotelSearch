package models

import scala.xml._
import java.util.Date
import TypePassager._
import Salutation._

object Passenger{
  def fromXml( passengerXml : scala.xml.Node) = {
    val index = (passengerXml \\ "@Index").text   
    val title = Salutation.withName((passengerXml \\ "@Title").text)   
    val firstName = (passengerXml \\ "@FirstName").text   
    val lastName = (passengerXml \\ "@LastName").text   
    val ptype = (passengerXml \\ "@Type").text   
    val passengerType = if (ptype == "A") Adult else if (ptype == "C") Child else Infant  
    val lead = (passengerXml \\ "@Lead").text   
    val age = (passengerXml \\ "@Age").text   
    val passenger = new Passenger(index.toInt, title,firstName, lastName,
                        passengerType, if (lead == "") "N" else lead , age)
    passenger
  }
}

object PassengerList{
  def fromXml( passengerXml : scala.xml.Node) = {
    val passengerList = passengerXml \ "Passenger"
    passengerList.map(passengerXml => Passenger.fromXml(passengerXml) )
  }
}

class Passenger(val index: Int = 1, val title: Salutation = Mr,
  val firstName: String, val lastName: String, val passengerType: TypePassager = Adult,
  val lead: String = "N", val age: String = "") {

  def toXml: Node = {
    val ptype: String = if (passengerType == Adult) "A" else if (passengerType == Child) "C" else "I"
    var xml = <Passenger Index={ index.toString } Title={ title.toString } FirstName={ firstName } LastName={ lastName } Type={ ptype }/>

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