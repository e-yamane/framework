package jp.rough_diamond.ant.taskdefs;

import java.io.File;

import junit.framework.TestCase;

public class WSDL2JavaExtTest extends TestCase {
	public void testGetSubPackageName() throws Exception {
		assertEquals("hello", WSDL2JavaExt.getSubPackageName("Hello.wsdl"));
	}
	
	public void testGetPackageName() throws Exception {
		WSDL2JavaExt task = new WSDL2JavaExt();
		File f = new File("Hello.wsdl");
		task.setRootPackage("jp.rough_diamond.edi.stub");
		assertEquals("jp.rough_diamond.edi.stub.hello", task.getPackage(f));
	}
}
