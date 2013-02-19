package models

import scala.xml._
import java.util.Date

class RequestGetProductContent( sessionID : String, productType : String = "H",
                                productCode : String = "ALL", startIndex : Int = 1
                           ) extends Request {
  private val xml =
    <Request>
      <Function>GET_PRODUCT_CONTENT</Function>
      <SessionID>{ sessionID }</SessionID>
      <RequestDetails>
        <ProductType>{productType}</ProductType>
        <ProductCode>{productCode}</ProductCode>
        <StartIndex>{startIndex}</StartIndex>        
      </RequestDetails>
    </Request>

  override def toString = xml.toString 
}


class ResponseGetProductContent(xmlString :String) extends Response(xmlString) {   
  private val productsTag = xml \ "Products"  
  val moreProducts = (productsTag \ "MoreProducts").text
  val productEndIndex = (productsTag \ "ProductEndIndex").text.toInt
  private val productsXml = (productsTag \ "Product")
  val products = productsXml.map(Product.fromXml(_)) 
}

object Product{
  def fromXml(productXml : scala.xml.Node) = {   
    val code = ((productXml \\ "Code").head).text
    val productType = ((productXml \\ "Type").head).text
    val address = ((productXml \\ "Address").head).text
    val phone = ((productXml \\ "Phone").head).text
    val latitude = ((productXml \\ "Latitude").head).text
    val longitude = ((productXml \\ "Longitude").head).text
    val description = ((productXml \\ "Description").head).text
    val imagesXml = (productXml \\ "Images" \ "Image").toList
    val images = imagesXml.map(Image.fromXml(_))
    new Product(code, productType, address,phone, latitude, longitude, description, images)
  }
}

class Product( val code : String, val productType : String, val address :String, val phone : String,
               val latitude : String, val longitude : String, val description : String,val images : List[Image]){

}

class Image(val imageType : String, val decoded : Array[Byte]){

}

object Image{
  def fromXml(imageXml : scala.xml.Node) = {
    import org.apache.commons.codec.binary.Base64    
    val imageType = (imageXml \\ "@Type").text
    val text64 = imageXml.text
    val decoded = Base64.decodeBase64(text64.getBytes)
    new Image(imageType, decoded)
  }
}

