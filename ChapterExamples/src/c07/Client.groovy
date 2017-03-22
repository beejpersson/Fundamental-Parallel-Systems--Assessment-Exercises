package c07

// copyright 2012-13 Jon Kerridge
// Let's Do It In Parallel

import org.jcsp.lang.*
import org.jcsp.groovy.*


class Client implements CSProcess{  
	
  def ChannelInput receiveChannel
  def ChannelOutput requestChannel
  def clientNumber   
  def selectList = [ ] 
   
  void run () {
    def iterations = selectList.size
    println "Client $clientNumber has $iterations values in $selectList"
    def passed = true
    def failedClient
    def timesFailed = 0
    for ( i in 0 ..< iterations) {
      def key = selectList[i]
      def desiredValue = 10 * key
      requestChannel.write(key)
      println "Client $clientNumber requested value stored at $key."
      def v = receiveChannel.read()
      if (v != desiredValue) {
        println "Client $clientNumber has recieved the wrong value. $v was returned instead of $desiredValue."
        failedClient = clientNumber
        timesFailed = timesFailed + 1
        passed = false
      } else {
        println "Client $clientNumber recieved the desired value, $desiredValue."
      }
    }
	
    if (passed) {
      println "\nAll values successfully returned to Client $clientNumber."
    } else {
      println "\nClient $failedClient was not successful. It failed $timesFailed time(s)."
    }
    println "\n *** Client $clientNumber has finished *** \n"
  }
}
