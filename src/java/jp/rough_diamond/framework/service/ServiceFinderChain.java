package jp.rough_diamond.framework.service;

import java.util.List;

/**
 * ServiceFinder��Chain of Responsibility
 * (������Chain of Responsibility�p�^�[���ł͂���܂���)
 * @author e-yamane
 */
public class ServiceFinderChain implements ServiceFinder {
	private List<ServiceFinder> serviceFinderChain;
	public ServiceFinderChain(List<ServiceFinder> serviceFinderChain) {
		this.serviceFinderChain = serviceFinderChain;
	}
	
	public <T extends Service> T getService(Class<T> cl) {
		for(ServiceFinder serviceFinder : serviceFinderChain) {
			T ret = serviceFinder.getService(cl);
			if(ret != null) {
				return ret;
			}
		}
		return null;
	}
}
