/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.util.mule.transformer;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;

import jp.rough_diamond.commons.util.PropertyUtils;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 通常のJAXBエレメントからプロパティ名ベースでJavaオブジェクトに変換させるトランスフォーマー
 * CXFのwsdl2javaによって出力されたスタブコードを元に変換処理を行う
 */
@SuppressWarnings("unchecked")
public class JAXBElementToObject {
	private final static Log log = LogFactory.getLog(JAXBElementToObject.class);
	
//	@Override
//	protected Object doTransform(Object src, String encoding) throws TransformerException {
//		return transform(src, getReturnType());
//	}
	public Object transform(Object src, Class<?> returnType) {
		return transform(src, returnType, returnType.getComponentType());
	}
	
	public Object transform(Object src, Class<?> returnType, Class<?> componentType) {
		if(src == null) {
			return null;
		}
		log.debug(src);
		if(JAXBElement.class.isAssignableFrom(src.getClass())) {
			return transform(((JAXBElement)src).getValue(), returnType);
		}
		if(returnType.isAssignableFrom(src.getClass())) {
			log.debug("変換が不要なオブジェクトなので変換処理は行いません。");
			return src;
		}
		if(returnType.isPrimitive()) {
			log.debug("変換が不要なオブジェクト（プリミティブ）なので変換処理は行いません。");
			return src;
		}
		if(isArrayType(src.getClass())) {
			List list = copyList(src, componentType);
			if(returnType.isArray()) {
				Object ret = Array.newInstance(componentType, list.size());
				return list.toArray((Object[])ret);
			} else {
				return list;
			}
		}
		Object dest = createObjectByType(returnType);
		if(dest == null) {
			log.warn("返却オブジェクトの生成に失敗したため変換は行いません");
			return src;
		}
		try {
			copyProperty(src, dest);
			return dest;
		} catch(Exception e) {
			log.warn("プロパティのコピー中に例外が発生したため変換は行いません。", e);
			return src;
		}
	}
	
	List copyList(Object src, Class<?> componentType) {
		List ret = new ArrayList();
		try {
			List srcArray = extractArrayData(src.getClass(), src);
			for(Object o : srcArray) {
				ret.add(transform(o, componentType));
			}
			return ret;
		} catch(Exception e) {
			log.warn("プロパティのコピー中に例外が発生したため変換は行いません。", e);
			return ret;
		}
	}
	
	void copyProperty(Object src, Object dest) throws Exception {
		PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(src);
		for(PropertyDescriptor pd : pds) {
			Class<?> pdType = pd.getPropertyType();
			if(pdType.equals(JAXBElement.class)) {
				Type t = pd.getReadMethod().getGenericReturnType();
				if(t instanceof ParameterizedType) {
					ParameterizedType pt = (ParameterizedType)t;
					pdType = (Class)pt.getActualTypeArguments()[0];
				}
			}
			PropertyDescriptor destPD = PropertyUtils.getPropertyDescriptor(dest, pd.getName());
			if(destPD == null) {
				log.warn(pd.getName() + "プロパティが変換後オブジェクトに存在しません。スキップします。");
				continue;
			}
			Method m = PropertyUtils.getGetterMethod(src, pd.getName()); 
			Object srcVal = m.invoke(src);
			if(srcVal == null) {
				//nullの場合はスキップ
			} else if (destPD.getWriteMethod() == null) {
				//nullの場合はスキップ
			} else {
				copyElement(pdType, destPD, srcVal, dest);
			}
		}
	}

	void copyElement(Class srcType, PropertyDescriptor destPD, Object srcVal, Object dest) throws Exception {
		if(srcVal instanceof JAXBElement) {
			srcVal = ((JAXBElement)srcVal).getValue();
		}
		if(srcType.equals(XMLGregorianCalendar.class)) {
			copyDateObject(destPD, (XMLGregorianCalendar)srcVal, dest);
		} else if(isArrayType(srcType)) {
			copyJAXElementOfArray(srcType, destPD, srcVal, dest);
		} else {
			Object destVal = transform(srcVal, destPD.getWriteMethod().getParameterTypes()[0]);
			PropertyUtils.setProperty(dest, destPD.getName(), destVal);
		}
	}
	
	void copyJAXElementOfArray(Class<?> srcType, PropertyDescriptor pd, Object srcVal, Object dest) throws Exception {
		List arrayVal = extractArrayData(srcType, srcVal);
		if(pd.getWriteMethod().getParameterTypes()[0].isArray()) {
			copyToArray(arrayVal, pd, dest);
		} else {
			copyToList(arrayVal, pd, dest);
		}
	}
	
	void copyToList(List arrayVal, PropertyDescriptor pd, Object dest) throws Exception {
		Type t = pd.getWriteMethod().getGenericParameterTypes()[0];
		if(!(t instanceof ParameterizedType)) {
			log.debug(pd.getName() + "リストの要素タイプの指定がないためコピーできません。");
			return;
		}
		ParameterizedType pt = (ParameterizedType)t;
		Class genericType = (Class)pt.getActualTypeArguments()[0];
		List destList = new ArrayList();
		for(Object srcVal : arrayVal) {
			Object destVal = transform(srcVal, genericType);
			destList.add(destVal);
		}
		PropertyUtils.setProperty(dest, pd.getName(), destList);
	}

	void copyToArray(List arrayVal, PropertyDescriptor pd, Object dest) throws Exception {
		Class cl = pd.getWriteMethod().getParameterTypes()[0].getComponentType();
		Object destArray = Array.newInstance(cl, arrayVal.size());
		int index = 0;
		for(Object srcVal : arrayVal) {
			Object destVal = transform(srcVal, cl);
			Array.set(destArray, index++, destVal);
		}
		PropertyUtils.setProperty(dest, pd.getName(), destArray);
	}

	void copyDateObject(PropertyDescriptor pd, XMLGregorianCalendar srcVal, Object dest) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, srcVal.getYear());
		cal.set(Calendar.MONTH, srcVal.getMonth() - 1);
		cal.set(Calendar.DAY_OF_MONTH, srcVal.getDay());
		cal.set(Calendar.HOUR_OF_DAY, srcVal.getHour());
		cal.set(Calendar.MINUTE, srcVal.getMinute());
		cal.set(Calendar.SECOND, srcVal.getSecond());
		cal.set(Calendar.MILLISECOND, srcVal.getMillisecond());
		cal.setTimeZone(srcVal.getTimeZone(srcVal.getTimezone()));
		Class paramType = pd.getWriteMethod().getParameterTypes()[0];
		if(Date.class.isAssignableFrom(paramType)) {
			pd.getWriteMethod().invoke(dest, cal.getTime());
		} else {
			pd.getWriteMethod().invoke(dest, cal);
		}
	}

	boolean isArrayType(Class srcType) {
		return srcType.getSimpleName().startsWith("ArrayOf") /*&& !srcType.isAssignableFrom(srcVal.getClass())*/;
	}
	
	List extractArrayData(Class srcType, Object src) throws Exception {
		String propertyName = srcType.getSimpleName().replaceAll("^ArrayOf", "");
		char[] tmp = propertyName.toCharArray();
		tmp[0] = Character.toLowerCase(tmp[0]);
		propertyName = new String(tmp);
		PropertyDescriptor pd2 = PropertyUtils.getPropertyDescriptor(src, propertyName);
		List arrayVal = (List)pd2.getReadMethod().invoke(src);
		return arrayVal;
	}
	
	Object createObjectByType(Class<?> parameterType) {
		try {
			return parameterType.newInstance();
		} catch(Exception e) {
			log.debug(e.getMessage(), e);
		}
		return null;
	}
}
