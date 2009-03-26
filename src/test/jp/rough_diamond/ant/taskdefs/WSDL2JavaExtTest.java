/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
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
