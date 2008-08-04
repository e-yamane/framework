package jp.rough_diamond.commons.util.mule.transformer;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;



import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractTransformer;

/**
 * �ʏ��Java�I�u�W�F�N�g����v���p�e�B���x�[�X��JAXB�G�������g�ɕϊ�������g�����X�t�H�[�}�[
 * CXF��wsdl2java�ɂ���ďo�͂��ꂽ�X�^�u�R�[�h�����ɕϊ��������s��
 * @author e-yamane
 */
abstract public class AbstractObjectToJAXBElement extends AbstractTransformer {
	private final static Log log = LogFactory.getLog(AbstractObjectToJAXBElement.class);
	
	@Override
	protected Object doTransform(Object src, String encoding) throws TransformerException {
		Method method = getMethod();
		if(method == null) {
			log.warn("���b�p�[���\�b�h���擾�ł��Ȃ����߃^�C�v�ϊ��͍s���܂���B");
			return src;
		}
		return transform(src, method.getParameterTypes()[0]);
	}

	Object transform(Object src, Class<?> parameterType) {
		Object factory = createObjectFactory(parameterType);
		if(factory == null) {
			log.warn("ObjectFactory�̎擾�Ɏ��s�������ߕϊ��͍s���܂���B");
			return src;
		}
		if(parameterType.isAssignableFrom(src.getClass())) {
			log.debug("�ϊ����s�v�ȃI�u�W�F�N�g�Ȃ̂ŕϊ������͍s���܂���B");
			return src;
		}
		Object dest = createObjectByType(factory, parameterType);
		if(dest == null) {
			log.warn("�ԋp�I�u�W�F�N�g�̐����Ɏ��s�������ߕϊ��͍s���܂���");
			return src;
		}
		try {
			copyProperty(factory, src, dest);
			return dest;
		} catch(Exception e) {
			log.warn("�v���p�e�B�̃R�s�[���ɗ�O�������������ߕϊ��͍s���܂���B", e);
			return src;
		}
	}

	void copyProperty(Object factory, Object src, Object dest) throws Exception {
		PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(dest);
		for(PropertyDescriptor pd : pds) {
			Class<?> pdType = pd.getPropertyType();
			PropertyDescriptor srcPD = PropertyUtils.getPropertyDescriptor(src, pd.getName());
			if(srcPD == null) {
				log.warn(pd.getName() + "�v���p�e�B���ϊ��O�I�u�W�F�N�g�ɑ��݂��܂���B�X�L�b�v���܂��B");
				continue;
			}
			Object srcVal = PropertyUtils.getProperty(src, pd.getName());
			if(pdType.equals(XMLGregorianCalendar.class)) {
				copyDateObject(pd, srcVal, dest);
			} else if(pdType.equals(JAXBElement.class)) {
				copyJAXBElement(factory, pd, srcVal, dest);
			}
		}
	}

	void copyJAXBElement(Object factory, PropertyDescriptor pd, Object srcVal, Object dest) throws Exception {
		if(srcVal == null) {
			return;
		}
		Class<?> genericType = getGenericsType(pd);
		if(genericType.getSimpleName().startsWith("ArrayOf") && !genericType.isAssignableFrom(srcVal.getClass())) {
			copyJAXElementOfArray(factory, pd, genericType, srcVal, dest);
		} else {
			String copyMethodName = getCopyMethodName(pd, dest);
			Object destVal = getCreateMethod(factory, copyMethodName).invoke(factory, srcVal);
			PropertyUtils.setProperty(dest, pd.getName(), destVal);
		}
	}

	@SuppressWarnings("unchecked")
	void copyJAXElementOfArray(Object factory, 
			PropertyDescriptor pd, Class<?> genericsType, Object srcVal, Object dest) throws Exception {
		Object destVal = createObjectByType(factory, genericsType);
		copyJAXBElement(factory, pd, destVal, dest);
		Method m = getGetListMethod(destVal);
		List list = (List)m.invoke(destVal, new Object[0]);
		Type t = m.getGenericReturnType();
		if(!(t instanceof ParameterizedType)) {
			throw new RuntimeException("Generics�̒�`������܂���B");
		}
		ParameterizedType pt = (ParameterizedType)t;
		Class<?> listGenericsType = (Class<?>)pt.getActualTypeArguments()[0];
		if(srcVal.getClass().isArray()) {
			copyJAXElementOfArrayFromArray(factory, listGenericsType, srcVal, list);
		} else if(Collection.class.isAssignableFrom(srcVal.getClass())) {
			copyJAXElementOfArrayFromCollection(factory, listGenericsType, (Collection)srcVal, list);
		}
	}
	
	@SuppressWarnings("unchecked")
	void copyJAXElementOfArrayFromCollection(Object factory,
			Class<?> genericsType, Collection srcVal, List list) throws Exception {
		for(Object element : srcVal) {
			copyElement(factory, genericsType, list, element);
		}
	}

	@SuppressWarnings("unchecked")
	void copyJAXElementOfArrayFromArray(Object factory, Class<?> genericsType, Object srcVal, List list) throws Exception {
		for(int i = 0 ; i < Array.getLength(srcVal) ; i++) {
			Object element = Array.get(srcVal, i);
			copyElement(factory, genericsType, list, element);
		}
	}

	@SuppressWarnings("unchecked")
	void copyElement(Object factory, Class<?> genericsType, List list, Object element) throws Exception {
		if(genericsType.isAssignableFrom(element.getClass())) {
			list.add(element);
		} else {
			Object destElement = createObjectByType(factory, genericsType);
			list.add(destElement);
			copyProperty(factory, element, destElement);
		}
	}

	Method getGetListMethod(Object destVal) {
		Method[] methods = destVal.getClass().getMethods();
		for(Method m : methods) {
			if(List.class.isAssignableFrom(m.getReturnType()) && m.getParameterTypes().length == 0) {
				return m;
			}
		}
		throw new RuntimeException("List�̕ԋp���ł��܂���B");
	}
	
	Class<?> getGenericsType(PropertyDescriptor pd) {
		Method m = pd.getWriteMethod();
		Type t = m.getGenericParameterTypes()[0];
		if(!(t instanceof ParameterizedType)) {
			log.debug(pd.getName() + "�́AJAXBElement��Generics����`����Ă��܂���B");
			return null;
		}
		ParameterizedType pt = (ParameterizedType)t;
		Class<?> genericType = (Class<?>)pt.getActualTypeArguments()[0];
		return genericType;
	}
	
	private Method getCreateMethod(Object factory, String copyMethodName) {
		Method[] methods = factory.getClass().getMethods();
		for(Method m : methods) {
			if(m.getName().equals(copyMethodName) 
					&& JAXBElement.class.isAssignableFrom(m.getReturnType()) 
					&& m.getParameterTypes().length == 1) {
				return m;
			}
		}
		throw new RuntimeException();
	}

	private String getCopyMethodName(PropertyDescriptor pd, Object dest) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("create");
		sb.append(dest.getClass().getSimpleName());
		char[] tmp = pd.getName().toCharArray();
		tmp[0] = Character.toUpperCase(tmp[0]);
		sb.append(tmp);
		return sb.toString();
	}
	
	void copyDateObject(PropertyDescriptor pd, Object srcVal, Object dest) throws Exception {
		if(srcVal == null) {
			return;
		}
		if(srcVal instanceof Date) {
			Date d = (Date)srcVal;
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			srcVal = cal;
		}
		if(srcVal instanceof Calendar && !(srcVal instanceof GregorianCalendar)) {
			//XXX ���������ɂ͓����
			Calendar cal = (Calendar)srcVal;
			GregorianCalendar gCal = new GregorianCalendar(
					cal.get(Calendar.YEAR),
					cal.get(Calendar.MONTH),
					cal.get(Calendar.DAY_OF_MONTH),
					cal.get(Calendar.HOUR_OF_DAY),
					cal.get(Calendar.MINUTE),
					cal.get(Calendar.SECOND));
			gCal.setTimeZone(cal.getTimeZone());
			srcVal = gCal;
		}
		if(srcVal instanceof GregorianCalendar) {
			GregorianCalendar gCal = (GregorianCalendar)srcVal;
			DatatypeFactory dtf = DatatypeFactory.newInstance();
			XMLGregorianCalendar xCal = dtf.newXMLGregorianCalendar(gCal);
			PropertyUtils.setProperty(dest, pd.getName(), xCal);
		} else {
			log.debug(pd.getName() + "�v���p�e�B���ϊ��O�I�u�W�F�N�g�ł͓��t�ł͂���܂���B");
		}
	}

	Object createObjectByType(Object factory, Class<?> parameterType) {
		try {
			Class<?> factoryType = factory.getClass();
			Method[] methods = factoryType.getMethods();
			for(Method m : methods) {
				if(m.getName().startsWith("create") && m.getParameterTypes().length == 0 && m.getReturnType().equals(parameterType)) {
					return m.invoke(factory, new Object[0]);
				}
			}
		} catch(Exception e) {
			log.debug(e.getMessage(), e);
		}
		return null;
	}
	
	Object createObjectFactory(Class<?> parameterType) {
		try {
			Package p = parameterType.getPackage();
			return Class.forName(p.getName() + "." + "ObjectFactory").newInstance();
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			return null;
		}
	}
	
	protected Method getMethod() {
		Class<?> portType = getPortType();
		String operation = getOperation();
		Method[] methods = portType.getMethods();
		for(Method m : methods) {
			//XXX �����p�����[�^�̏ꍇ�͂܂��l����
			if(m.getName().equals(operation) && m.getParameterTypes().length == 1) {
				return m;
			}
		}
		return null;
	}
	
	/**
	 * WSDL�̃|�[�g�ɑΉ�����N���X��ԋp����
	 * @return
	 */
	abstract protected Class<?> getPortType();

	/**
	 * �I�y���[�V��������ԋp���� 
	 */
	abstract protected String getOperation();
}
