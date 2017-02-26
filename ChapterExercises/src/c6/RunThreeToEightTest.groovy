package c6

import org.jcsp.lang.*
import c2.GenerateSetsOfThree
import c2.ListToStream
import org.jcsp.groovy.*

class RunThreeToEightTest extends GroovyTestCase {
	
	void testRunThreeToEight() {
		def G2S = Channel.one2one()
		def S2C = Channel.one2one()
		
		def GSo3 = new GenerateSetsOfThree ( outChannel: G2S.out() )
		def L2S = new ListToStream ( inChannel: G2S.in(), outChannel: S2C.out() )
		def CSo8 = new CreateSetsOfEightForTest ( inChannel: S2C.in() )
		
		def processList = [ GSo3, L2S, CSo8 ]
		new PAR ( processList ).run()
		
		def expected = [17, 18, 19, 20, 21, 22, 23, 24]
		def actual = CSo8.outList
		assertTrue(expected == actual)
	}
}