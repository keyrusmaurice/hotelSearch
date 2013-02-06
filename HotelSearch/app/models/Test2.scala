import models._

object Test2 {
  def main(args: Array[String]): Unit = {
    val test = <Response>
                 <Environment>PRODUCTION</Environment>
                 <SessionID>OIWXZ5OUYU6+I6TAP5NTAW==</SessionID>
                 <CheckTransferAvailability>
                   <Date>2010-04-15</Date>
                   <PickUpPoint>AJAA01</PickUpPoint><DropOffPoint>DXBAPT</DropOffPoint><Currency>USD</Currency>
                   <TransferList>
                     <Transfer>
                       <Code>SOMPOW</Code>
                       <Name>Private Car/One Way</Name><Amount>100</Amount><Remarks></Remarks>
                     </Transfer>
                     <Transfer>
                       <Code>HELOW</Code>
                       <Name>Helicopter Transfer</Name><Amount>200</Amount><Remarks></Remarks>
                     </Transfer>
                   </TransferList>
                 </CheckTransferAvailability>
               </Response>

    val cta = new ResponseCheckTransferAvailability(test.toString)
    val transferList = cta.transferList
    
    for (t <- transferList) println(t.code)
  }
}
