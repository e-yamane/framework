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
