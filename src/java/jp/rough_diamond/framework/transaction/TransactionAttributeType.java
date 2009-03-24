/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.framework.transaction;

import java.util.EnumMap;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public enum TransactionAttributeType {
	REQUIRED, REQUIRED_NEW, NOP;

	private final static EnumMap<TransactionAttributeType, MethodInterceptor> strategy;
	static {
		strategy = new EnumMap<TransactionAttributeType, MethodInterceptor>(TransactionAttributeType.class);
		strategy.put(REQUIRED, new RequiredInterceptor());
		strategy.put(REQUIRED_NEW, new RequiredNewInterceptor());
		strategy.put(NOP, new NopInterceptor());
	}

	public Object doIt(MethodInvocation mi) throws Exception {
		try {
			return strategy.get(this).invoke(mi);
		} catch (Throwable e) {
			if(e instanceof Error) {
				throw (Error)e;
			} else {
				throw (Exception)e;
			}
		}
	}
}
