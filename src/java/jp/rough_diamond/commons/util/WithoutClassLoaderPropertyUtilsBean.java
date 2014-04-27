/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */

package jp.rough_diamond.commons.util;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 */
public class WithoutClassLoaderPropertyUtilsBean extends PropertyUtilsBean {
	private final static Log log = LogFactory.getLog(WithoutClassLoaderPropertyUtilsBean.class);
	
	@Override
	public Object getIndexedProperty(Object bean, String name, int index)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		return checkClassLoader(super.getIndexedProperty(bean, name, index));
	}

	@Override
	public Object getMappedProperty(Object bean, String name, String key)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		return checkClassLoader(super.getMappedProperty(bean, name, key));
	}

	@Override
	public Object getSimpleProperty(Object bean, String name)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		return checkClassLoader(super.getSimpleProperty(bean, name));
	}
	
	private Object checkClassLoader(Object returnValue) throws NoSuchMethodException {
		if(returnValue instanceof ClassLoader) {
			log.debug("ClassLoader‚Ì•Ô‹p‚ðƒLƒƒƒ“ƒZƒ‹‚µ‚Ü‚·");
			throw new NoSuchMethodException("ClassLoader‚Ì•Ô‹p‚ðƒLƒƒƒ“ƒZƒ‹‚µ‚Ü‚·");
		}
		return returnValue;
	}
}
