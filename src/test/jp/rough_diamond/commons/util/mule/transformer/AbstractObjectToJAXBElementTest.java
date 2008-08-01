package jp.rough_diamond.commons.util.mule.transformer;

import java.beans.PropertyDescriptor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.beanutils.PropertyUtils;

import jp.rough_diamond.commons.util.mule.transformer.AbstractObjectToJAXBElement;
import jp.rough_diamond.commons.util.mule.transformer.test.ArrayOfDetails;
import jp.rough_diamond.commons.util.mule.transformer.test.Details;
import jp.rough_diamond.commons.util.mule.transformer.test.JAXBElementBean;
import jp.rough_diamond.commons.util.mule.transformer.test.ObjectFactory;
import jp.rough_diamond.commons.util.mule.transformer.test.Sample;
import junit.framework.TestCase;

public class AbstractObjectToJAXBElementTest extends TestCase {
	public void testGetMethod() throws Exception {
		TransformerExt t = new TransformerExt();
		assertNotNull("メソッドが返却されていません。", t.getMethod());
	}
	
	public void testCreateObjectFactory() {
		TransformerExt t = new TransformerExt();
		assertEquals("ObjectFactoryクラスが誤っています。", 
				ObjectFactory.class, 
				t.createObjectFactory(t.getMethod().getParameterTypes()[0]).getClass());
	}
	
	public void testCreateObjectByType() {
		TransformerExt t = new TransformerExt();
		Class<?> type = t.getMethod().getParameterTypes()[0];
		Object factory = t.createObjectFactory(type);
		Object bean = t.createObjectByType(factory, type);
		assertEquals("返却オブジェクトのタイプが誤っています。", type, bean.getClass());
	}
	
	public void testCopyDateObject() throws Exception {
		TransformerExt t = new TransformerExt();
		Class<?> type = t.getMethod().getParameterTypes()[0];
		Object factory = t.createObjectFactory(type);
		JAXBElementBean dest = (JAXBElementBean)t.createObjectByType(factory, type);
		PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(dest, "acceptDate");
		t.copyDateObject(pd, null, dest);
		assertNull("Nullではありません。", dest.getAcceptDate());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Date d = sdf.parse("20080102030405006");
		t.copyDateObject(pd, d, dest);
		XMLGregorianCalendar xCal = dest.getAcceptDate();
		assertEquals("年が誤っています。", 2008, 	xCal.getYear());
		assertEquals("月が誤っています。", 1,    	xCal.getMonth());
		assertEquals("日が誤っています。", 2, 		xCal.getDay());
		assertEquals("時が誤っています。", 3, 		xCal.getHour());
		assertEquals("分が誤っています。", 4, 		xCal.getMinute());
		assertEquals("秒が誤っています。", 5, 		xCal.getSecond());
		assertEquals("ミリ秒が誤っています。", 6, 		xCal.getMillisecond());
		assertEquals("全体として誤っています。", "20080102030405006", sdf.format(xCal.toGregorianCalendar().getTime()));
	}
	
	public void testCopyJAXBElement() throws Exception {
		TransformerExt t = new TransformerExt();
		Class<?> type = t.getMethod().getParameterTypes()[0];
		Object factory = t.createObjectFactory(type);
		JAXBElementBean dest = (JAXBElementBean)t.createObjectByType(factory, type);
		PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(dest, "acceptId");
		t.copyJAXBElement(factory, pd, null, dest);
		assertNull("Nullではありません。", dest.getAcceptId());
		t.copyJAXBElement(factory, pd, "xyz", dest);
		assertEquals("値が誤っています。", "xyz", dest.getAcceptId().getValue());
		
		pd = PropertyUtils.getPropertyDescriptor(dest, "details");
		t.copyJAXBElement(factory, pd, null, dest);
		assertNull("Nullではありません。", dest.getDetails());

		InBeanDetails detail1 = new InBeanDetails();
		detail1.setItemId(1L);
		InBeanDetails detail2 = new InBeanDetails();
		detail2.setItemId(2L);
		
		t.copyJAXBElement(factory, pd, new InBeanDetails[]{detail1, detail2}, dest);
		JAXBElement<ArrayOfDetails> details = dest.getDetails();
		assertNotNull("Nullです。", details);
		List<Details> list = details.getValue().getAcceptDetails();
		assertEquals("要素数が誤っています。", 2, list.size());
		assertEquals("値が誤っています。", 1L, list.get(0).getItemId().getValue().longValue());
		assertEquals("値が誤っています。", 2L, list.get(1).getItemId().getValue().longValue());

		List<InBeanDetails> baseList = new ArrayList<InBeanDetails>();
		baseList.add(detail2);
		baseList.add(detail1);
		t.copyJAXBElement(factory, pd, baseList, dest);
		details = dest.getDetails();
		assertNotNull("Nullです。", details);
		list = details.getValue().getAcceptDetails();
		assertEquals("要素数が誤っています。", 2, list.size());
		assertEquals("値が誤っています。", 2L, list.get(0).getItemId().getValue().longValue());
		assertEquals("値が誤っています。", 1L, list.get(1).getItemId().getValue().longValue());
	}
	
	public void testGetGenericsType() throws Exception {
		TransformerExt t = new TransformerExt();
		Class<?> type = t.getMethod().getParameterTypes()[0];
		Object factory = t.createObjectFactory(type);
		JAXBElementBean dest = (JAXBElementBean)t.createObjectByType(factory, type);
		PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(dest, "details");
		assertEquals("返却値が誤っています。", ArrayOfDetails.class, t.getGenericsType(pd));
	}
	
	public static class InBean {
		private Date acceptDate;
		private String acceptId;
		private InBeanDetails[] details;
		public InBeanDetails[] getDetails() {
			return details;
		}

		public void setDetails(InBeanDetails[] details) {
			this.details = details;
		}

		public String getAcceptId() {
			return acceptId;
		}

		public void setAcceptId(String acceptId) {
			this.acceptId = acceptId;
		}

		public Date getAcceptDate() {
			return acceptDate;
		}

		public void setAcceptDate(Date acceptDate) {
			this.acceptDate = acceptDate;
		}
	}
	
	public static class InBeanDetails {
		private Long itemId;

		public Long getItemId() {
			return itemId;
		}

		public void setItemId(Long itemId) {
			this.itemId = itemId;
		}
	}
	
	public static class TransformerExt extends AbstractObjectToJAXBElement {
		@Override
		protected String getOperation() {
			return "doIt";
		}

		@Override
		protected Class<?> getPortType() {
			return Sample.class;
		}
	}
}
