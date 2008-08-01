/*
 * ====================================================================
 * 
 *  Copyright 2007 Eiji Yamane(e-yamane@rough-diamond.jp)
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
package jp.rough_diamond.commons.service;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import jp.rough_diamond.commons.entity.Numbering;
import jp.rough_diamond.commons.lang.StringUtils;
import jp.rough_diamond.commons.resource.MessagesIncludingException;
import jp.rough_diamond.framework.service.Service;
import jp.rough_diamond.framework.service.ServiceLocator;
import jp.rough_diamond.framework.transaction.TransactionAttribute;
import jp.rough_diamond.framework.transaction.TransactionAttributeType;
import jp.rough_diamond.framework.transaction.VersionUnmuchException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ナンバリング
 * @author $Author: e-yamane@rough-diamond.jp $
 * @date $Date: 2006-02-14 19:22:14 +0900 (轣ｫ, 14 2 2006) $
 *
 */
@SuppressWarnings("unchecked")
abstract public class NumberingService implements Service {
	private final static Log log = LogFactory.getLog(NumberingService.class);
	
    /**
     * キーに対応するナンバーを取得する
     * 直前の値がLong.MAX_VALUE=2^64-1=9223372036854775807であれば、
     * 返却値は１となる
     * @param key   キー
     * @return ナンバー
     */
	@TransactionAttribute(TransactionAttributeType.REQUIRED_NEW)
	public synchronized long getNumber(String key) {
		try {
			return getNumber(key, false);
		} catch(Exception e) {
			log.warn("ナンバー生成時にエラーが発生しました。多重挿入の可能性があるのでも１回実行します。");
			try {
				return getNumber(key, true);
			} catch (Exception e1) {
				throw new RuntimeException(e1);
			}
		}
	}

    protected final static Map<Class, Supplimenter> NUMBERING_ALLOWED_CLASSES;
    static {
        Map<Class, Supplimenter> map = new HashMap<Class, Supplimenter>();
        map.put(String.class, new StringSupplimenter());
        map.put(Long.class, new LongSupplimenter());
        map.put(Integer.class, new IntegerSupplimenter());
        NUMBERING_ALLOWED_CLASSES = Collections.unmodifiableMap(map);
    }

    public static boolean isAllowedNumberingType(Class cl) {
        return NUMBERING_ALLOWED_CLASSES.containsKey(cl);
    }

   protected static interface Supplimenter {
        public Serializable suppliment(long value, int length);
    }
    
    private static class StringSupplimenter implements Supplimenter {
        public Serializable suppliment(long value, int length) {
            if(length > 1) {
                String pattern = StringUtils.repeat("0", length);
                DecimalFormat df = new DecimalFormat(pattern);
                return df.format(value);
            } else {
                return "" + value;
            }
        }
    }
    
    private static class LongSupplimenter implements Supplimenter {
        public Serializable suppliment(long value, int length) {
            return value;
        }
    }
    
    private static class IntegerSupplimenter implements Supplimenter {
        public Serializable suppliment(long value, int length) {
            return (int)(value % Integer.MAX_VALUE);
        }
    }
    
    /**
     * 指定されたクラスに対応するテーブルの主キーで
     * 利用されていない最適なナンバーを返却する
     * その際に利用されるキーはエンティティ名となる（Hibernate依存）
     * @param entityClass
     * @return  ナンバー
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED_NEW)
    abstract public <T> Serializable getNumber(Class<T> entityClass);
    
	private static long getNumber(String key, boolean isLoadOnly) throws VersionUnmuchException, MessagesIncludingException {
		BasicService service = BasicService.getService();
		Numbering numbering = service.findByPK(Numbering.class, key, BasicService.RecordLock.FOR_UPDATE);
		if(numbering == null) {
			if(isLoadOnly) {
				throw new RuntimeException();
			}
			numbering = new Numbering();
			numbering.setId(key);
			numbering.setNextNumber(1L);
			service.insert(numbering);
			return 1L;
		} else {
	        long ret = numbering.getNextNumber();
	        if(ret == Long.MAX_VALUE) {
	            ret = 1;
	        } else {
	            ret++;
	        }
			numbering.setNextNumber(ret);
			service.update(numbering);
			return ret;
		}
//		Session session = HibernateUtils.getSession();
//		Numbering numbering = (isLoadOnly) 
//				? (Numbering)session.load(Numbering.class, key, LockMode.UPGRADE)
//				: (Numbering)session.get(Numbering.class, key, LockMode.UPGRADE);
//		if(numbering == null) {
//			numbering = new Numbering();
//			numbering.setId(key);
//			numbering.setNextNumber(0L);
//		}
//        long ret = numbering.getNextNumber();
//        if(ret == Long.MAX_VALUE) {
//            ret = 1;
//        } else {
//            ret++;
//        }
//		numbering.setNextNumber(ret);
//		session.save(numbering);
//		return ret;
	}
	
    private final static String DEFAULT_BASIC_SERVICE_CLASS_NAME = "jp.rough_diamond.commons.service.hibernate.HibernateNumberingService";
    private final static NumberingService INSTANCE;
    static {
    	NumberingService tmp = null;
    	try {
    		tmp = ServiceLocator.getService(NumberingService.class);
    	} catch(Exception e) {
    		//指定されていない場合の可能性あり。デフォルトのクラスでサービスを生成する
    		Class<? extends NumberingService> cl;
			try {
				cl = (Class<NumberingService>)Class.forName(DEFAULT_BASIC_SERVICE_CLASS_NAME);
				tmp = (NumberingService)ServiceLocator.getService(cl);
			} catch (ClassNotFoundException e1) {
				throw new RuntimeException(e);
			}
    	}
    	INSTANCE = tmp;
    }

    public static NumberingService getService() {
		return INSTANCE;
	}
}
