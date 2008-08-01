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
/**
 * 
 */
package jp.rough_diamond.commons.service;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.rough_diamond.commons.di.DIContainerFactory;
import jp.rough_diamond.commons.extractor.Extractor;
import jp.rough_diamond.commons.resource.Message;
import jp.rough_diamond.commons.resource.Messages;
import jp.rough_diamond.commons.resource.MessagesIncludingException;
import jp.rough_diamond.commons.resource.ResourceManager;
import jp.rough_diamond.commons.service.annotation.MaxLength;
import jp.rough_diamond.commons.service.annotation.NestedComponent;
import jp.rough_diamond.commons.service.annotation.NotNull;
import jp.rough_diamond.commons.service.annotation.Unique;
import jp.rough_diamond.commons.service.annotation.Verifier;
import jp.rough_diamond.framework.service.Service;
import jp.rough_diamond.framework.service.ServiceLocator;
import jp.rough_diamond.framework.transaction.VersionUnmuchException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * DAO基本サービス
 * @author $Author: e-yamane@rough-diamond.jp$
 * @date $Date: 2006-04-17 19:10:57 +0900 (譛?, 17 4 2006) $
 */
@SuppressWarnings("unchecked")
abstract public class BasicService implements Service {
    private final static Log log = LogFactory.getLog(BasicService.class);
    
    public final static String BOOLEAN_CHAR_T = "Y";
    public final static String BOOLEAN_CHAR_F = "N";
    
    public static enum RecordLock {
    	NONE,				//ロックしない
    	FOR_UPDATE,			//排他ロック
    	FOR_UPDATE_NOWAIT,	//排他ロック（待たない）
    }
    
    private final static String DEFAULT_BASIC_SERVICE_CLASS_NAME = "jp.rough_diamond.commons.service.hibernate.HibernateBasicService";
    private final static BasicService INSTANCE;
    static {
    	BasicService tmp = null;
    	try {
    		tmp = ServiceLocator.getService(BasicService.class);
    	} catch(Exception e) {
    		//指定されていない場合の可能性あり。デフォルトのクラスでサービスを生成する
    		Class<? extends BasicService> cl;
			try {
				cl = (Class<BasicService>)Class.forName(DEFAULT_BASIC_SERVICE_CLASS_NAME);
				tmp = (BasicService)ServiceLocator.getService(cl);
			} catch (ClassNotFoundException e1) {
				throw new RuntimeException(e);
			}
    	}
    	INSTANCE = tmp;
    }
    
    /**
     * Basicサービスを取得する
     * @return  Basicサービス
     */
    public static BasicService getService() {
    	return INSTANCE;
    }
    
    public <T> T findByPK(Class<T> type, Serializable pk) {
    	return findByPK(type, pk, false);
    }
    
    public <T> T findByPK(Class<T> type, Serializable pk, RecordLock lock) {
    	return findByPK(type, pk, false, lock);
    }

    public <T> T findByPK(Class<T> type, Serializable pk, boolean isNoCache) {
    	return findByPK(type, pk, isNoCache, RecordLock.NONE);
    }
    
    abstract public <T> T findByPK(Class<T> type, Serializable pk, boolean isNoCache, RecordLock lock);
    
    public <T> List<T> findByExtractor(Extractor extractor) {
    	return findByExtractor(extractor, false);
    }

    public <T> List<T> findByExtractor(Extractor extractor, RecordLock lock) {
    	return findByExtractor(extractor, false, lock);
    }

    public <T> List<T> findByExtractor(Extractor extractor, boolean isNoCache) {
    	return findByExtractor(extractor.target, extractor, isNoCache, RecordLock.NONE);
    }
    
    public <T> List<T> findByExtractor(Extractor extractor, boolean isNoCache, RecordLock lock) {
    	return findByExtractor(extractor.target, extractor, isNoCache, lock);
    }

    abstract protected <T> List<T> findByExtractor(Class<T> type, Extractor extractor, boolean isNoCache, RecordLock lock);

	public <T> FindResult<T> findByExtractorWithCount(Extractor extractor) {
    	return findByExtractorWithCount(extractor, false);
    }
    
	public <T> FindResult<T> findByExtractorWithCount(Extractor extractor, RecordLock lock) {
    	return findByExtractorWithCount(extractor, false, lock);
    }

	public <T> FindResult<T> findByExtractorWithCount(Extractor extractor, boolean isNoCache) {
    	return findByExtractorWithCount(extractor.target, extractor, isNoCache, RecordLock.NONE);
    }
    
	public <T> FindResult<T> findByExtractorWithCount(Extractor extractor, boolean isNoCache, RecordLock lock) {
    	return findByExtractorWithCount(extractor.target, extractor, isNoCache, lock);
    }

    abstract protected <T> FindResult<T> findByExtractorWithCount(Class<T> type, Extractor extractor, boolean isNoCache, RecordLock lock);
    
    public <T> List<T> findAll(Class<T> type) {
    	return findAll(type, false);
    }

    abstract public <T> List<T> findAll(Class<T> type, boolean isNoCache);
    abstract public <T> void insert(T... objects) throws MessagesIncludingException;
    abstract public <T> void update(T... objects) throws VersionUnmuchException, MessagesIncludingException;

    public void deleteByExtractor(Extractor extractor) throws VersionUnmuchException, MessagesIncludingException {
    	List list = findByExtractor(extractor);
    	delete(list.toArray());
    }
    
    public <T> void deleteAll(Class<T> cl) {
    	List<T> list = findAll(cl);
    	try {
			delete(list.toArray(new Object[list.size()]));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }

    abstract public void delete(Object... objects) throws VersionUnmuchException, MessagesIncludingException;

    public <T> void deleteByPK(Class<T> type, Serializable pk) throws VersionUnmuchException, MessagesIncludingException {
        T o = findByPK(type, pk);
        if(o != null) {
            delete(o);
        }
    }
    
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
    
    
    protected void fireEvent(CallbackEventType eventType, List objects) throws VersionUnmuchException, MessagesIncludingException {
        if(objects.size() == 0) {
            return;
        }
        Map<Class, SortedSet<Method>> map = new HashMap<Class, SortedSet<Method>>();
        for(Object o : objects) {
            if(o == null) {
                continue;
            }
            SortedSet<Method> set = map.get(o.getClass());
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
                SortedSet<Method> set = map.get(o.getClass());
                for(Method m : set) {
                    if(m.getParameterTypes().length == 0) {
                        m.invoke(o);
                    } else {
                        m.invoke(o, eventType);
                    }
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
    
    private Map<Class, Map<CallbackEventType, SortedSet<Method>>> eventListenrsCache = 
                            new HashMap<Class, Map<CallbackEventType, SortedSet<Method>>>();
    
    SortedSet<Method> getEventListener(Class cl, CallbackEventType eventType) {
        Map<CallbackEventType, SortedSet<Method>> map = eventListenrsCache.get(cl);
        if(map == null) {
            map = new HashMap<CallbackEventType, SortedSet<Method>>();
            eventListenrsCache.put(cl, map);
        }
        SortedSet<Method> set = map.get(eventType);
        if(set == null) {
            set = findEventListener(cl, eventType);
            map.put(eventType, set);
        }
        return set;
    }
    
    SortedSet<Method> findEventListener(Class cl, CallbackEventType eventType) {
        Class annotationType = eventType.getAnnotation();
        Method[] methods = cl.getMethods();
        SortedSet<Method> set = new TreeSet<Method>(new EventListenerSorter(annotationType));
        for(Method m : methods) {
            Annotation annotation = m.getAnnotation(annotationType);
            if(annotation != null) {
                if(m.getParameterTypes().length == 0) {
                    set.add(m);
                } else if(m.getParameterTypes().length == 1 && CallbackEventType.class.isAssignableFrom(m.getParameterTypes()[0])) {
                    set.add(m);
                } else {
                    log.warn(m.toString() + "は引数タイプが誤っているためコールバックメソッドとして認識しません。");
                }
            }
        }
        return set;
    }
    
    private final static class EventListenerSorter implements Comparator<Method>, Serializable {
		private static final long serialVersionUID = 1L;
		final Class annotationType;
        EventListenerSorter(Class annotationType) {
            this.annotationType = annotationType;
        }
        public int compare(Method o1, Method o2) {
            try {
                Annotation a1 = o1.getAnnotation(annotationType);
                Annotation a2 = o2.getAnnotation(annotationType);
                int p1 = (Integer)a1.getClass().getMethod("priority").invoke(a1);
                int p2 = (Integer)a2.getClass().getMethod("priority").invoke(a2);
                if(p1 == p2) {
                    return o1.toString().compareTo(o2.toString());
                } else {
                    return p2 - p1;
                }
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }
        
    }
    
    private Messages unitPropertyValidate(Object o, WhenVerifier when) throws Exception {
    	Messages ret = new Messages();
    	Class cl = o.getClass();
    	PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(cl);
    	for(PropertyDescriptor pd : pds) {
    		Method m = pd.getReadMethod();
    		if(m != null) {
	    		MaxLength ml = m.getAnnotation(MaxLength.class);
	    		if(ml != null) {
	    			Object str = m.invoke(o);
	    			if(getLength(str) > ml.length()) {
                        log.debug("最大長超過エラー:" + ml.property());
	    				ret.add(ml.property(), new Message("errors.maxlength", ResourceManager.getResource().getString(ml.property()), "" + ml.length()));
	    			}
	    		}
    			Object val = m.invoke(o);
	    		NotNull nn = m.getAnnotation(NotNull.class);
	    		if(nn != null) {
	    			if(val == null) {
                        log.debug("必須属性エラー:" + nn.property());
	    				ret.add(nn.property(), new Message("errors.required", ResourceManager.getResource().getString(nn.property())));
	    			} else if(val instanceof String) {
	    				if(getLength((String)val) == 0) {
                            log.debug("必須属性エラー:" + nn.property());
		    				ret.add(nn.property(), new Message("errors.required", ResourceManager.getResource().getString(nn.property())));
	    				}
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
    
	abstract protected Messages checkUnique(Object o, WhenVerifier when, Unique u);
	
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
