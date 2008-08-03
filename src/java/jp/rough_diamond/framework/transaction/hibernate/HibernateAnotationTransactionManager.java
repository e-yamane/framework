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
package jp.rough_diamond.framework.transaction.hibernate;

import jp.rough_diamond.framework.transaction.AnnotationTransactionManager;
import jp.rough_diamond.framework.transaction.VersionUnmuchException;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.StaleObjectStateException;

public class HibernateAnotationTransactionManager extends AnnotationTransactionManager {
	private final static Log log = LogFactory.getLog(HibernateAnotationTransactionManager.class);
	
	@Override
	@SuppressWarnings("unchecked")
	public Object invoke(MethodInvocation mi) throws Throwable {
		try {
			return super.invoke(mi);
		} catch(StaleObjectStateException e) {
			Class[] exceptionTypes = mi.getMethod().getExceptionTypes();
			for(Class exceptionType : exceptionTypes) {
				if(VersionUnmuchException.class.isAssignableFrom(exceptionType)) {
					log.debug(e.getMessage(), e);
					throw new VersionUnmuchException(e.getMessage());
				}
			}
			throw e;
		}
	}
}
