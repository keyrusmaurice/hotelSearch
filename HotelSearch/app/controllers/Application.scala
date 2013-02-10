package controllers

import play.api._
import play.api.mvc._
import models._

object Application extends Controller {
  
  def index = Action {
    val response = new ResponseCreateBooking(r.xml.toString)
    val passengerList = response.passengerList
    val str = passengerList.map(p => (p.firstName + " " + p.lastName)).mkString(",") 
    Ok(views.html.index(str))
  }
  
}