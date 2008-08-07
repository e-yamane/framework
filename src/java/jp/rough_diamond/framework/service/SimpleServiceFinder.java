package jp.rough_diamond.framework.service;

import jp.rough_diamond.commons.di.DIContainerFactory;

public class SimpleServiceFinder implements ServiceFinder {
	@Override
	@SuppressWarnings("unchecked")
	public <T extends Service> T getService(Class<T> cl) {
		try {
			T ret = (T)DIContainerFactory.getDIContainer().getObject(cl.getName());
			if(ret == null) {
				ret = cl.newInstance();
			}
			return ret;
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
