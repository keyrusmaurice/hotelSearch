package controllers

import play.api._
import play.api.mvc._
import models._
import Constants._
import play.api.libs.ws._

object Application extends Controller {
  
  def index = Action {
    val login = new RequestLogin(UserName, Password,AgentCode)
    val c = WS.url(url).post(login.toString)
    val str = ""
    Ok(views.html.index(str))

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