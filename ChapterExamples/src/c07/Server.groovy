package c07

// copyright 2012-13 Jon Kerridge
// Let's Do It In Parallel

import org.jcsp.lang.*
import org.jcsp.groovy.*


class Server implements CSProcess{
	  
  def ChannelInput clientRequest
  def ChannelOutput clientSend  
  def ChannelOutput thisServerRequest
  def ChannelInput thisServerReceive  
  def ChannelInput otherServerRequest
  def ChannelOutput otherServerSend  
  def dataMap = [ : ]    
  def serverID = ""
                
  void run () {
    def CLIENT = 0
    def OTHER_REQUEST = 1
    def THIS_RECEIVE = 2
    def serverAlt = new ALT ([clientRequest, 
		                      otherServerRequest, 
							  thisServerReceive])
    while (true) {
      def index = serverAlt.select()
	  
      switch (index) {		  
        case CLIENT :
          def key = clientRequest.read()
		  println "client$serverID requested value for key $key from server$serverID"
		  def value = dataMap[key]
          if ( dataMap.containsKey(key) ) {
            clientSend.write(dataMap[key])    
			println "server$serverID sent $value to client$serverID"
		  }
          else {
		    println "server$serverID requested value for key $key from other server"
			thisServerRequest.write(key)
		  } //end if 
          break
        case OTHER_REQUEST :
          def key = otherServerRequest.read()
		  def value = dataMap[key]
          if ( dataMap.containsKey(key) ) {
			otherServerSend.write(dataMap[key])  
			println "server$serverID sent $value back to other server"
		  }  
          else {
            otherServerSend.write(-1)
			println "server$serverID did not have $key "
		  } //end if 
          break
        case THIS_RECEIVE :
		  def value = thisServerReceive.read()
          clientSend.write(value)
		  println "server$serverID sent $value received from other server to client$serverID"
          break
      } // end switch              
    } //end while   
  } //end run
}
