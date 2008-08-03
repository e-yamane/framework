package jp.rough_diamond.framework.service;

import java.util.List;

/**
 * ServiceFinder‚ÌChain of Responsibility
 * (À‘•‚ÍChain of Responsibilityƒpƒ^[ƒ“‚Å‚Í‚ ‚è‚Ü‚¹‚ñ)
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
