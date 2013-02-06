package models

object Constants {
  import java.text.SimpleDateFormat
  val dateFormat = new SimpleDateFormat("yyyy-MM-dd")
  val hotelCityCodes = Map( "Dubai" -> "DXB",  "Abu Dhabi" -> "AUH",  "Sharjah" -> "SHG")

  val ratingCode = Map(
    "001" -> "5* Dubai Beach Hotels", "003" -> "4* Dubai Beach Hotels", "005" -> "5* Dubai City Hotels", "007" -> "4* Dubai City Hotels",
    "009" -> "3* Dubai City Hotels", "011" -> "Abu Dhabi Beach Hotels", "013" -> "Abu Dhabi City Hotels", "015" -> "Sharjah Beach Hotels","017" -> "Sharjah City Hotels",
    "019" -> "Al Ain Hotels", "021" -> "Fujairah Hotels", "023" -> "Ras Al Khaimah Hotels", "025" -> "Hatta Hotels", "027" -> "Ajman Hotels", "029" -> "2* Dubai City Hotels",
    "031" -> "Dubai Marina Apartments", "033" -> "Dubai Desert Resorts", "035" -> "Dubai Standard Apartments", "037" -> "Dubai Deluxe Apartments", "039" -> "Dubai Marina Hotels",
    "041" -> "Abu Dhabi West Coast Hotels", "043" -> "Abu Dhabi Yas Island Hotels", "046" -> "Abu Dhabi Desert Resort Hotels", "100" -> "Muscat Hotels",
    "101" -> "Oman Hotels - Interior Regions", "102" -> "Salalah Hotels", "200" -> "Bahrain Hotels", "300" -> "Doha Hotels")
}

object Salutation extends Enumeration {
    type Salutation = Value
    val Mr, Mrs, Mstr, Miss, Inf = Value
}

object TypePassager extends Enumeration {
    type TypePassager = Value
    val Adult, Child, Infant = Value
}