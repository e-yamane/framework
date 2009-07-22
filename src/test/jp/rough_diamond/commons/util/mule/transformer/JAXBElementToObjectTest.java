/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */

package jp.rough_diamond.commons.util.mule.transformer;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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
	public void testTransformObject() throws Exception {
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
		assertEquals("�l������Ă��܂��B", "Yamane", after.getXxx());
		assertEquals("�l������Ă��܂��B", "Eiji", after.getChild().getYyy());
		assertEquals("�l������Ă��܂��B", "abc", after.getChild().getZzz());
		assertEquals("�l������Ă��܂��B", "2009/05/11", sdf.format(after.getCal().getTime()));
		assertEquals("�z��T�C�Y������Ă��܂��B", 2, after.getArray().length);
		assertEquals("�l������Ă��܂��B", "abc", after.getArray()[0]);
		assertEquals("�l������Ă��܂��B", "xyz", after.getArray()[1]);
		assertTrue("�l������Ă��܂��B", after.isBoolean1());
		assertEquals("�l������Ă��܂��B", "2009/05/13", sdf.format(after.getDate()));
		assertEquals("�l������Ă��܂��B", 10, after.getInt1().intValue());
		assertEquals("���X�g�T�C�Y������Ă��܂��B", 3, after.getList().size());
	}
	
	@SuppressWarnings("unchecked")
	public void testTransformStringList() throws Exception {
		ObjectFactory of = new ObjectFactory();
		ArrayOfString aos = of.createArrayOfString();
		aos.getString().addAll(Arrays.asList(new String[]{"abc", "xyz"}));
		List<String> ret = (List<String>)new JAXBElementToObject().transform(aos, List.class, String.class);
		assertEquals("�z��T�C�Y������Ă��܂��B", 2, ret.size());
		assertEquals("�l������Ă��܂��B", "abc", ret.get(0));
		assertEquals("�l������Ă��܂��B", "xyz", ret.get(1));
	}
	
	@SuppressWarnings("unchecked")
	public void testTransformStringArray() throws Exception {
		ObjectFactory of = new ObjectFactory();
		ArrayOfString aos = of.createArrayOfString();
		aos.getString().addAll(Arrays.asList(new String[]{"abc", "xyz"}));
		String[] ret = (String[])new JAXBElementToObject().transform(aos, String[].class);
		assertEquals("�z��T�C�Y������Ă��܂��B", 2, ret.length);
		assertEquals("�l������Ă��܂��B", "abc", ret[0]);
		assertEquals("�l������Ă��܂��B", "xyz", ret[1]);
	}

	public static List<String> foo() {
		return null;
	}
	
	public static String[] bar() {
		return null;
	}

	public static void main(String[] args) throws Exception {
		Method m = JAXBElementToObjectTest.class.getMethod("foo");
		Class<?> cl = m.getReturnType();
		System.out.println(cl.getClass());
		ParameterizedType pt = (ParameterizedType)m.getGenericReturnType();
		System.out.println(pt.getClass());
		System.out.println(pt);
		System.out.println(pt.getActualTypeArguments()[0]);
		System.out.println(pt.getRawType());

		m = JAXBElementToObjectTest.class.getMethod("bar");
		System.out.println(m.getGenericReturnType().getClass().getName());
	}
}
