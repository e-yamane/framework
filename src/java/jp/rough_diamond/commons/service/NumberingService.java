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

import org.apache.commons.collections.map.LRUMap;
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
	
	final int cashSize;
	final CashingStrategy cStrategy;
	
	public NumberingService() {
		this(1);
	}
	
	public NumberingService(int cashSize) {
		this.cashSize = cashSize;
		this.cStrategy = ServiceLocator.getService(NonCashingStrategy.class);
	}
	
    /**
     * キーに対応するナンバーを取得する
     * 直前の値がLong.MAX_VALUE=2^64-1=9223372036854775807であれば、
     * 返却値は１となる
     * @param key   キー
     * @return ナンバー
     */
	public synchronized long getNumber(String key) {
		return cStrategy.getNumber(key);
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
	
    private final static String DEFAULT_NUMBERING_SERVICE_CLASS_NAME = "jp.rough_diamond.commons.service.hibernate.HibernateNumberingService";

    public static NumberingService getService() {
		return ServiceLocator.getService(NumberingService.class, DEFAULT_NUMBERING_SERVICE_CLASS_NAME);
	}
    
    abstract public static class CashingStrategy implements Service {
    	abstract public long getNumber(String key);

		@TransactionAttribute(TransactionAttributeType.REQUIRED_NEW)
		public Info getInfo(String key, boolean isLoadOnly) throws VersionUnmuchException, MessagesIncludingException {
			BasicService service = BasicService.getService();
			Numbering numbering = service.findByPK(Numbering.class, key, BasicService.RecordLock.FOR_UPDATE);
			if(numbering == null) {
				if(isLoadOnly) {
					throw new RuntimeException();
				}
				numbering = new Numbering();
				numbering.setId(key);
				numbering.setNextNumber((long)getCashSize());
				service.insert(numbering);
				Info ret = new Info();
				ret.currentNumber = 1L;
				ret.numbering = numbering;
				return ret;
			} else {
		        long currentNumber = numbering.getNextNumber();
		        long nextValue = currentNumber + getCashSize();
		        //桁あふれしたとき
		        if(nextValue <= 0) {
		        	nextValue = getCashSize();
		        }
		        if(currentNumber == Long.MAX_VALUE) {
		        	currentNumber = 1;
		        } else {
		        	currentNumber++;
		        }
				numbering.setNextNumber(nextValue);
				service.update(numbering);
				Info ret = new Info();
				ret.currentNumber = currentNumber;
				ret.numbering = numbering;
				return ret;
			}
		}

		//for mock
		protected int getCashSize() {
			return NumberingService.getService().cashSize;
		}
		
		//for mock
		protected CashingStrategy getStrategy() {
			return NumberingService.getService().cStrategy;
		}
		
		static class Info {
			long currentNumber;
			Numbering numbering; 
		}
    }
    
    public static class NonCashingStrategy extends CashingStrategy {
		@Override
		public long getNumber(String key) {
			Info info;
			try {
				info = getStrategy().getInfo(key, false);
			} catch(Exception e) {
				log.warn("ナンバー生成時にエラーが発生しました。多重挿入の可能性があるのでも１回実行します。");
				try {
					info = getStrategy().getInfo(key, true);
				} catch (Exception e1) {
					throw new RuntimeException(e1);
				}
			}
			return info.currentNumber;
		}
    }
    
    public static class NumberCashingStrategy extends CashingStrategy {
    	Map<String, Info> map = new LRUMap(1000);
    	
		@Override
		public long getNumber(String key) {
			Info info = map.get(key);
			if(info != null && info.currentNumber < info.numbering.getNextNumber()) {
				return ++info.currentNumber;
			}
			try {
				info = getStrategy().getInfo(key, false);
			} catch(Exception e) {
				log.warn("ナンバー生成時にエラーが発生しました。多重挿入の可能性があるのでも１回実行します。");
				try {
					info = getStrategy().getInfo(key, true);
				} catch (Exception e1) {
					throw new RuntimeException(e1);
				}
			}
			map.put(key, info);
			return info.currentNumber;
		}
    }
}
