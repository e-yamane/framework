/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.service;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.rough_diamond.commons.di.DIContainerFactory;
import jp.rough_diamond.commons.extractor.Condition;
import jp.rough_diamond.commons.extractor.Extractor;
import jp.rough_diamond.commons.extractor.Property;
import jp.rough_diamond.commons.resource.Message;
import jp.rough_diamond.commons.resource.Messages;
import jp.rough_diamond.commons.resource.MessagesIncludingException;
import jp.rough_diamond.commons.resource.ResourceManager;
import jp.rough_diamond.commons.service.annotation.Check;
import jp.rough_diamond.commons.service.annotation.MaxCharLength;
import jp.rough_diamond.commons.service.annotation.MaxLength;
import jp.rough_diamond.commons.service.annotation.NestedComponent;
import jp.rough_diamond.commons.service.annotation.NotNull;
import jp.rough_diamond.commons.service.annotation.Unique;
import jp.rough_diamond.commons.service.annotation.Verifier;
import jp.rough_diamond.framework.service.Service;
import jp.rough_diamond.framework.service.ServiceLocator;
import jp.rough_diamond.framework.transaction.TransactionAttribute;
import jp.rough_diamond.framework.transaction.TransactionAttributeType;
import jp.rough_diamond.framework.transaction.VersionUnmuchException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * DAO基本サービス
 */
@SuppressWarnings("unchecked")
abstract public class BasicService implements Service {
    private final static Log log = LogFactory.getLog(BasicService.class);
    
    public final static String 	BOOLEAN_CHAR_T = "Y";
    public final static String 	BOOLEAN_CHAR_F = "N";

    /**
     * レコードのロックモード
     * @author e-yamane
     */
    public static enum RecordLock {
    	NONE,				//ロックしない
    	FOR_UPDATE,			//排他ロック
    	FOR_UPDATE_NOWAIT,	//排他ロック（待たない）
    }
    
    private final static String DEFAULT_BASIC_SERVICE_CLASS_NAME = "jp.rough_diamond.commons.service.hibernate.HibernateBasicService";
    
    /**
     * Basicサービスを取得する
     * @return  Basicサービス
     */
    public static BasicService getService() {
    	return ServiceLocator.getService(BasicService.class, DEFAULT_BASIC_SERVICE_CLASS_NAME);
    }

    /**
     * 指定された主キーに対応するオブジェクトを取得する
     * 取得したオブジェクトは永続化エンジン（例：Hibernate）がキャッシュするように指示する
     * また、取得したレコードに対するロックは行わない
     * @param <T>	取得するクラスのタイプ
     * @param type	取得するクラスのタイプ
     * @param pk	主キー
     * @return		主キーに対応するオブジェクト。対応するオブジェクトが無い場合はnullを返却する
     */
    public <T> T findByPK(Class<T> type, Serializable pk) {
    	return findByPK(type, pk, false);
    }
    
    /**
     * 指定された主キーに対応するオブジェクトを取得する
     * 取得したオブジェクトは永続化エンジン（例：Hibernate）がキャッシュするように指示する
     * @param <T>	取得するクラスのタイプ
     * @param type	取得するクラスのタイプ
     * @param pk	主キー
     * @param lock	取得オブジェクトのロックモードを指定する
     * @return		主キーに対応するオブジェクト。対応するオブジェクトが無い場合はnullを返却する
     */
    public <T> T findByPK(Class<T> type, Serializable pk, RecordLock lock) {
    	return findByPK(type, pk, false, lock);
    }

    /**
     * 指定された主キーに対応するオブジェクトを取得する
     * 取得したレコードに対するロックは行わない
     * @param <T>		取得するクラスのタイプ
     * @param type		取得するクラスのタイプ
     * @param pk		主キー
     * @param isNoCache	true：永続化エンジン（例：Hibernate）がキャッシュしない false:キャッシュする	
     * @return			主キーに対応するオブジェクト。対応するオブジェクトが無い場合はnullを返却する
     */
    public <T> T findByPK(Class<T> type, Serializable pk, boolean isNoCache) {
    	return findByPK(type, pk, isNoCache, RecordLock.NONE);
    }
    
    /**
     * 指定された主キーに対応するオブジェクトを取得する
     * なお、レコードロック、オブジェクトキャッシュに関しては使用する永続化エンジンによっては
     * 正しく適用されない場合があります。
     * @param <T>		取得するクラスのタイプ
     * @param type		取得するクラスのタイプ
     * @param pk		主キー
     * @param isNoCache	true：永続化エンジン（例：Hibernate）がキャッシュしない false:キャッシュする	
     * @param lock		取得オブジェクトのロックモードを指定する
     * @return			主キーに対応するオブジェクト。対応するオブジェクトが無い場合はnullを返却する
     */
    abstract public <T> T findByPK(Class<T> type, Serializable pk, boolean isNoCache, RecordLock lock);

    /**
     * 検索条件に対応するオブジェクト一覧を取得する
     * 取得したオブジェクトは永続化エンジン（例：Hibernate）がキャッシュするように指示する
     * また、取得したレコードに対するロックは行わない
     * @param <T>		取得するオブジェクトのタイプ
     * @param extractor	検索条件
     * @return			検索条件に対応するオブジェクト一覧。１件も該当するデータが無ければ要素数０のリストを返却する
     */
    public <T> List<T> findByExtractor(Extractor extractor) {
    	return findByExtractor(extractor, false);
    }

    /**
     * 検索条件に対応するオブジェクト一覧を取得する
     * 取得したオブジェクトは永続化エンジン（例：Hibernate）がキャッシュするように指示する
     * @param <T>		取得するオブジェクトのタイプ
     * @param extractor	検索条件
     * @param lock		取得オブジェクトのロックモードを指定する
     * @return			検索条件に対応するオブジェクト一覧。１件も該当するデータが無ければ要素数０のリストを返却する
     */
    public <T> List<T> findByExtractor(Extractor extractor, RecordLock lock) {
    	return findByExtractor(extractor, false, lock);
    }

    /**
     * 検索条件に対応するオブジェクト一覧を取得する
     * 取得したレコードに対するロックは行わない
     * @param <T>		取得するオブジェクトのタイプ
     * @param extractor	検索条件
     * @param isNoCache	true：永続化エンジン（例：Hibernate）がキャッシュしない false:キャッシュする	
     * @return			検索条件に対応するオブジェクト一覧。１件も該当するデータが無ければ要素数０のリストを返却する
     */
    public <T> List<T> findByExtractor(Extractor extractor, boolean isNoCache) {
    	return findByExtractor(getReturnType(extractor), extractor, isNoCache, RecordLock.NONE);
    }
    
    /**
     * 検索条件に対応するオブジェクト一覧を取得する
     * @param <T>		取得するオブジェクトのタイプ
     * @param extractor	検索条件
     * @param isNoCache	true：永続化エンジン（例：Hibernate）がキャッシュしない false:キャッシュする	
     * @param lock		取得オブジェクトのロックモードを指定する
     * @return			検索条件に対応するオブジェクト一覧。１件も該当するデータが無ければ要素数０のリストを返却する
     */
    public <T> List<T> findByExtractor(Extractor extractor, boolean isNoCache, RecordLock lock) {
    	return findByExtractor(getReturnType(extractor), extractor, isNoCache, lock);
    }

    Class getReturnType(Extractor extractor) {
    	if(extractor.returnType != null) {
    		return extractor.returnType;
    	} else if(extractor.getValues().size() != 0) {
    		return Map.class;
    	} else {
    		return extractor.target;
    	}
    }
    
    abstract protected <T> List<T> findByExtractor(Class<T> type, Extractor extractor, boolean isNoCache, RecordLock lock);

    /**
     * 検索条件に対応するオブジェクト一覧と、検索条件に合致する総件数を取得する
     * 取得したオブジェクトは永続化エンジン（例：Hibernate）がキャッシュするように指示する
     * また、取得したレコードに対するロックは行わない
     * @param <T>		取得するオブジェクトのタイプ
     * @param extractor	検索条件
     * @return			検索結果
     */
	public <T> FindResult<T> findByExtractorWithCount(Extractor extractor) {
    	return findByExtractorWithCount(extractor, false);
    }

	/**
     * 検索条件に対応するオブジェクト一覧と、検索条件に合致する総件数を取得する
     * 取得したオブジェクトは永続化エンジン（例：Hibernate）がキャッシュするように指示する
     * @param <T>		取得するオブジェクトのタイプ
     * @param extractor	検索条件
     * @param lock		取得オブジェクトのロックモードを指定する
     * @return			検索結果
	 */
	public <T> FindResult<T> findByExtractorWithCount(Extractor extractor, RecordLock lock) {
    	return findByExtractorWithCount(extractor, false, lock);
    }

	/**
     * 検索条件に対応するオブジェクト一覧と、検索条件に合致する総件数を取得する
     * 取得したレコードに対するロックは行わない
     * @param <T>		取得するオブジェクトのタイプ
     * @param extractor	検索条件
     * @param isNoCache	true：永続化エンジン（例：Hibernate）がキャッシュしない false:キャッシュする	
     * @return			検索結果
	 */
	public <T> FindResult<T> findByExtractorWithCount(Extractor extractor, boolean isNoCache) {
    	return findByExtractorWithCount(extractor, isNoCache, RecordLock.NONE);
    }

	/**
     * 検索条件に対応するオブジェクト一覧と、検索条件に合致する総件数を取得する
     * @param <T>		取得するオブジェクトのタイプ
     * @param extractor	検索条件
     * @param isNoCache	true：永続化エンジン（例：Hibernate）がキャッシュしない false:キャッシュする	
     * @param lock		取得オブジェクトのロックモードを指定する
     * @return			検索結果
	 */
	public <T> FindResult<T> findByExtractorWithCount(Extractor extractor, boolean isNoCache, RecordLock lock) {
    	return findByExtractorWithCount(getReturnType(extractor), extractor, isNoCache, lock);
    }

    protected <T> FindResult<T> findByExtractorWithCount(Class<T> type, Extractor extractor, boolean isNoCache, RecordLock lock) {
    	List<T> list = (extractor.getLimit() == 0) ? new ArrayList<T>() : findByExtractor(type, extractor, isNoCache, lock);
    	long count = getCountByExtractor(extractor);
    	return new FindResult<T>(list, count);
    }
    
	/**
	 * 検索条件に合致する永続オブジェクトの件数を取得する
	 * @param <T>		件数取得対象オブジェクトタイプ
	 * @param extractor	検索条件
	 * @return			検索条件に合致する永続オブジェクトの件数
	 */
	abstract public <T> long getCountByExtractor(Extractor extractor);
	
    /**
     * 指定されたクラスの永続化オブジェクトを全て取得する
     * 取得したオブジェクトは永続化エンジン（例：Hibernate）がキャッシュするように指示する
     * また、取得したレコードに対するロックは行わなず、フェッチサイズは下位ライブラリに依存する
     * @param <T>	取得対象クラスタイプ
     * @param type	取得対象クラスタイプ
     * @return		取得対象クラスの永続オブジェクト一覧。１件も無い場合は要素数０のリストを返却する
     */
    public <T> List<T> findAll(Class<T> type) {
    	return findAll(type, false, Extractor.DEFAULT_FETCH_SIZE, RecordLock.NONE);
    }

    /**
     * 指定されたクラスの永続化オブジェクトを全て取得する
     * 取得したレコードに対するロックは行わないず、フェッチサイズは下位ライブラリに依存する
     * @param <T>		取得対象クラスタイプ
     * @param type		取得対象クラスタイプ
     * @param isNoCache	true：永続化エンジン（例：Hibernate）がキャッシュしない false:キャッシュする	
     * @return			取得対象クラスの永続オブジェクト一覧。１件も無い場合は要素数０のリストを返却する
     */
    public <T> List<T> findAll(Class<T> type, boolean isNoCache) {
    	return findAll(type, isNoCache, Extractor.DEFAULT_FETCH_SIZE, RecordLock.NONE);
    }

    /**
     * 指定されたクラスの永続化オブジェクトを全て取得する
     * 取得したオブジェクトは永続化エンジン（例：Hibernate）がキャッシュするように指示する
     * また、取得したレコードに対するロックは行わない
     * @param <T>		取得対象クラスタイプ
     * @param type		取得対象クラスタイプ
     * @param fetchSize	フェッチサイズ（内部的な振る舞い）
     * @return			取得対象クラスの永続オブジェクト一覧。１件も無い場合は要素数０のリストを返却する
     */
    public <T> List<T> findAll(Class<T> type, int fetchSize) {
    	return findAll(type, false, fetchSize, RecordLock.NONE);
    }
    
    /**
     * 指定されたクラスの永続化オブジェクトを全て取得する
     * 取得したレコードに対するロックは行わないず、フェッチサイズは下位ライブラリに依存する
     * @param <T>		取得対象クラスタイプ
     * @param type		取得対象クラスタイプ
     * @param lock		取得オブジェクトのロックモードを指定する
     * @return			取得対象クラスの永続オブジェクト一覧。１件も無い場合は要素数０のリストを返却する
     */
    public <T> List<T> findAll(Class<T> type, RecordLock lock) {
    	return findAll(type, false, Extractor.DEFAULT_FETCH_SIZE, lock);
    }

    /**
     * 指定されたクラスの永続化オブジェクトを全て取得する
     * 取得したレコードに対するロックは行わない
     * @param <T>		取得対象クラスタイプ
     * @param type		取得対象クラスタイプ
     * @param isNoCache	true：永続化エンジン（例：Hibernate）がキャッシュしない false:キャッシュする	
     * @param fetchSize	フェッチサイズ（内部的な振る舞い）
     * @return			取得対象クラスの永続オブジェクト一覧。１件も無い場合は要素数０のリストを返却する
     */
    public <T> List<T> findAll(Class<T> type, boolean isNoCache, int fetchSize) {
    	return findAll(type, isNoCache, fetchSize, RecordLock.NONE);
    }
    
    /**
     * 指定されたクラスの永続化オブジェクトを全て取得する
     * 取得したレコードに対するロックは行わない
     * @param <T>		取得対象クラスタイプ
     * @param type		取得対象クラスタイプ
     * @param isNoCache	true：永続化エンジン（例：Hibernate）がキャッシュしない false:キャッシュする	
     * @param lock		取得オブジェクトのロックモードを指定する
     * @return			取得対象クラスの永続オブジェクト一覧。１件も無い場合は要素数０のリストを返却する
     */
    public <T> List<T> findAll(Class<T> type, boolean isNoCache, RecordLock lock) {
    	return findAll(type, isNoCache, Extractor.DEFAULT_FETCH_SIZE, lock);
    }
    
    /**
     * 指定されたクラスの永続化オブジェクトを全て取得する
     * 取得したレコードに対するロックは行わない
     * @param <T>		取得対象クラスタイプ
     * @param type		取得対象クラスタイプ
     * @param fetchSize	フェッチサイズ（内部的な振る舞い）
     * @param lock		取得オブジェクトのロックモードを指定する
     * @return			取得対象クラスの永続オブジェクト一覧。１件も無い場合は要素数０のリストを返却する
     */
    public <T> List<T> findAll(Class<T> type, int fetchSize, RecordLock lock) {
    	return findAll(type, false, fetchSize, lock);
    }
    
    /**
     * 指定されたクラスの永続化オブジェクトを全て取得する
     * 取得したレコードに対するロックは行わない
     * @param <T>		取得対象クラスタイプ
     * @param type		取得対象クラスタイプ
     * @param isNoCache	true：永続化エンジン（例：Hibernate）がキャッシュしない false:キャッシュする	
     * @param fetchSize	フェッチサイズ（内部的な振る舞い）
     * @param lock		取得オブジェクトのロックモードを指定する
     * @return			取得対象クラスの永続オブジェクト一覧。１件も無い場合は要素数０のリストを返却する
     */
    public <T> List<T> findAll(Class<T> type, boolean isNoCache, int fetchSize, RecordLock lock) {
		Extractor ex = new Extractor(type);
		ex.setFetchSize(fetchSize);
		return findByExtractor(type, ex, isNoCache, lock);
    }
    
    /**
     * 指定されたオブジェクト（群）を永続化(INSERT)する
     * 主キーがStringもしくはNumberを継承するオブジェクトでかつnullの場合は主キーは自動的に
     * ユニークな値が採番される
     * @param <T>		永続化オブジェクトのタイプ
     * @param objects	永続化オブジェクト群
     * @throws MessagesIncludingException	検証例外（１つ以上のプロパティの検証に失敗）
     */
    abstract public <T> void insert(T... objects) throws MessagesIncludingException;

    /**
     * 指定されたオブジェクト（群）を永続化（UPDATE）する
     * @param <T>		永続化オブジェクトのタイプ
     * @param objects	永続化オブジェクト群
     * @throws MessagesIncludingException	検証例外（１つ以上のプロパティの検証に失敗）
     */
    abstract public <T> void update(T... objects) throws VersionUnmuchException, MessagesIncludingException;

    /**
     * 検索条件に合致するオブジェクト群を削除する
     * @param extractor						削除オブジェクトの検索条件
     * @throws VersionUnmuchException		論理削除時の楽観的ロッキングエラー
     * @throws MessagesIncludingException	論理削除時の検証例外
     */
    public void deleteByExtractor(Extractor extractor) throws VersionUnmuchException, MessagesIncludingException {
    	List list = findByExtractor(extractor, true);
    	delete(list.toArray());
    }
    
    /**
     * 指定したクラスの全永続オブジェクトを削除する
     * @param <T>	削除対象永続化タイプ
     * @param cl	削除対象永続化タイプ
     */
    public <T> void deleteAll(Class<T> cl) {
    	List<T> list = findAll(cl);
    	try {
			delete(list.toArray(new Object[list.size()]));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }

    /**
     * 指定されたオブジェクト群を削除する
     * @param objects						削除対象オブジェクト
     * @throws VersionUnmuchException		論理削除時の楽観的ロッキングエラー
     * @throws MessagesIncludingException	論理削除時の検証例外
     */
    abstract public void delete(Object... objects) throws VersionUnmuchException, MessagesIncludingException;

    /**
     * 指定されたクラスの指定された主キーに一致するオブジェクトを削除する
     * @param <T>	削除対象永続化タイプ
     * @param type	削除対象永続化タイプ
     * @param pk	削除対象オブジェクトの主キー
     * @throws VersionUnmuchException		論理削除時の楽観的ロッキングエラー
     * @throws MessagesIncludingException	論理削除時の検証例外
     */
    public <T> void deleteByPK(Class<T> type, Serializable pk) throws VersionUnmuchException, MessagesIncludingException {
        T o = findByPK(type, pk, true);
        if(o != null) {
            delete(o);
        }
    }

    /**
     * オブジェクトの永続化可否を検証を行う
     * @param o		検証対象オブジェクト
     * @param when	永続化条件（追加/更新）
     * @return		検証失敗した場合の原因メッセージ群
     */
    public Messages validate(Object o, WhenVerifier when) {
        Messages ret = new Messages();
    	try {
	    	ret.add(unitPropertyValidate(o, when));
            ret.add(customValidate(o, when, ret.hasError()));
    		return ret;
    	} catch(Exception ex) {
    		throw new RuntimeException(ex);
    	}
    }
    
    /**
     * 指定されていたオブジェクトがDAOにキャッシュされている場合に削除する
     * @param o
     */
    @TransactionAttribute(TransactionAttributeType.NOP)
    abstract public void clearCache(Object o);
    
    protected void fireEvent(CallbackEventType eventType, List objects) throws VersionUnmuchException, MessagesIncludingException {
        if(objects.size() == 0) {
            return;
        }
        Map<Class, SortedSet<CallbackEventListener>> map = new HashMap<Class, SortedSet<CallbackEventListener>>();
        for(Object o : objects) {
            if(o == null) {
                continue;
            }
            SortedSet<CallbackEventListener> set = map.get(o.getClass());
            if(set == null) {
            	set = getEventListener(o.getClass(), eventType);
            	map.put(o.getClass(), set);
            }
        }
        if(map.size() == 0) {
            return;
        }
        try {
            for(Object o : objects) {
                SortedSet<CallbackEventListener> set = map.get(o.getClass());
                for(CallbackEventListener listener : set) {
                	listener.callback(o, eventType);
                }
                fireEvent(eventType, getNestedComponents(o));
            }
        } catch(InvocationTargetException ite) {
            Throwable t = ite.getTargetException();
            if(t instanceof VersionUnmuchException) {
                throw (VersionUnmuchException)t;
            } else if(t instanceof MessagesIncludingException) {
            	throw (MessagesIncludingException)t;
            } else {
                throw new RuntimeException(t);
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private List getNestedComponents(Object o) {
		try {
	    	List<PropertyDescriptor> list = getNestedComponentGetters(o.getClass());
	    	List ret = new ArrayList();
	    	for(PropertyDescriptor pd : list) {
	    		Method m = pd.getReadMethod();
	    		Object val;
					val = m.invoke(o);
	    		if(val != null) {
	    			ret.add(val);
	    		}
	    	}
	    	return ret;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
    
    private List<PropertyDescriptor> getNestedComponentGetters(Class cl) {
    	List<PropertyDescriptor> list = nestedComponentGetterMap.get(cl);
    	if(list == null) {
    		list = makeNestedComponentGetters(cl);
    	}
    	return list;
    }
    
    private List<PropertyDescriptor> makeNestedComponentGetters(Class cl) {
    	List<PropertyDescriptor> list = new ArrayList<PropertyDescriptor>();
    	PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(cl);
    	for(PropertyDescriptor pd : pds) {
    		Method m = pd.getReadMethod();
    		if(m == null) {
    			continue;
    		}
    		NestedComponent nc = m.getAnnotation(NestedComponent.class);
    		if(nc != null) {
    			list.add(pd);
    		}
    	}
    	nestedComponentGetterMap.put(cl, list);
    	return list;
	}

	private Map<Class, List<PropertyDescriptor>> nestedComponentGetterMap =
    		new HashMap<Class, List<PropertyDescriptor>>();
    
    private Map<Class, Map<CallbackEventType, SortedSet<CallbackEventListener>>> eventListenrsCache = 
                            new HashMap<Class, Map<CallbackEventType, SortedSet<CallbackEventListener>>>();
    
    SortedSet<CallbackEventListener> getEventListener(Class cl, CallbackEventType eventType) {
        Map<CallbackEventType, SortedSet<CallbackEventListener>> map = eventListenrsCache.get(cl);
        if(map == null) {
            map = new HashMap<CallbackEventType, SortedSet<CallbackEventListener>>();
            eventListenrsCache.put(cl, map);
        }
        SortedSet<CallbackEventListener> set = map.get(eventType);
        if(set == null) {
            set = findEventListener(cl, eventType);
            map.put(eventType, set);
        }
        return set;
    }
    
    SortedSet<CallbackEventListener> findEventListener(Class cl, CallbackEventType eventType) {
        Class annotationType = eventType.getAnnotation();
        SortedSet<CallbackEventListener> set = new TreeSet<CallbackEventListener>();
        Method[] methods = cl.getMethods();
        for(Method m : methods) {
        	if(CallbackEventListener.isEventListener(null, cl, m, annotationType)) {
                set.add(new CallbackEventListener.SelfEventListener(m, annotationType));
        	}
        }
        List<?> listeners = getPersistenceEventListeners();
        for(Object listener : listeners) {
        	Class listenerType = listener.getClass();
        	Method[] listenerMethods = listenerType.getMethods();
        	for(Method m : listenerMethods) {
            	if(CallbackEventListener.isEventListener(listener, cl, m, annotationType)) {
                    set.add(new CallbackEventListener.EventAdapter(listener, m, annotationType));
            	}
        	}
        }
        return set;
    }
    
    final static String PERSISTENCE_EVENT_LISTENERS = "persistenceEventListeners";
    List<?> getPersistenceEventListeners() {
    	List list = (List<?>)DIContainerFactory.getDIContainer().getObject(PERSISTENCE_EVENT_LISTENERS);
    	return (list == null) ? new ArrayList<Object>() : list;
    }
    
    private Messages unitPropertyValidate(Object o, WhenVerifier when) throws Exception {
    	Messages ret = new Messages();
    	Class cl = o.getClass();
    	PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(cl);
    	for(PropertyDescriptor pd : pds) {
    		Method m = pd.getReadMethod();
    		if(m != null) {
	    		NotNull nn = m.getAnnotation(NotNull.class);
	    		if(nn != null) {
	    			Object val = m.invoke(o);
	    			if(val == null) {
                        log.debug("必須属性エラー:" + nn.property());
	    				ret.add(nn.property(), new Message("errors.required", ResourceManager.getResource().getString(nn.property())));
	    				//必須エラーなので長さのチェックは不要
	    				continue;
	    			} else if(val instanceof String) {
	    				if(getLength((String)val) == 0) {
                            log.debug("必須属性エラー:" + nn.property());
		    				ret.add(nn.property(), new Message("errors.required", ResourceManager.getResource().getString(nn.property())));
		    				//必須エラーなので長さのチェックは不要
		    				continue;
	    				}
	    			}
	    		}
	    		MaxCharLength mcl = m.getAnnotation(MaxCharLength.class);
	    		if(mcl != null) {
	    			Object val = m.invoke(o);
	    			if(val != null && val.toString().length() > mcl.length()) {
                        log.debug("最大文字長超過エラー:" + mcl.property());
	    				ret.add(mcl.property(), new Message("errors.maxcharlength", ResourceManager.getResource().getString(mcl.property()), "" + mcl.length()));
	    				//文字列長超過の場合はバイト数のチェックは行いません
	    				continue;
	    			}
	    		}
	    		MaxLength ml = m.getAnnotation(MaxLength.class);
	    		if(ml != null) {
	    			Object val = m.invoke(o);
	    			if(getLength(val) > ml.length()) {
                        log.debug("最大長超過エラー:" + ml.property());
	    				ret.add(ml.property(), new Message("errors.maxlength", ResourceManager.getResource().getString(ml.property()), "" + ml.length()));
	    			}
	    		}
    		}
    	}
		List<PropertyDescriptor> list = getNestedComponentGetters(cl);
		for(PropertyDescriptor pd : list) {
			Method m = pd.getReadMethod();
			Object val = m.invoke(o);
    		if(val != null) {
    			Messages tmp = unitPropertyValidate(val, when);
    			if(!tmp.hasError()) {
    				continue;
    			}
        		NestedComponent nc = m.getAnnotation(NestedComponent.class);
    			for(String property : tmp.getProperties()) {
    				List<Message> tmpMsgs = tmp.get(property);
    				property = property.replaceAll("^[^\\.]+\\.", nc.property() + ".");
    				for(Message msg : tmpMsgs) {
    					ret.add(property, msg);
    				}
    			}
    		}
		}
    	return ret;
    }
    
    private Messages customValidate(Object o, WhenVerifier when, boolean hasError) throws Exception {
    	Messages ret = new Messages();
		Set<Method> set = getVerifier(o, when);
		for(Method m : set) {
			Verifier v = m.getAnnotation(Verifier.class);
			if(!v.isForceExec() && hasError) {
				return ret;
			}
			Object retTmp;
			if(m.getParameterTypes().length == 0) {
				retTmp = m.invoke(o); 
			} else {
				retTmp = m.invoke(o, when);
			}
			if(retTmp != null) {
				ret.add((Messages)retTmp);
			}
            hasError = ret.hasError();
		}
		List list = getNestedComponents(o);
		for(Object nestedComponent : list) {
			customValidate(nestedComponent, when, hasError);
		}
		return ret;
	}

    Map<Class, List<Unique>> uniqueMap = new HashMap<Class, List<Unique>>(); 
	private List<Unique> findUnique(Class cl) {
		List<Unique> ret = uniqueMap.get(cl);
		if(ret != null) {
			return ret;
		}
		ret = new ArrayList<Unique>();
		Unique u = (Unique)cl.getAnnotation(Unique.class);
		if(u != null) {
			ret.add(u);
		}
		Class parent = cl.getSuperclass();
		if(parent != null) {
			ret.addAll(findUnique(parent));
			uniqueMap.put(cl, ret);
		}
		return ret;
    }

	/**
	 * 永続対象オブジェクトのユニーク性を検証する
	 * @param o		検証対象オブジェクト
     * @param when	永続化条件（追加/更新）
     * @return		検証失敗した場合の原因メッセージ群
	 */
    public Messages checkUnique(Object o, WhenVerifier when) {
    	List<Unique> uniqueList = findUnique(o.getClass());
    	Messages ret = new Messages();
    	if(uniqueList.size() == 0) {
    		return ret;
    	}
        for(Unique u : uniqueList) {
	    	Messages msgs = checkUnique(o, when, u);
	    	ret.add(msgs);
        }
    	return ret;
	}
    
	protected Messages checkUnique(Object o, WhenVerifier when, Unique u) {
		Messages ret = new Messages();
		for(Check check : u.groups()) {
			List list = getMutchingObjects(o, check);
			if(list.size() == 0) {
				continue;	//サイズ０なので無条件にＯＫ
			}
			if(when == WhenVerifier.INSERT) {
				//登録時に１件以上あるので無条件にエラー
				ret.add(makeUniqueErrorMessage(o, u, check));
			} else {
				//更新時
				if(list.size() > 1) {
					ret.add(makeUniqueErrorMessage(o, u, check));
				} else if(!compareUniqueObject(o, list.get(0))){
					ret.add(makeUniqueErrorMessage(o, u, check));
				}
			}
		}
		return ret;
	}

	protected boolean compareUniqueObject(Object target, Object org) {
		return target.equals(org);
	}
	
	protected static Messages makeUniqueErrorMessage(Object o, Unique u, Check c) {
		Messages ret = new Messages();
		String targetProperty = u.entity() + "." + c.properties()[0];
		String[] strArray = c.properties()[0].split("\\.");
		if(strArray.length == 1) {
			ret.add(targetProperty, new Message("errors.duplicate", 
						ResourceManager.getResource().getString(targetProperty)));
		} else {
			int i = 0;
			Object base = o;
			for( ; i < strArray.length - 1 ; i++) {
				base = jp.rough_diamond.commons.util.PropertyUtils.getProperty(base, strArray[0]);
			}
			ret.add(targetProperty, new Message("errors.duplicate",
					ResourceManager.getResource().getString(base.getClass().getSimpleName() + "." + strArray[i])));
		}
		return ret;
	}

	protected Extractor getMutchingExtractor(Object o, Check check) {
		Extractor ex = new Extractor(o.getClass());
		for(String property : check.properties()) {
			Object value = jp.rough_diamond.commons.util.PropertyUtils.getProperty(o, property);
			if(value == null) {
				ex.add(Condition.isNull(new Property(property)));
			} else {
				ex.add(Condition.eq(new Property(property), value));
			}
		}		
		return ex;
	}
	
	protected List getMutchingObjects(Object o, Check check) {
		Extractor ex = getMutchingExtractor(o, check);
		//XXX HibernateBasicServiceを使用するとロックが走らない・・・でも実害ない気がしている。。。
		return findByExtractor(ex, RecordLock.FOR_UPDATE);
	}
	
	private int getLength(Object target) throws Exception {
    	if(target == null) {
    		return 0;
    	} else if (target instanceof String){
    		String charset = (String)DIContainerFactory.getDIContainer().getObject("databaseCharset");
    		byte[] array = ((String)target).getBytes(charset);
    		return array.length;
    	} else if(target instanceof Integer) {
    	    return getLength(target.toString());
        } else {
            throw new RuntimeException();
        }
    }
    
	private Map<Class, SortedSet<Method>> verifierMap = new HashMap<Class, SortedSet<Method>>();
	
	SortedSet<Method> getVerifier(Object o, WhenVerifier when) {
		SortedSet<Method> tmp = getVerifier(o);
		SortedSet<Method> ret = new TreeSet<Method>(VerifierSorter.INSTANCE);
		for(Method m : tmp) {
			Verifier v = m.getAnnotation(Verifier.class);
			for(WhenVerifier w : v.when()) {
				if(w == when) {
					ret.add(m);
					break;
				}
			}
		}
		return ret;
	}
	
	SortedSet<Method> getVerifier(Object o) {
		Class cl = o.getClass();
		SortedSet<Method> ret = verifierMap.get(cl);
		if(ret == null) {
			SortedSet<Method> tmp = findVerifier(cl);
			verifierMap.put(cl, tmp);
			ret = tmp;
		}
		return ret;
	}
	
    private SortedSet<Method> findVerifier(Class cl) {
    	SortedSet<Method> ret = new TreeSet<Method>(VerifierSorter.INSTANCE);
    	Method[] methods = cl.getMethods();
    	for(Method m : methods) {
    		Verifier v = m.getAnnotation(Verifier.class);
    		if(v != null && verifyVerifyMethod(m)) {
    			ret.add(m);
    		}
    	}
    	return ret;
	}

    private boolean verifyVerifyMethod(Method m) {
    	Class returnType = m.getReturnType();
    	if(returnType == Void.TYPE) {
    		log.warn("検証メソッド" + m.toString() + "は戻り値を返却しません。");
    	} else if(!Messages.class.isAssignableFrom(returnType)) {
    		log.warn("検証メソッド" + m.toString() + "は適切な返却値を持たないため検証メソッドとは認識しません。");
    		//例外あげるべきかなぁ・・・
    		return false;
    	}
    	Class[] paramTypes = m.getParameterTypes();
    	if(paramTypes.length > 1) {
    		log.warn("検証メソッド" + m.toString() + "は適切なパラメタではないため検証メソッドとは認識しません。");
    		return false;
    	} else if(paramTypes.length == 1 && !WhenVerifier.class.isAssignableFrom(paramTypes[0])) {
    		log.warn("検証メソッド" + m.toString() + "は適切なパラメタではないため検証メソッドとは認識しません。");
    		return false;
    	}
		return true;
	}
}
