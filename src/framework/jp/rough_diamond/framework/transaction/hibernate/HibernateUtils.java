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

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Projections;
import org.hibernate.dialect.Dialect;
import org.hibernate.impl.SessionFactoryImpl;

public class HibernateUtils {
    public final static String BOOLEAN_CHAR_T = "Y";
    public final static String BOOLEAN_CHAR_F = "N";
    
	public static int getCountForCriteria(Criteria criteria) {
		Integer ret = (Integer)criteria.setProjection(Projections.rowCount()).uniqueResult();
		criteria.setProjection(null);
		criteria.setResultTransformer(Criteria.ROOT_ENTITY);
		return ret.intValue();
	}
	
    public static Configuration getConfig() {
        return HibernateConnectionManager.getConfig();
    }
    
	public static Session getSession() {
		return HibernateConnectionManager.getCurrentSession();
	}
    
    public static void rebuildSessionFactory() {
        HibernateConnectionManager.rebuildSessionFactory();
    }
    
    public static Dialect getDialect() {
    	Session session = getSession();
    	SessionFactory sf = session.getSessionFactory();
    	if(sf instanceof SessionFactoryImpl) {
    		SessionFactoryImpl sfi = (SessionFactoryImpl)sf;
    		return sfi.getDialect();
    	} else {
    		return Dialect.getDialect();
    	}
    }
}
