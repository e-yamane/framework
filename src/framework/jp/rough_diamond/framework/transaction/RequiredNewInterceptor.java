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

import java.lang.reflect.Modifier;

import org.aopalliance.intercept.MethodInvocation;

public class RequiredNewInterceptor extends TransactionInterceptor {
	public Object invoke(MethodInvocation mi) throws Throwable {
		if((mi.getMethod().getModifiers() & Modifier.SYNCHRONIZED) == Modifier.SYNCHRONIZED) {
			return synchronousInvoke(mi);
		} else {
			return unsynchronousInvoke(mi);
		}
	}

	private Object synchronousInvoke(MethodInvocation mi) throws Throwable {
		synchronized(mi.getThis()) {
			return unsynchronousInvoke(mi);
		}
	}
	
	private Object unsynchronousInvoke(MethodInvocation mi) throws Throwable {
		ConnectionManager cm = ConnectionManager.getConnectionManager();
		Throwable ex = null;
		cm.beginTransaction(mi);
        rollbackOnly.set(Boolean.FALSE);
        TransactionManager.pushTransactionBeginingInterceptor(this);
		Object ret;
		try {
			ret = mi.proceed();
		} catch(Throwable e) {
			ex = e;
			throw e;
		} finally {
			if(ex == null && !isRollbackOnly()) {
				try {
					cm.commit(mi);
				} catch(Exception e) {
					cm.rollback(mi);
					throw e;
				}
			} else {
				cm.rollback(mi);
			}
		}
		return ret;
	}
}

