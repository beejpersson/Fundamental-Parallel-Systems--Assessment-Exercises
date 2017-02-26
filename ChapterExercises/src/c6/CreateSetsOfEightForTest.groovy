package c6

import org.jcsp.lang.*

class CreateSetsOfEightForTest implements CSProcess{

	def ChannelInput inChannel
	def outList = []

	void run(){
		def v = inChannel.read()
		while (v != -1){
			for ( i in 0 .. 7 ) {
				// put v into outList and read next input
				outList[i] = v
				v = inChannel.read()
			}
			println "Eight Object is ${outList}"
		}
		println "Finished"
	}
}