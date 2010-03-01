/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.testing;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import jp.rough_diamond.commons.di.AbstractDIContainer;
import jp.rough_diamond.commons.di.DIContainer;
import jp.rough_diamond.commons.di.DIContainerFactory;
import jp.rough_diamond.commons.service.BasicService;
import jp.rough_diamond.framework.service.Service;
import jp.rough_diamond.framework.service.ServiceFinder;
import jp.rough_diamond.framework.service.ServiceLocator;
import jp.rough_diamond.framework.service.ServiceLocatorLogic;
import jp.rough_diamond.framework.service.SimpleServiceLocatorLogic;
import junit.framework.TestCase;

/**
 * DBUnitを利用してデータをローディングしているテストケース
 */
public abstract class DataLoadingTestCase extends TestCase {
	private final static Log log = LogFactory.getLog(DataLoadingTestCase.class);
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setUpDB();
    }

    public static void setUpDB() throws Exception {
        DBInitializer.clearModifiedClasses();
        DIContainer org = DIContainerFactory.getDIContainer();
        DIContainerFactory.setDIContainer(new DIContainerExt(org));
    }
    
    @Override
    protected void tearDown() throws Exception {
    	try {
    		cleanUpDB();
    	} finally {
    		super.tearDown();
    	}
    }
    
    public static void cleanUpDB() throws Exception {
    	DIContainer di = DIContainerFactory.getDIContainer();
    	if(di instanceof DIContainerExt) {
	        DIContainerExt ext = (DIContainerExt)di;
	        DIContainerFactory.setDIContainer(ext.org);
    	}
        DBInitializer.clearModifiedData();
    }
    
    static class DIContainerExt extends AbstractDIContainer {
    	DIContainer org;
    	DIContainerExt(DIContainer org) {
    		this.org = org;
    	}
    	static ServiceLocatorLogic sllExt;
		@SuppressWarnings("unchecked")
		@Override
		public synchronized <T> T getObject(Class<T> arg0, Object arg1) {
			if(arg1.equals(ServiceLocator.SERVICE_LOCATOR_KEY)) {
				synchronized(this) {
					if(sllExt == null) {
						log.debug("ServiceLocatorLogicの取得要求です");
						DIContainer current = DIContainerFactory.getDIContainer();
						DIContainerFactory.setDIContainer(org);
						try {
							ServiceLocatorLogic orgLogic = ServiceLocatorLogic.getServiceLocatorLogic();
							sllExt = (ServiceLocatorLogic)new ServiceLocatorLogicExt(orgLogic);
						} finally {
							DIContainerFactory.setDIContainer(current);
						}
					}
					return (T)sllExt;
				}
			}
			return org.getObject(arg0, arg1);
		}

		@Override
		public <T> T getSource(Class<T> arg0) {
			return org.getSource(arg0);
		}
    }
    
    static class ServiceLocatorLogicExt extends SimpleServiceLocatorLogic {
    	private ServiceLocatorLogic org;
    	ServiceLocatorLogicExt(ServiceLocatorLogic org) {
    		this.org = org;
    	}
		@Override
    	public <T extends Service> T getService(Class<T> cl, Class<? extends T> defaultClass) {
    		if(cl.equals(BasicService.class)) {
    			return super.getService(cl, defaultClass);
    		} else {
    			return org.getService(cl, defaultClass);
    		}
    	}

    	protected ServiceFinder getFinder() {
        	return new ServiceFinder() {
				@SuppressWarnings("unchecked")
				@Override
				public <T extends Service> T getService(Class<T> cl, Class<? extends T> defaultClass) {
					log.debug("BasicServiceを作成します");
					BasicService service = (BasicService)org.getService(cl, defaultClass);
					ProxyFactory pf = new ProxyFactory(service);
					MethodInterceptor mi = new MethodInterceptor() {
						@Override
						public Object invoke(MethodInvocation arg0) throws Throwable {
							log.debug("call deleteAll");
							Object ret = arg0.proceed();
							DBInitializer.addModifiedClasses((Class)arg0.getArguments()[0]);
							return ret;
						}
					};
					NameMatchMethodPointcut pc = new NameMatchMethodPointcut();
					pc.addMethodName("deleteAll");
					pf.addAdvisor(new DefaultPointcutAdvisor(pc, mi));
					pf.setOptimize(true);
					return (T)pf.getProxy();
				}
        	};
        }
    }
}
