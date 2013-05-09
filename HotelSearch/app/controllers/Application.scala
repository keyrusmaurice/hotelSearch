package controllers

import play.api._
import play.api.mvc._
import models._
import Constants._
import play.api.libs.ws._
import org.joda.time.DateTime

object Application extends Controller {
 
  def getWebserviceGetResult(url : String) : String = {
    val promise =  WS.url(url).get     
    promise.await(200000)
    promise.value.get.body
  }

  def getWebServicePostResult(url : String, map : Map[String,Seq[String]]) : String = {
    val promise =  WS.url(url).post(map)     
    promise.await(200000)
    promise.value.get.body
  }

  def getWsdl = Action {
    request =>    
      val wsdl = getWebserviceGetResult(goVoyagesBest + "?op=GetBestPrices")
      println(wsdl)
      Ok(views.html.getWsdl(wsdl))
  }

  def byMarket = Action {
    request =>
      val sessionID =  getSession(request)      
      Ok(views.html.byMarket(sessionID)).withSession(request.session + ("sessionID" -> sessionID))
  }

  def getAvailableDep= Action {
    request =>
      //initialise data
      val post = request.body.asFormUrlEncoded
      val dateFrom = post.get("dateFrom").head
      val dateTo = post.get("dateTo").head
      val marketCode = post.get("marketCode").head    
      val lang = "fr"
      val map = Map("dteFrom" -> Seq(dateFrom), "dateTo" -> Seq(dateTo), "marketCode" -> Seq(marketCode), "lng" -> Seq(lang))
      
      // we do the request here
      val xmlString = getWebServicePostResult(goVoyagesModel + "/GetAvailableDepByMarket", map)
      val deps = new GetAvailableDepByMarket(xmlString).deps      
      Ok(views.html.getAvailableDep(deps))
  }

  def getAvailableDepByMarket= Action {
    request =>
      //initialise data
      val post = request.body.asFormUrlEncoded
      val dateFrom = post.get("dateFrom").head
      val dateTo = post.get("dateTo").head
      val marketCode = post.get("marketCode").head    
      val lang = "fr"
      val map = Map("dteFrom" -> Seq(dateFrom), "dateTo" -> Seq(dateTo), "marketCode" -> Seq(marketCode), "lng" -> Seq(lang))
      
      // we do the request here
      val xmlString = getWebServicePostResult(goVoyagesModel + "/GetAvailableDepByMarket", map)
      //println(xmlString)
      val deps = new GetAvailableDepByMarket(xmlString).deps      
      Ok(views.html.getAvailableDepByMarket(deps))
  }

  def getPopulatedZonesForIata = Action {
    request =>
      //initialise data
      val post = request.body.asFormUrlEncoded
      val dateFrom = swapMonth(post.get("dateFrom").head)
      val dateTo = swapMonth(post.get("dateTo").head)    
      val label = post.get("label").head    
      val lang = "fr"
      val map = Map("dteFrom" -> Seq(dateFrom), "dteTo" -> Seq(dateTo), "iataDep" -> Seq(label), "lng" -> Seq(lang))
          
      // we do the request here
      val xmlString =  getWebServicePostResult(goVoyagesModel + "/GetPopulatedZonesForIata", map)
      println(xmlString)
      val zones = new ZonesResponse(xmlString).zones      
      Ok(views.html.getPopulatedZonesForIata(zones))
  }

  def getBestPricesByCityIata = Action {
    request =>
    val post = request.body.asFormUrlEncoded
    val label = post.get("label").head   
    val lang = "fr"
    val map = Map("iata" -> Seq(label), "lng" -> Seq(lang))
    // we do the request here
    val xmlString =  getWebServicePostResult(goVoyagesBest + "/GetBestPricesByCityIata",map)     
    val bestPrices = new BestPricesByCityIata(xmlString)

    if (post.get.get("filterzone") != None ){
        val filterbyZone = post.get.get("filterzone").get.head.toInt
        println(filterbyZone)
        if(filterbyZone != -1) bestPrices.flights = bestPrices.flights.filter( _.zoneId == filterbyZone)
    }

    if(post.get.get("filterDes") != None ){
       val filterDes = post.get.get("filterDes").get.head
       println(filterDes)
       if(filterDes != "") bestPrices.flights = bestPrices.flights.filter( _.arptDest == filterDes)
    }

    val groups = bestPrices.flights.groupBy(_.zoneId)   
    Ok(views.html.getBestPricesByCityIata(groups, bestPrices))
  }

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
    var sessionID = if (request.session.get("sessionID") == None) "" else request.session.get("sessionID").get
    //val sessionID = request.session.get("sessionID")
    if (sessionID == "") createHotelSessionID else sessionID
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
      //val post = request.body.asFormUrlEncoded
      val hotelCode = request.queryString ("hotelCode").head
      val rgpc = new RequestGetProductContent(sessionID, productCode = hotelCode)
      var flag = true
      var result = ""
      do {
        result =  getHotelDetailsRequest(rgpc)
        if (true) flag = false
      }while(flag)
      //println(result)
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
      println(result)

      chooseViewOutput(result,
       { val resHa = new ResponseHotelAvailability(result)
         Ok(views.html.searchHotel(resHa)).withSession(request.session + ("sessionID" -> sessionID))
       })      
  }

  def searchHotelForm = Action {
    request =>      
      val sessionID = getSession(request)      
      Ok(views.html.searchHotelForm(sessionID)).withSession(request.session + ("sessionID" -> sessionID))
  }

  def searchHotelFormGoVoyage = Action {
    request =>      
      val sessionID = getSession(request)      
      Ok(views.html.searchHotelFormGoVoyage(sessionID)).withSession(request.session + ("sessionID" -> sessionID))
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
        //println(result)
        if (true) flag = false
      }while(flag)
      //println(result)

       chooseViewOutput(result,
       { val resEa = new ResponseCheckExcursionAvailability(result)
        Ok(views.html.searchExcursion(resEa)).withSession(request.session + ("sessionID" -> sessionID))
       })
  }

  def checkExcurionAvailabilityAction(rea : RequestCheckExcursionAvailability )  = {             
       println(rea.toString)
       val promise = WS.url(url).post(Map("RequestXML" -> Seq(rea.toString)))
       promise.await(200000)
       promise.value.get.body
  }
  

}
