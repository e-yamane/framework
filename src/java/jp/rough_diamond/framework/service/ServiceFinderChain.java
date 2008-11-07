package jp.rough_diamond.framework.service;

import java.util.List;

/**
 * ServiceFinderのChain of Responsibility
 * (実装はChain of Responsibilityパターンではありません)
 * @author e-yamane
 */
public class ServiceFinderChain implements ServiceFinder {
	private List<ServiceFinder> serviceFinderChain;
	public ServiceFinderChain(List<ServiceFinder> serviceFinderChain) {
		this.serviceFinderChain = serviceFinderChain;
	}
	
	public <T extends Service> T getService(Class<T> cl, Class<? extends T> defaultClass) {
		for(ServiceFinder serviceFinder : serviceFinderChain) {
			T ret = serviceFinder.getService(cl, defaultClass);
			if(ret != null) {
				return ret;
			}
		}
		return null;
	}
}
