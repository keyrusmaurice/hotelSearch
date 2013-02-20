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
       val promise = WS.url(url).post(Map("RequestXML" -> Seq(rha.toString)))
       promise.await(200000)
       promise.value.get.body 
  }


  def getHotelDetailsRequest(rgpc : RequestGetProductContent )  = {             
       val promise = WS.url(url).post(Map("RequestXML" -> Seq(rgpc.toString)))
       promise.await(200000)
       promise.value.get.body 
  }

  def getHotelDetails = Action {
    request =>
      val sessionID = getSession(request)
      val post = request.body.asFormUrlEncoded
      val hotelCode = post.get("hotelCode").head
      val rgpc = new RequestGetProductContent(sessionID, productCode = hotelCode)
      var flag = true
      var result = ""
      do {
        result =  getHotelDetailsRequest(rgpc)
        if (true) flag = false
      }while(flag)
      val resHd = new ResponseGetProductContent(result)
      var imageName = ""
      for(products <- resHd.products;image <- products.images) {
         Utils.generateImage("./public/images/hotels/" + products.code + "." + image.imageType, image.decoded)
         imageName =  products.code + "." + image.imageType 
      }
      Ok(views.html.getHotelDetails(resHd,imageName)).withSession(request.session + ("sessionID" -> sessionID))
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
      val hotelCode = post.get("hotelCode").head
      val rha = new RequestHotelAvailability(sessionID, ci, co, hotelCityCode = hotelCity,adults=adults,children=children,units=units,hotelCode = hotelCode )
      var flag = true
      var result = ""
      do {
        result =  checkhotelavailabilityAction(rha)
        if (true) flag = false
      }while(flag)
     
      val resHa = new ResponseHotelAvailability(result)
      Ok(views.html.getRoomAvailability(resHa)).withSession(request.session + ("sessionID" -> sessionID))
  }

  def viewOnGMap(long : String, lat  : String, name : String) = Action {  request =>
    Ok(views.html.viewOnGMap((long,lat,name)))
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
      //println(result)
      val resHa = new ResponseHotelAvailability(result)
      Ok(views.html.searchHotel(resHa)).withSession(request.session + ("sessionID" -> sessionID))
  }

  def searchHotelForm = Action {
    request =>
      val sessionID = getSession(request)
      Ok(views.html.searchHotelForm(sessionID)).withSession(request.session + ("sessionID" -> sessionID))
  }

  def searchExcursionForm = Action {
    request =>
      val sessionID = getSession(request)
      Ok(views.html.searchExcursionForm(sessionID)).withSession(request.session + ("sessionID" -> sessionID)) 
  }


  def searchExcursion = Action {   
    request =>
      val sessionID = getSession(request)
      val post = request.body.asFormUrlEncoded
      val ci = dateFormat.parse(post.get("checkIN").head)
      val co = dateFormat.parse(post.get("checkOUT").head)
      val hotelCity = post.get("hotelCityCode").head
      val adults = post.get("adults").head.toInt
      val children = post.get("children").head.toInt
      val rea = new RequestCheckExcursionAvailability(sessionID, city = hotelCity,startDate = ci, co, adults=adults,children=children)
      var flag = true
      var result = ""

      do {
        result =  checkExcurionAvailabilityAction(rea)
        println(result)
        if (true) flag = false
      }while(flag)
      //println(result)
      val resEa = new ResponseCheckExcursionAvailability(result)
      Ok(views.html.searchExcursion(resEa)).withSession(request.session + ("sessionID" -> sessionID))
  }

  def checkExcurionAvailabilityAction(rea : RequestCheckExcursionAvailability )  = {             
       println(rea.toString)
       val promise = WS.url(url).post(Map("RequestXML" -> Seq(rea.toString)))
       promise.await(200000)
       promise.value.get.body
  }
  

}
