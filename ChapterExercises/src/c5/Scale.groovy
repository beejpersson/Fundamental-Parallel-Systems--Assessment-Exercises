package c5

import org.jcsp.lang.*
import org.jcsp.groovy.*
import c05.ScaledData

class Scale implements CSProcess {
	def int scaling = 2
	def int multiplier = 2
	def ChannelOutput outChannel
	def ChannelOutput factor
	def ChannelInput inChannel
	def ChannelInput suspend
	def ChannelInput injector

	void run () {
		def SECOND = 1000
		def DOUBLE_INTERVAL = 5 * SECOND
		def SUSPEND  = 0
		def INJECT   = 1
		def TIMER    = 2
		def INPUT    = 3

		def timer = new CSTimer()
		def scaleAlt = new ALT ( [suspend, injector, timer, inChannel ])

		def preCon = new boolean [4]
		preCon[SUSPEND] = true
		preCon[INJECT] = false
		preCon[TIMER] = true
		preCon[INPUT] = true
		def suspended = false

		def timeout = timer.read() + DOUBLE_INTERVAL
		timer.setAlarm ( timeout )

		while (true) {
			switch ( scaleAlt.priSelect(preCon) ) {
				case SUSPEND :
				//  deal with suspend input
					suspend.read()
					factor.write(scaling)
					suspended = true
					println "Suspended"
					preCon[INJECT] = true
					preCon[SUSPEND] = false
					break
				case INJECT:
				//  deal with inject input
					scaling = injector.read()
					println "Injected scaling is $scaling"
					suspended = false
					timeout = timer.read() + DOUBLE_INTERVAL
					timer.setAlarm(timeout)
					preCon[SUSPEND] = true
					preCon[INJECT] = false
					break
				case TIMER:
				//  deal with Timer input
					timeout = timer.read() + DOUBLE_INTERVAL
					timer.setAlarm ( timeout )
					scaling = scaling * multiplier
					println "Normal Timer: new scaling is ${scaling}"
					break
				case INPUT:
				//   deal with Input channel
					def inValue = inChannel.read()
					def result = new ScaledData()
					result.original = inValue
					if (preCon[SUSPEND] == true) {
						result.scaled = inValue * scaling
					}
					else {
						result.scaled = inValue
					}
					outChannel.write ( result )
					break
			} //end-switch
		} //end-while
	} //end-run
}