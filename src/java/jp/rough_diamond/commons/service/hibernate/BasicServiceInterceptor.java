/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
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
