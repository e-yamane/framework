/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */

package jp.rough_diamond.commons.util.mule.transformer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;

import edu.emory.mathcs.backport.java.util.Arrays;

import jp.rough_diamond.commons.util.mule.transformer.JAXBElementToObject;
import jp.rough_diamond.commons.util.mule.transformer.test.stub.sampleservice.ArrayOfString;
import jp.rough_diamond.commons.util.mule.transformer.test.stub.sampleservice.ChildBean;
import jp.rough_diamond.commons.util.mule.transformer.test.stub.sampleservice.ObjectFactory;
import jp.rough_diamond.commons.util.mule.transformer.test.stub.sampleservice.ParentBean;
import junit.framework.TestCase;

public class JAXBElementToObjectTest extends TestCase {
	@SuppressWarnings("unchecked")
	public void testIt() throws Exception {
		ObjectFactory of = new ObjectFactory();
		ParentBean base = of.createParentBean();
		base.setXxx(of.createParentBeanXxx("Yamane"));
		ChildBean child = of.createChildBean();
		child.setYyy(of.createChildBeanYyy("Eiji"));
		child.setZzz(of.createChildBeanZzz("abc"));
		base.setChild(of.createParentBeanChild(child));
		DatatypeFactory dtf = DatatypeFactory.newInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		GregorianCalendar cal1 = (GregorianCalendar)Calendar.getInstance();
		cal1.setTime(sdf.parse("2009/05/11"));
		base.setCal(of.createParentBeanCal(dtf.newXMLGregorianCalendar(cal1)));
		ArrayOfString aos = of.createArrayOfString();
		aos.getString().addAll(Arrays.asList(new String[]{"abc", "xyz"}));
		base.setArray(of.createParentBeanArray(aos));
		base.setBoolean1(Boolean.TRUE);
		GregorianCalendar cal2 = (GregorianCalendar)Calendar.getInstance();
		cal2.setTime(sdf.parse("2009/05/13"));
		base.setDate(dtf.newXMLGregorianCalendar(cal2));
		base.setInt1(of.createParentBeanInt1(10));
		ArrayOfString aos2 = of.createArrayOfString();
		aos2.getString().addAll(Arrays.asList(new String[]{"123", "456", "789"}));
		base.setList(of.createParentBeanArray(aos2));
		
		jp.rough_diamond.commons.util.mule.transformer.test.ParentBean after = (jp.rough_diamond.commons.util.mule.transformer.test.ParentBean)(new JAXBElementToObject().transform(base, jp.rough_diamond.commons.util.mule.transformer.test.ParentBean.class));
		assertEquals("値が誤っています。", "Yamane", after.getXxx());
		assertEquals("値が誤っています。", "Eiji", after.getChild().getYyy());
		assertEquals("値が誤っています。", "abc", after.getChild().getZzz());
		assertEquals("値が誤っています。", "2009/05/11", sdf.format(after.getCal().getTime()));
		assertEquals("配列サイズが誤っています。", 2, after.getArray().length);
		assertEquals("値が誤っています。", "abc", after.getArray()[0]);
		assertEquals("値が誤っています。", "xyz", after.getArray()[1]);
		assertTrue("値が誤っています。", after.isBoolean1());
		assertEquals("値が誤っています。", "2009/05/13", sdf.format(after.getDate()));
		assertEquals("値が誤っています。", 10, after.getInt1().intValue());
		assertEquals("リストサイズが誤っています。", 3, after.getList().size());
	}
}
