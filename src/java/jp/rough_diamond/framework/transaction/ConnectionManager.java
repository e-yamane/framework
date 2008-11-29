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

import java.sql.Connection;
import java.sql.SQLException;

import jp.rough_diamond.commons.di.DIContainer;
import jp.rough_diamond.commons.di.DIContainerFactory;

import org.aopalliance.intercept.MethodInvocation;

abstract public class ConnectionManager {
	abstract protected boolean isTransactionBegining(MethodInvocation mi);
	abstract public Connection getCurrentConnection(MethodInvocation mi);
	abstract public void beginTransaction(MethodInvocation mi);
	abstract public void rollback(MethodInvocation mi);
	abstract public void commit(MethodInvocation mi) throws SQLException;

	public final static String CONNECTION_MANAGER_KEY = "connectionManager";
	
	public static ConnectionManager getConnectionManager() {
		DIContainer container = DIContainerFactory.getDIContainer();
		return (ConnectionManager)container.getObject(CONNECTION_MANAGER_KEY);
	}
}
