/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.util.mule.transformer.test.stub.sampleservice;

import junit.framework.TestCase;

public class ObjectToJAXBElementTest extends TestCase {
	public void testTransform() throws Exception {
		jp.rough_diamond.commons.util.mule.transformer.test.ParentBean bean = 
			new jp.rough_diamond.commons.util.mule.transformer.test.ParentBean();
		bean.setXxx("Yamane");
		jp.rough_diamond.commons.util.mule.transformer.test.ChildBean child = 
			new jp.rough_diamond.commons.util.mule.transformer.test.ChildBean();
		bean.setChild(child);
		child.setYyy("Eiji");
		child.setZzz("abc");
		ParentBean afterBean = (ParentBean)(new ObjectToJAXBElement().transform(bean));
		assertEquals("�l������Ă��܂��B", "Yamane", afterBean.getXxx().getValue());
		assertEquals("�l������Ă��܂��B", "Eiji", afterBean.getChild().getValue().getYyy().getValue());
		assertEquals("�l������Ă��܂��B", "abc", afterBean.getChild().getValue().getZzz().getValue());
	}
}
