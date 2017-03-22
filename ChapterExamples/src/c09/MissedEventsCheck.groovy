package c09

import org.jcsp.lang.*
import org.jcsp.groovy.*

class MissedEventsCheck implements CSProcess {

	def ChannelInput inChannel
	def ChannelOutput outChannel

	void run(){

		def previousData = 0
		def count = 0
		def e = new EventData()

		while(true) {
			e = inChannel.read().copy()
			if (count > 0) {
				if (e.data == previousData + 1 + e.missed) {
					println "Number of missed data is correct."
				} else {
					println "Number of missed data is incorrect."
				}
			}

			previousData = e.data
			count = count + 1
			outChannel.write(e)
		}
	}
}