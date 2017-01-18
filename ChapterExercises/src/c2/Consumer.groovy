package c2

import org.jcsp.lang.*

class Consumer implements CSProcess {

	def ChannelInput inChannel

	void run() {
		def i = inChannel.read()
		while (i > 0) {
			i = inChannel.read()
			// insert a modified println statement
			println "the result was: ${i}"
		}
		println "Finished"
	}
}

