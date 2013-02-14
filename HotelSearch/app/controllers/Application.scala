package controllers

import play.api._
import play.api.mvc._
import models._
import Constants._
import play.api.libs.ws._
import org.joda.time.DateTime

object Application extends Controller {

  private var newSession : String = ""

  def clear = Action {
    Ok("Bye").withNewSession
  }

  def createHotelSessionID = {
    val login = new RequestLogin(UserName, Password, AgentCode)
    val promise = WS.url(url).post(Map("RequestXML" -> Seq(login.toString)))
    val data = promise.value.get.body
    val sessionID = new ResponseLogin(data).sessionID
    sessionID
  }

  def getSession[A](request: play.api.mvc.Request[A]) = {
    val sessionID = request.session.get("sessionID")
    if (sessionID == None) createHotelSessionID else sessionID.get
  }

  def index = Action {
    request =>
      val sessionID = getSession(request)
      Ok(views.html.index(sessionID)).withSession(request.session + ("sessionID" -> sessionID))
  }

  def checkhotelavailability = Action {
    request =>
      request.session.get("connected").map {
        connected =>
          {
            val ci = dateFormat.parse("2013-02-14")
            val co = dateFormat.parse("2013-02-15")
            val rha = new RequestHotelAvailability(connected, ci, co, hotelCityCode = Constants.hotelCityCodes("Dubai"))
            val promis2 = WS.url(url).post(Map("RequestXML" -> Seq(rha.toString)))
            promis2.await(200000)
            val responseStr2 = promis2.value.get.body
            Ok(views.html.index(responseStr2))
          }
      }.getOrElse {
        Unauthorized("Oops, you are not connected")
      }
  }

  def checkhotelavailabilityAction(rha : RequestHotelAvailability )  = {       
       println(rha)
       val promise = WS.url(url).post(Map("RequestXML" -> Seq(rha.toString)))
       promise.await(200000)
       promise.value.get.body 
  }
 
  def getRoomAvailability = Action {
    request =>
      val sessionID = getSession(request)
      val post = request.body.asFormUrlEncoded
      val ci = dateFormat.parse(post.get("checkIN").head)
      val co = dateFormat.parse(post.get("checkOUT").head)
      val hotelCity = post.get("hotelCityCode").head
      val adults = post.get("adults").head.toInt
      val children = post.get("children").head.toInt
      val units = post.get("units").head.toInt
      val rha = new RequestHotelAvailability(sessionID, ci, co, hotelCityCode = hotelCity,adults=adults,children=children,units=units )
      var flag = true
      var result = ""
      do {
        result =  checkhotelavailabilityAction(rha)
        if (true) flag = false
      }while(flag)
      println(result)
      val resHa = new ResponseHotelAvailability(result)
      Ok(views.html.getRoomAvailability(result)).withSession(request.session + ("sessionID" -> sessionID))
  }

  def searchHotel = Action {   
    request =>
      val sessionID = getSession(request)
      val post = request.body.asFormUrlEncoded
      val ci = dateFormat.parse(post.get("checkIN").head)
      val co = dateFormat.parse(post.get("checkOUT").head)
      val hotelCity = post.get("hotelCityCode").head
      val adults = post.get("adults").head.toInt
      val children = post.get("children").head.toInt
      val units = post.get("units").head.toInt
      val rha = new RequestHotelAvailability(sessionID, ci, co, hotelCityCode = hotelCity,adults=adults,children=children,units=units )
      var flag = true
      var result = ""
      do {
        result =  checkhotelavailabilityAction(rha)
        if (true) flag = false
      }while(flag)
      println(result)
      val resHa = new ResponseHotelAvailability(result)
      Ok(views.html.searchHotel(resHa)).withSession(request.session + ("sessionID" -> sessionID))
  }

  def searchHotelForm = Action {
    request =>
      val sessionID = getSession(request)
      Ok(views.html.searchHotelForm(sessionID)).withSession(request.session + ("sessionID" -> sessionID))
  }

  /*
   dummy code
   val response = new ResponseCreateBooking(r.xml.toString)
    val passengerList = response.passengerList
    val str = passengerList.map(p => (p.firstName + " " + p.lastName)).mkString(",")   
    Ok(views.html.index(str))
    
    //render
    val test = TestGetProductContent.y.products.head.images-jquery-ui.head
    val byteArray = test.decoded  
    Ok(byteArray).as("image/jpeg")
  */

  /*def render2 = Action {
   
  }*/

}
