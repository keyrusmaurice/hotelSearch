package models

import org.apache.commons.codec.binary.Base64
import java.io.FileOutputStream

object Utils{

   def generateImage(filename : String, imageDataString : String) = {
      val imageByteArray = decodeImage(imageDataString)
      // Write a image byte array into file system
      val imageOutFile = new FileOutputStream(filename)
      imageOutFile.write(imageByteArray)
      imageOutFile.close
   }

   def decodeImage(imageDataString : String) : Array[Byte] = {
      Base64.decodeBase64(imageDataString.getBytes);
   }
}