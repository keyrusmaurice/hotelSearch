package models

abstract class Request()

case class  RequestLogin(username : String, password : String, agentCode : String) extends Request {
	
	override def toString = {
		val xml = <Request>
			<Function>CREATE_SESSION</Function>
			<Credentials UserName={username} Password={password} AgentCode={agentCode} />
		</Request>
	    xml.toString
	}
}


