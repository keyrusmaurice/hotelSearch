package controllers

import play.api._
import play.api.mvc._
import models._
import Constants._
import play.api.libs.ws._

object Application extends Controller {
  
  def clear = Action{
    Ok("Bye").withNewSession
  }
  
  def getSession = {
     val login = new RequestLogin(UserName, Password,AgentCode)    
      val promis = WS.url(url).post(Map("RequestXML" -> Seq(login.toString)))    
      val responseStr = promis.value.get.body
      val newSession = new ResponseLogin(responseStr)
      newSession.sessionID 
  }

  def index = Action {
    request =>   
    val sessionSearch = request.session.get("connected")
    val sessionValue = if (sessionSearch == None) getSession else sessionSearch.get     
    
    val str = sessionValue
    Ok(views.html.index(str)).withSession("connected" -> sessionValue)
  }
  
  def checkhotelavailability = Action{
    request =>   
    request.session.get("connected").map {
        connected => {          
      val ci = dateFormat.parse("2013-02-14")
      val co = dateFormat.parse("2013-02-15")
      val rha = new RequestHotelAvailability(connected,ci,co,hotelCityCode = Constants.hotelCityCodes("Dubai"))
      println(rha)      
      val promis2 = WS.url(url).post(Map("RequestXML" -> Seq(rha.toString)))
      promis2.await(10000)          
      val responseStr2 = promis2.value.get.body     
      Ok(views.html.index(responseStr2))      
    }}.getOrElse {
    Unauthorized("Oops, you are not connected")   
   } 
 }
  /*
   dummy code
   val response = new ResponseCreateBooking(r.xml.toString)
    val passengerList = response.passengerList
    val str = passengerList.map(p => (p.firstName + " " + p.lastName)).mkString(",")   
    Ok(views.html.index(str))
    
    //render
    val test = TestGetProductContent.y.products.head.images.head
    val byteArray = test.decoded  
    Ok(byteArray).as("image/jpeg")
  */

  /*def render2 = Action {
   
  }*/
  
}
