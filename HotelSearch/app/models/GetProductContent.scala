package models

import scala.xml._
import java.util.Date

class RequestGetProductContent( sessionID : String, productType : String,
                                productCode : String = "USD", startIndex : Int
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


object TestGetProductContent{
  val x = <Response> <Products> <MoreProducts>FALSE</MoreProducts> <ProductEndIndex>1</ProductEndIndex> <Product> <Code>DXA173</Code> <Type>H</Type> <Address> Dubai Marina P. O. Box 32923, Dubai, UAE </Address> <Phone>+971 4 4367777</Phone> <Latitude>55.14</Latitude> <Longitude>25.08</Longitude> <Description><![CDATA[The Address Dubai Marina Accommodation Code: DXA173 Situated on its own 800-metre, private beach on Dubai’s Palm Island, the 5-star Atlantis offers stunning views of the Arabian Gulf. It provides an underwater aquarium, dolphin-swimming opportunities and an extensive water park. Arabian décor and large beds are fitted in all rooms. The balcony in each offers a panoramic view of the city or the Arabic Sea. Every room is equipped with a flat-screen satellite TV. Fine dining restaurants include Mediterranean, Italian and French. The award-winning Nobu restaurant serves contemporary Japanese food. Atlantis The Palm Dubai includes an underwater labyrinth and the largest water park in the Middle East with near-vertical slides and torrent rivers. The expansive pool is surrounded by lounge chairs and palm trees. A comprehensive spa provides facial and body treatments and the gym provides personal fitness coaches. The Emirates Golf Club is 3 km away and The Dubai Mall is a 20-minute drive away from the Atlantis.]]></Description> <Images> <Image Type="JPG">/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAgGBgcGBAP/9kA</Image> </Images> </Product> </Products> </Response>  
  val y = new ResponseGetProductContent(x.toString) 
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

