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
/*
 * 作成日: 2005/02/14
 *
 */
package jp.rough_diamond.framework.service;

import java.util.HashMap;
import java.util.Map;

import jp.rough_diamond.commons.di.DIContainerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * サービスを取得するラッパークラス
 * 一度取得できたクラスはキャッシュされる。
 * @author $Author$
 * @date $Date$
 */
@SuppressWarnings("unchecked")
public class ServiceLocator {
	private final static Log log = LogFactory.getLog(ServiceLocator.class);
	private static Map<Class, Service> serviceMap = new HashMap<Class, Service>();

	private final static String DEFAULT_SERVICE_FINDER_NAME = "jp.rough_diamond.framework.transaction.ServiceFinder";

    /**
     * サービスを取得する
     * 取得したオブジェクトはトランザクションマネージャがWeavingされている
     * @param cl    取得対象サービスクラス
     * @return      取得対象サービス 
     */
	public static <T extends Service> T getService(Class<T> cl) {
		T service = (T)serviceMap.get(cl);
		if(service == null) {
			findService(cl);
			service = (T)serviceMap.get(cl);
		}
		if(service == null) {
			log.warn("サービスが取得できません。");
			throw new RuntimeException();
		}
		return service;
    }	
	
    private synchronized static <T extends Service> void findService(Class<T> cl) {
    	if(serviceMap.get(cl) == null) {
        	ServiceFinder finder = (ServiceFinder)DIContainerFactory.getDIContainer().getObject("serviceFinder");
        	if(finder == null) {
        		finder = getDefaultFinder();
        	}
        	T service = finder.getService(cl);
        	serviceMap.put(cl, service);
    	}
    }
    
    private static ServiceFinder defaultFinder = null;
	private static ServiceFinder getDefaultFinder() {
		if(defaultFinder == null) {
			createFinder();
		}
		return defaultFinder;
	}
	
	private synchronized static void createFinder() {
		try {
			if(defaultFinder == null) {
				Class cl = Class.forName(DEFAULT_SERVICE_FINDER_NAME);
				defaultFinder = (ServiceFinder)cl.newInstance();
			}
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
