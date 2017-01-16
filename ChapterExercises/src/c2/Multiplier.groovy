package c2

import org.jcsp.lang.*
 
class Multiplier implements CSProcess {
  
  def ChannelOutput outChannel
  def ChannelInput inChannel
  def int factor
  
  void run() {
    def i = inChannel.read()
    while (i > 0) {
		// write i * factor to outChannel
		outChannel.write(i * factor)
     	// read in the next value of i
		i = inChannel.read()
    }
    outChannel.write(i)
  }
}

    
