import models._

object Test3 {
  def main(args: Array[String]): Unit = {
    val test = <Response>
                 <Environment>PRODUCTION</Environment>
                 <SessionID>OIWXZ5OUYU6+I6TAP5NTAW==</SessionID>
                 <CheckExcursionAvailability>
                   <Date>2010-04-15</Date>
                   <ExcursionList>
                     <Excursion>
                       <Code>XDX001</Code>
                       <Name>Sundowner</Name>
                       <Remarks>Please carry original passport</Remarks>
                       <Currency>AED</Currency>
                       <Availability>
                         <Day Date="2010-05-10">
                           <Available>Y</Available>
                           <Amount>100</Amount>
                         </Day>
                         <Day Date="2010-05-11">
                           <Available>N</Available>
                           <Amount>120</Amount>
                         </Day>
                         <Day Date="2010-05-12">
                           <Available>Y</Available>
                           <Amount>130</Amount>
                         </Day>
                       </Availability>
                     </Excursion>
                     <Excursion>
                       <Code>XDX002</Code>
                       <Name>Silk, Spices and Gold</Name>
                       <Remarks>Dress code - smart casual strictly enforced</Remarks>
                       <Currency>AED</Currency>
                       <Day Date="2010-05-10">
                         <Available>Y</Available>
                         <Amount>100</Amount>
                       </Day>
                       <Day Date="2010-05-11">
                         <Available>N</Available>
                         <Amount>120</Amount>
                       </Day>
                       <Day Date="2010-05-12">
                         <Available>Y</Available>
                         <Amount>110</Amount>
                       </Day>
                     </Excursion>
                   </ExcursionList>
                 </CheckExcursionAvailability>
               </Response>

    val cea = new ResponseCheckExcursionAvailability(test.toString)
    val excursions = cea.excusions
    println(cea.date)

    for (e <- excursions) {
      println(e.name)
      for (av <- e.availability) {
        println("\t" + av.date + "\t" + av.available + "\t" + av.amount)
      }
      println()
    }
  }
}
