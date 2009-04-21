/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.service.hibernate;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.metadata.ClassMetadata;

import jp.rough_diamond.commons.extractor.Extractor;
import jp.rough_diamond.commons.resource.Messages;
import jp.rough_diamond.commons.resource.MessagesIncludingException;
import jp.rough_diamond.commons.service.BasicService;
import jp.rough_diamond.commons.service.CallbackEventType;
import jp.rough_diamond.commons.service.FindResult;
import jp.rough_diamond.commons.service.NumberingService;
import jp.rough_diamond.commons.service.RelationalChecker;
import jp.rough_diamond.commons.service.WhenVerifier;
import jp.rough_diamond.commons.service.annotation.Check;
import jp.rough_diamond.framework.transaction.VersionUnmuchException;
import jp.rough_diamond.framework.transaction.hibernate.HibernateConnectionManager;
import jp.rough_diamond.framework.transaction.hibernate.HibernateUtils;

public class HibernateBasicService extends BasicService {
    private final static Log log = LogFactory.getLog(HibernateBasicService.class);

    private LockMode getLockMode(RecordLock lock) {
    	switch (lock) {
		case NONE:
			return LockMode.NONE;
		case FOR_UPDATE:
			return LockMode.UPGRADE;
		case FOR_UPDATE_NOWAIT:
			return LockMode.UPGRADE_NOWAIT;
		default:
			throw new RuntimeException();
		}
    }

	@Override
    @SuppressWarnings("unchecked")
    public <T> T findByPK(Class<T> type, Serializable pk, boolean isNoCache, RecordLock lock) {
    	BasicServiceInterceptor.startLoad(isNoCache);
        try {
            T ret = (T)HibernateUtils.getSession().get(type, pk, getLockMode(lock));
            if(ret != null) {
                List<Object> loadedObjects = BasicServiceInterceptor.popPostLoadedObjects();
//                fireEvent(CallbackEventType.POST_LOAD, Arrays.asList(new Object[]{ret}));
                fireEvent(CallbackEventType.POST_LOAD, loadedObjects);
                if(isNoCache) {
    	            Session session = HibernateUtils.getSession();
    	            for(Object o : loadedObjects) {
    	            	session.evict(o);
    	            }
                }
            }
            return ret;
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
        	BasicServiceInterceptor.setNoCache(false);
        	BasicServiceInterceptor.popPostLoadedObjects();
        }
    }

	@Override
    @SuppressWarnings("unchecked")
    protected <T> List<T> findByExtractor(Class<T> type, Extractor extractor, boolean isNoCache, RecordLock lock) {
    	BasicServiceInterceptor.startLoad(isNoCache);
        try {
            List<T> list;
//            if(LogicalDeleteEntity.class.isAssignableFrom(type)) {
//                extractor.add(Condition.eq("deletedInDB", HibernateUtils.BOOLEAN_CHAR_F));
//            }
            Query query = Extractor2HQL.extractor2Query(extractor, getLockMode(lock));
            if(extractor.getValues().size() != 0) {
                //厳密にエンティティはロードされていないのでイベントをファイアしてはいけないのでリターン
                List<Object> tmp = query.list();
                list = (List<T>) Extractor2HQL.makeMap(extractor, tmp);
            } else {
//                list = query.list();
            	list = makeList(type, query.list());
            }
            List<Object> loadedObjects = BasicServiceInterceptor.popPostLoadedObjects();
            fireEvent(CallbackEventType.POST_LOAD, loadedObjects);
            if(isNoCache) {
	            Session session = HibernateUtils.getSession();
	            for(Object o : loadedObjects) {
	            	session.evict(o);
	            }
            }
            return list;
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
        	BasicServiceInterceptor.setNoCache(false);
        	BasicServiceInterceptor.popPostLoadedObjects();
        }
    }

	@Override
    @SuppressWarnings("unchecked")
    protected <T> FindResult<T> findByExtractorWithCount(Class<T> type, Extractor extractor, boolean isNoCache, RecordLock lock) {
    	BasicServiceInterceptor.startLoad(isNoCache);
        try {
//            if(LogicalDeleteEntity.class.isAssignableFrom(type)) {
//                extractor.add(Condition.eq("deletedInDB", HibernateUtils.BOOLEAN_CHAR_F));
//            }
            List<T> list;
            long count;
            if(extractor.getLimit() != 0) {
	            Query query = Extractor2HQL.extractor2Query(extractor, getLockMode(lock));
	            if(extractor.getValues().size() != 0) {
	                List<Object> tmp = query.list();
	                list = (List<T>) Extractor2HQL.makeMap(extractor, tmp);
	            } else {
//	            	list = query.list();
	            	list = makeList(type, query.list());
	            }
	            List<Object> loadedObjects = BasicServiceInterceptor.popPostLoadedObjects();
	            fireEvent(CallbackEventType.POST_LOAD, loadedObjects);
	            if(isNoCache) {
		            Session session = HibernateUtils.getSession();
		            for(Object o : loadedObjects) {
		            	session.evict(o);
		            }
	            }
            } else {
            	list = new ArrayList<T>();
            }
            Query countQuery = Extractor2HQL.extractor2CountQuery(extractor);
            Number n = (Number)countQuery.list().get(0);
            count = n.longValue();
            FindResult<T> ret = new FindResult<T>(list, count);
            return ret;
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
        	BasicServiceInterceptor.setNoCache(false);
        	BasicServiceInterceptor.popPostLoadedObjects();
        }
    }

	@Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findAll(Class<T> type, boolean isNoCache, int fetchSize) {
    	BasicServiceInterceptor.setNoCache(isNoCache);
        try {
            Criteria criteria = HibernateUtils.getSession().createCriteria(type);
//            if(LogicalDeleteEntity.class.isAssignableFrom(type)) {
//                criteria.add(Restrictions.eq("deletedInDB", HibernateUtils.BOOLEAN_CHAR_F));
//            }
            if(fetchSize != Extractor.DEFAULT_FETCH_SIZE) {
            	criteria.setFetchSize(fetchSize);
            }
            List<T> list = criteria.list();
            fireEvent(CallbackEventType.POST_LOAD, list);
            return list;
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
        	BasicServiceInterceptor.setNoCache(false);
        }
    }

    @SuppressWarnings("unchecked")
	private <T> List<T> makeList(Class<T> cl, List list) {
   		List<T> ret = new ArrayList<T>();
   		Iterator iterator = list.iterator();
   		while(iterator.hasNext()) {
   			Object o = iterator.next();
   			if(cl.isAssignableFrom(o.getClass())) {
   				ret.add((T)o);
   			} else {
   				ret.add((T)Array.get(o, 0));
   			}
   			iterator.remove();
   		}
		return ret;
   	}

	@Override
    @SuppressWarnings("unchecked")
    public <T> void insert(T... objects) throws MessagesIncludingException {
        try {
            SessionFactory sf = HibernateUtils.getSession().getSessionFactory();
            for(Object o : objects) {
//                if(o instanceof LogicalDeleteEntity) {
//                    LogicalDeleteEntity lde = (LogicalDeleteEntity)o;
//                    lde.setDeleted(false);
//                }
                ClassMetadata cm = sf.getClassMetadata(o.getClass());
                if(cm.getIdentifier(o, EntityMode.POJO) == null &&
                        NumberingService.isAllowedNumberingType(cm.getIdentifierType().getReturnedClass())) {
                    NumberingService ns = NumberingService.getService();
                    cm.setIdentifier(o, ns.getNumber(getSuperMappedClass(o.getClass())), EntityMode.POJO);
                }
            }
            Messages msgs = new Messages();
            for(Object o : objects) {
                msgs.add(validate(o, WhenVerifier.INSERT));
            }
            if(!msgs.hasError()) {
                for(Object o : objects) {
                    try {
	                    List<Object> list = new ArrayList<Object>();
	                    list.add(o);
	               		fireEvent(CallbackEventType.PRE_PERSIST, list);
	                    msgs.add(checkUnique(o, WhenVerifier.INSERT));
                    } catch(MessagesIncludingException e) {
                    	msgs.add(e.getMessages());
                    }
                }
            }
        	if(!msgs.hasError()) {
                for(Object o : objects) {
            		HibernateUtils.getSession().save(o);
                    List<Object> list = new ArrayList<Object>();
                    list.add(o);
                    fireEvent(CallbackEventType.POST_PERSIST, list);
                }
        	}
        	if(msgs.hasError()) {
        		throw new MessagesIncludingException(msgs);
        	}
        } catch(MessagesIncludingException e) {
        	throw e;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

	@SuppressWarnings("unchecked")
	private Class getSuperMappedClass(Class cl) {
        PersistentClass pc = HibernateConnectionManager.getConfig().getClassMapping(cl.getName());
        while(pc.isInherited()) {
        	pc = pc.getSuperclass();
        }
        return pc.getMappedClass();
    }

	@Override
    public <T> void update(T... objects) throws VersionUnmuchException, MessagesIncludingException {
        boolean isLogicalDelete = isLogicalDelete(new Throwable());
        Messages msgs = new Messages();
        if(!isLogicalDelete) {
            for(Object o : objects) {
                msgs.add(validate(o, WhenVerifier.UPDATE));
            }
        }
        //論理削除でもユニークチェックはいるねん！！
        if(!msgs.hasError()) {
            for(Object o : objects) {
                try {
                    if(!isLogicalDelete) {
	                    List<Object> list = new ArrayList<Object>();
	                    list.add(o);
	               		fireEvent(CallbackEventType.PRE_UPDATE, list);
                    }
                    msgs.add(checkUnique(o, WhenVerifier.UPDATE));
                } catch(MessagesIncludingException e) {
                	msgs.add(e.getMessages());
                }
            }
        }
        if(!msgs.hasError()) {
            for(Object o : objects) {
                HibernateUtils.getSession().update(o);
                if(!isLogicalDelete) {
                    List<Object> list = new ArrayList<Object>();
                    list.add(o);
                    fireEvent(CallbackEventType.POST_UPDATE, list);
                }
            }
    	}
        if(msgs.hasError()) {
        	throw new MessagesIncludingException(msgs);
        }
    }

    private boolean isLogicalDelete(Throwable t) {
        StackTraceElement el = t.getStackTrace()[1];
        if(el.getClassName().endsWith(".BasicService") &&
                el.getMethodName().equals("delete")) {
            return true;
        } else {
            return false;
        }
    }

	@Override
    @SuppressWarnings("unchecked")
    public void deleteAll(Class cl) {
        Session session = HibernateUtils.getSession();
        SessionFactory sf = session.getSessionFactory();
        ClassMetadata cm = sf.getClassMetadata(cl);
        String hql = "delete from " + cm.getEntityName();
        log.debug(hql);
        Query query = session.createQuery(hql);
        int ret = query.executeUpdate();
        log.debug("削除件数：" + ret);
    }

    @Override
    public void delete(Object... objects) throws VersionUnmuchException, MessagesIncludingException {
        Messages msgs = new Messages();
        RelationalChecker checker = RelationalChecker.getRerationalChecker();
        SessionFactory sf = HibernateUtils.getSession().getSessionFactory();
        for(Object o : objects) {
            try {
                checker.allowRemove(o);
                ClassMetadata cm = sf.getClassMetadata(o.getClass());
                if(cm.getIdentifier(o, EntityMode.POJO) == null) {
                    continue;
                }
//                if(o instanceof LogicalDeleteEntity) {
//                    LogicalDeleteEntity lde = (LogicalDeleteEntity)o;
//                    List<LogicalDeleteEntity> list = new ArrayList<LogicalDeleteEntity>();
//                    list.add(lde);
//                    lde.setDeleted(true);
//                    fireEvent(CallbackEventType.PRE_REMOVE, list);
//                    msgs.add(update(o));
//                    fireEvent(CallbackEventType.POST_REMOVE, list);
//                } else {
                    List<Object> list = new ArrayList<Object>();
                    list.add(o);
                    fireEvent(CallbackEventType.PRE_REMOVE, list);
                    HibernateUtils.getSession().delete(o);
                    fireEvent(CallbackEventType.POST_REMOVE, list);
//                }
            } catch(MessagesIncludingException e) {
                msgs.add(e.getMessages());
            }
        }
        if(msgs.hasError()) {
        	throw new MessagesIncludingException(msgs);
        }
    }

    @Override
    public Messages validate(Object o, WhenVerifier when) {
        Messages ret = super.validate(o, when);
    	try {
	        if(when == WhenVerifier.INSERT) {
	            RelationalChecker.getRerationalChecker().allowPersist(o);
	        } else {
	            RelationalChecker.getRerationalChecker().allowUpdate(o);
	        }
	        return ret;
        } catch(MessagesIncludingException e) {
            ret.add(e.getMessages());
        	return ret;
    	} catch(Exception ex) {
    		throw new RuntimeException(ex);
        }
    }

    @Override
	@SuppressWarnings("unchecked")
	protected List getMutchingObjects(Object o, Check check) {
    	Session session = HibernateUtils.getSession();
		session.evict(o);
		List list = super.getMutchingObjects(o, check);
		for(Object tmp : list) {
			session.evict(tmp);	//Hibernateの管理対象オブジェクトから除外（しないとうにゃうにゃする）
		}
		return list;
	}
}
