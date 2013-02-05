package models
import scala.xml._

class  RequestLogin(username : String, password : String, agentCode : String) extends Request {
  private val xml = <Request>
      <Function>CREATE_SESSION</Function>
      <Credentials UserName={username} Password={password} AgentCode={agentCode} />
    </Request>
  
  override def toString = xml.toString   
}

class  ResponseLogin(xmlString :String) extends Response(xmlString) { 
  private val sess = xml \ "SessionID"
  val sessionID = sess.text
  private val env = xml \ "Environment"
  val environment = env.text
}

class RequestLogOff(sessionID : String) {
  private val xml = <Request>
    <Function>CLOSE_SESSION</Function>
    <SessionID>{sessionID}</SessionID>
    </Request>
  
  override def toString = xml.toString
}

class ResponseLogOff(xmlString :String) extends Response(xmlString) { 
  private val sess = xml \ "SessionStatus"
  val sessionStatus = sess.text
  private val env = xml \ "Environment"
  val environment = env.text
}

