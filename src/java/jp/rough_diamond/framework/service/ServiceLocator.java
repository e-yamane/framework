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
	static Map<Class<?>, Service> serviceMap = new HashMap<Class<?>, Service>();

	private final static String DEFAULT_SERVICE_FINDER_NAME = "jp.rough_diamond.framework.transaction.ServiceFinder";

	public final static String SERVICE_FINDER_KEY = "serviceFinder";

	/**
     * サービスを取得する
	 * @param <T>	返却するインスタンスのタイプ
     * @param cl    取得対象サービスクラス
     * @return      サービス 
     */
	public static <T extends Service> T getService(Class<T> cl) {
		return getService(cl, cl);
    }	
	
	/**
	 * サービスを取得する
	 * @param <T>				返却するインスタンスのタイプ
	 * @param cl				サービスの基点となるインスタンスのタイプ
	 * @param defaultClassName	DIコンテナに設定が無い場合に実体化するインスタンスのタイプ名	
     * @return      サービス 
	 */
	public static <T extends Service> T getService(Class<T> cl, String defaultClassName) {
		Class<? extends T> defaultClass;
		try {
			defaultClass = (Class<? extends T>)Class.forName(defaultClassName);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		return getService(cl, defaultClass);
	}
	
	/**
	 * サービスを取得する
	 * @param <T>				返却するインスタンスのタイプ
	 * @param cl				サービスの基点となるインスタンスのタイプ
	 * @param defaultClass		DIコンテナに設定が無い場合に実体化するインスタンスのタイプ	
     * @return      サービス 
	 */
	public static <T extends Service> T getService(Class<T> cl, Class<? extends T> defaultClass) {
		T service = (T)serviceMap.get(cl);
		if(service == null) {
			findService(cl, defaultClass);
			service = (T)serviceMap.get(cl);
		}
		if(service == null) {
			log.warn("サービスが取得できません。");
			throw new RuntimeException();
		}
		return service;
	}
	
    private synchronized static <T extends Service> void findService(Class<T> cl, Class<? extends T> defaultClass) {
    	if(serviceMap.get(cl) == null) {
        	ServiceFinder finder = (ServiceFinder)DIContainerFactory.getDIContainer().getObject(SERVICE_FINDER_KEY);
        	if(finder == null) {
        		finder = getDefaultFinder();
        	}
        	T service = finder.getService(cl, defaultClass);
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
