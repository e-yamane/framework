/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.service.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    protected <T> List<T> findByExtractor(Class<T> type, Extractor extractor, boolean isNoCache, RecordLock lock) {
    	BasicServiceInterceptor.startLoad(isNoCache);
        try {
        	return findByExtractor2(type, extractor, isNoCache, lock);
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
        	BasicServiceInterceptor.setNoCache(false);
        	BasicServiceInterceptor.popPostLoadedObjects();
        }
    }

	@Override
	public <T> long getCountByExtractor(Extractor extractor) {
		if(extractor.getValues().size() == 0) {
	        Query countQuery = Extractor2HQL.extractor2CountQuery(extractor);
	        Number n = (Number)countQuery.list().get(0);
	        return n.longValue();
		} else {
			PreparedStatement pstmt = Extractor2HQL.extractor2PreparedStatement(extractor);
			SQLException sqlex = null;
			try {
				ResultSet rs = pstmt.executeQuery();
				try {
					rs.next();
					return rs.getLong(1);
				} finally {
					rs.close();
				}
			} catch (SQLException e) {
				sqlex = e;
				throw new RuntimeException(e);
			} finally {
				try {
					pstmt.close();
				} catch (SQLException e) {
					sqlex = (sqlex == null) ? e : sqlex;
					throw new RuntimeException(sqlex);
				}
			}
		}
	}
	
    @SuppressWarnings("unchecked")
    <T> List<T> findByExtractor2(Class<T> type, Extractor extractor, boolean isNoCache, RecordLock lock) throws VersionUnmuchException, MessagesIncludingException {
        List<T> list;
        if(extractor.getLimit() != 0) {
	        Query query = Extractor2HQL.extractor2Query(extractor, getLockMode(lock));
        	list = (List<T>) Extractor2HQL.makeList(type, extractor, query.list());
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
        return list;
    }
    
	@Override
    @SuppressWarnings("unchecked")
    public <T> void insert(T... objects) throws MessagesIncludingException {
        try {
            SessionFactory sf = HibernateUtils.getSession().getSessionFactory();
            for(Object o : objects) {
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

	/* (non-Javadoc)
	 * @see jp.rough_diamond.commons.service.BasicService#clearCache(java.lang.Object)
	 */
	@Override
	public void clearCache(Object o) {
		Session session = HibernateUtils.getSession();
		if(session != null) {
			session.evict(o);
		}
	}

	void firePostLoadDirect(Object o) {
		log.debug("予期しないload（lazy loading）が発生しました。POST_LOADイベントをFireします。");
		try {
			fireEvent(CallbackEventType.POST_LOAD, new ArrayList<Object>(Arrays.asList(o)));
		} catch (Exception e) {
            throw new RuntimeException(e);
		}
	}
}
