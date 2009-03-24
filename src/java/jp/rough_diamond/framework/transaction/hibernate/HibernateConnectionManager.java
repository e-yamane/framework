/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.framework.transaction.hibernate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;

import jp.rough_diamond.commons.lang.StringUtils;
import jp.rough_diamond.framework.transaction.ConnectionManager;
import jp.rough_diamond.framework.transaction.VersionUnmuchException;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Interceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StaleObjectStateException;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

@SuppressWarnings("unchecked")
public class HibernateConnectionManager extends ConnectionManager {
	private final static Log log = LogFactory.getLog(HibernateConnectionManager.class);
	
	static ThreadLocal<Stack<Session>> tl = new ThreadLocal<Stack<Session>>() {
        protected Stack<Session> initialValue() {
            return new Stack<Session>();
        }
    };
    

	static ThreadLocal<Map<Session, Transaction>> transactionMap = new ThreadLocal<Map<Session, Transaction>>() {
		protected Map<Session, Transaction> initialValue() {
			return new HashMap<Session, Transaction>();
		}
	};
	
	private SessionFactory sessionFactory;
	private SessionFactory getSessionFactory() {
		init();
		return sessionFactory;
	}

    private Configuration config;
    private Configuration getConfig2() {
    	init();
        return config;
    }
    
	private synchronized void init() {
		if(sessionFactory == null) {
			init2();
		}
	}
	
	private void init2() {
		log.debug("�Z�b�V�����t�@�N�g���[�����������܂��B:" + this);
		config = new Configuration();
		if(getHibernateConfigName() == null) {
			log.debug("�f�t�H���g�ݒ�t�@�C���ŏ��������܂��B");
			config.configure();
		} else {
			log.debug("�ݒ�t�@�C�����F" + getHibernateConfigName());
			config.configure(getHibernateConfigName());
		}
		String addingPropertyFileName = getAddingPropertyFileName();
		if(!StringUtils.isBlank(addingPropertyFileName)) {
			InputStream is = null;
			IOException ioe = null;
			try {
				File f = new File(addingPropertyFileName);
				if(f.exists()) {
					try {
						log.debug("�v���p�e�B�t�@�C�����t�@�C������ǂݍ��݂܂��B");
						is = new FileInputStream(f);
					} catch (FileNotFoundException e) {
						// ���݃`�F�b�N���Ă��邩���{���肦�Ȃ�
						throw new RuntimeException(e);
					}
				} else {
					log.debug("�v���p�e�B�t�@�C����CLASSPATH����ǂݍ��݂܂��B");
					is = this.getClass().getClassLoader().getResourceAsStream(addingPropertyFileName);
				}
				Properties prop = new Properties();
				prop.load(is);
				config.addProperties(prop);
			} catch (IOException e) {
				ioe = e;
				throw new RuntimeException(e);
			} finally {
				if(is != null) {
					try {
						is.close();
					} catch (IOException e) {
						log.debug(e.getMessage(), e);
						if(ioe == null) {
							throw new RuntimeException(e);
						}
					}
				}
			}
		}
		
		if(getInterceptor() != null) {
			config.setInterceptor(getInterceptor());
		}
		setListeners();
		buildSessionFactory();
        log.debug("�Z�b�V�����X�^�b�N�����������܂��B");
	}
	
	private void buildSessionFactory() {
		sessionFactory = config.buildSessionFactory();
	}
	
	protected void setListeners() {
		if(listenersMap == null) {
			return;
		}
		for(Map.Entry<String, List<String>> entry : listenersMap.entrySet()) {
			List<String> list = entry.getValue();
			if(list != null) {
				config.setListeners(entry.getKey(), list.toArray(new String[list.size()]));
			}
		}
	}
	
	protected boolean isTransactionBegining(MethodInvocation mi) {
        init();
		return (tl.get().size() != 0);
	}

	@SuppressWarnings("deprecation")
	public Connection getCurrentConnection(MethodInvocation mi) {
		Session session = getSession2();
		if(session == null) {
			return null;
		} else {
			return getSession2().connection();
		}
	}

    public static Configuration getConfig() {
        HibernateConnectionManager hcm = 
            (HibernateConnectionManager)getConnectionManager();
        return hcm.getConfig2();
    }

    public static Session getCurrentSession() {
		HibernateConnectionManager hcm = 
			(HibernateConnectionManager)getConnectionManager();
		return hcm.getSession2();
	}

    public static void rebuildSessionFactory() {
        HibernateConnectionManager hcm = 
            (HibernateConnectionManager)getConnectionManager();
        hcm.rebuildSessionFactory2();
    }
    
	private void rebuildSessionFactory2() {
		buildSessionFactory();
    }

    private Session getSession2() {
        init();
        if(tl.get().isEmpty()) {
        	return null;
        } else {
        	return tl.get().peek();
        }
	}

	volatile static int accessCounter = 0;
    public void beginTransaction(MethodInvocation mi) {
		Session session = getSessionFactory().openSession();
		log.info("--- DB�Z�b�V�������m�ۂł��܂����B ");
		Transaction t = session.beginTransaction();
		transactionMap.get().put(session, t);
		tl.get().push(session);
        
		String methodName = mi.getMethod().getName();
		log.info("�g�����U�N�V�������J�n���܂� : " + mi.getMethod().getDeclaringClass().getName() + "#" + methodName);
		accessCounter++;
		log.info("transaction accessCounter : " + accessCounter);
	}

    public void rollback(MethodInvocation mi) {
        init();
		String methodName = mi.getMethod().getName();
		log.info("���[���o�b�N���܂� : " + mi.getMethod().getDeclaringClass().getName() + "#" + methodName);

		Session session = (Session)((Stack)tl.get()).pop();
		try {
			Transaction t = (Transaction)((Map)transactionMap.get()).get(session);
			t.rollback();
		} catch(Exception e) {
			log.warn("���[���o�b�N���ɗ�O���������܂������������܂��B", e);
		} finally {
            ((Map)transactionMap.get()).remove(session);
			accessCounter--;
			session.close();
		}
	}

	public void commit(MethodInvocation mi) throws VersionUnmuchException {
        init();
		String methodName = mi.getMethod().getName();
		log.info("�R�~�b�g���܂� : " + mi.getMethod().getDeclaringClass().getName() + "#" + methodName);
		log.debug(log.getClass().getName());

		Session session = (Session)((Stack)tl.get()).peek();
		Transaction t = (Transaction)((Map)transactionMap.get()).get(session);
		try {
			t.commit();
			session.close();
			((Stack)tl.get()).pop();
            ((Map)transactionMap.get()).remove(session);
		} catch(StaleObjectStateException e) {
			Class[] exceptionTypes = mi.getMethod().getExceptionTypes();
			for(int i = 0 ; i < exceptionTypes.length ; i++) {
				Class exceptionType = exceptionTypes[i];
				if(VersionUnmuchException.class.isAssignableFrom(exceptionType)) {
					log.debug(e.getMessage(), e);
					throw new VersionUnmuchException(e.getMessage());
				}
			}
			throw e;
		} finally{
			accessCounter--;
		}
	}

	private String hibernateConfigName;
	public String getHibernateConfigName() {
		return hibernateConfigName;
	}

	//DI�p�I�I
	public void setHibernateConfigName(String hibernateConfigName) {
		this.hibernateConfigName = hibernateConfigName;
	}

	private String addingPropertyFileName;
	public String getAddingPropertyFileName() {
		return addingPropertyFileName;
	}

	//DI�p�I�I
	public void setAddingPropertyFileName(String addingPropertyFileName) {
		this.addingPropertyFileName = addingPropertyFileName;
	}
	
	private Interceptor interceptor;
	public Interceptor getInterceptor() {
		return interceptor;
	}

	//DI�p�I�I
	public void setInterceptor(Interceptor interceptor) {
		this.interceptor = interceptor;
	}
	
	private Map<String, List<String>> listenersMap;
	public Map<String, List<String>> getListenersMap() {
		return listenersMap;
	}

	//DI�p�I�I
	public void setListenersMap(Map<String, List<String>> listenersMap) {
		this.listenersMap = listenersMap;
	}
}
