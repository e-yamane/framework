/*
 * ====================================================================
 * 
 *  Copyright 2007 Eiji Yamane(yamane@super-gs.jp)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 */
/**
 * 
 */
package jp.rough_diamond.framework.transaction;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import jp.rough_diamond.commons.di.DIContainerFactory;
import jp.rough_diamond.framework.service.Service;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

/**
 * ＤＢアクセスサービスを生成するFinder
 * @version 1.0
 * @author $Author$
 */
public class ServiceFinder implements jp.rough_diamond.framework.service.ServiceFinder {
	@SuppressWarnings("unchecked")
	public <T extends Service> T getService(Class<T> cl) {
		init();
		try {
			T base = (T)DIContainerFactory.getDIContainer().getObject(cl.getName());
			if(base == null) {
				base = cl.newInstance();
			}
			ProxyFactory pf = new ProxyFactory(base);
			pf.addAdvice(mi);
			pf.addAdvisor(interceptor);
			pf.setOptimize(true);
			return (T)pf.getProxy();
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	
	private static boolean isInit = false;
	private static void init() {
		if(!isInit) {
			init2();
		}
	}

	private synchronized static void init2() {
		if(!isInit) {
			mi = (MethodInterceptor)DIContainerFactory.getDIContainer().getObject("transactionInterceptor");
			interceptor = new ServiceAdvisor();
			interceptor.setAdvice(mi);
			isInit = true;
		}
	}
	
	static MethodInterceptor mi;
	static StaticMethodMatcherPointcutAdvisor interceptor;
	
	private final static class ServiceAdvisor extends StaticMethodMatcherPointcutAdvisor {
		public final static long serialVersionUID = -1L;
		
		@SuppressWarnings("unchecked")
		public boolean matches(Method arg0, Class arg1) {
			if(Service.class.isAssignableFrom(arg1) && 
					(arg0.getModifiers() & Modifier.PUBLIC) == Modifier.PUBLIC) {
				return true;
			} else {
				return false;
			}
		}
	}
}
