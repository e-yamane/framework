package jp.rough_diamond.tools.makewsdl;

import junit.framework.TestCase;

public class GetAndMakeWSDLTest extends TestCase {
	public void testGetWsdlName() throws Exception {
		assertEquals("Hello.wsdl", GetAndMakeWSDL.getWsdlName("http://localhost:20080/services/Hello"));
	}
}
