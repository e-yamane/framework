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
package jp.rough_diamond.commons.service.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.rough_diamond.framework.transaction.TransactionManager;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

public class BasicServiceInterceptor extends EmptyInterceptor {
	private static final long serialVersionUID = 1L;
	private static ThreadLocal<Boolean> NO_CACHE = new ThreadLocal<Boolean>() {
		@Override
		protected Boolean initialValue() {
			return Boolean.FALSE;
		}
	};

	public static void setNoCache(boolean noCache) {
		NO_CACHE.set(noCache);
	}

	public static boolean isNoCache() {
		return NO_CACHE.get();
	}

	private final static String POST_LOAD_OBJECTS = "postLoadedObjects";

	@SuppressWarnings("unchecked")
	static void startLoad(boolean noCache) {
		setNoCache(noCache);
		Map map = TransactionManager.getTransactionContext();
		Set<Object> set = new LinkedHashSet<Object>();
		map.put(POST_LOAD_OBJECTS, set);
	}
	
	@SuppressWarnings("unchecked")
	static List<Object> popPostLoadedObjects() {
		Map map = TransactionManager.getTransactionContext();
		Set<Object> set = (Set<Object>)map.get(POST_LOAD_OBJECTS);
		if(set == null) {
			return new ArrayList<Object>();
		} else {
			map.remove(POST_LOAD_OBJECTS);
			return new ArrayList<Object>(set);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean onLoad(Object arg0, Serializable arg1, Object[] arg2, String[] arg3, Type[] arg4) {
		Map map = TransactionManager.getTransactionContext();
		Set<Object> set = (Set<Object>)map.get(POST_LOAD_OBJECTS);
		if(set != null) {
			set.add(arg0);
		}
		return super.onLoad(arg0, arg1, arg2, arg3, arg4);
	}
}
