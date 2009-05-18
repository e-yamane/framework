/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
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
import jp.rough_diamond.commons.extractor.Condition;
import jp.rough_diamond.commons.extractor.Extractor;
import jp.rough_diamond.commons.extractor.Property;
import jp.rough_diamond.commons.resource.Message;
import jp.rough_diamond.commons.resource.Messages;
import jp.rough_diamond.commons.resource.MessagesIncludingException;
import jp.rough_diamond.commons.resource.ResourceManager;
import jp.rough_diamond.commons.service.annotation.Check;
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
 * DAO��{�T�[�r�X
 */
@SuppressWarnings("unchecked")
abstract public class BasicService implements Service {
    private final static Log log = LogFactory.getLog(BasicService.class);
    
    public final static String 	BOOLEAN_CHAR_T = "Y";
    public final static String 	BOOLEAN_CHAR_F = "N";

    /**
     * ���R�[�h�̃��b�N���[�h
     * @author e-yamane
     */
    public static enum RecordLock {
    	NONE,				//���b�N���Ȃ�
    	FOR_UPDATE,			//�r�����b�N
    	FOR_UPDATE_NOWAIT,	//�r�����b�N�i�҂��Ȃ��j
    }
    
    private final static String DEFAULT_BASIC_SERVICE_CLASS_NAME = "jp.rough_diamond.commons.service.hibernate.HibernateBasicService";
    
    /**
     * Basic�T�[�r�X���擾����
     * @return  Basic�T�[�r�X
     */
    public static BasicService getService() {
    	return ServiceLocator.getService(BasicService.class, DEFAULT_BASIC_SERVICE_CLASS_NAME);
    }

    /**
     * �w�肳�ꂽ��L�[�ɑΉ�����I�u�W�F�N�g���擾����
     * �擾�����I�u�W�F�N�g�͉i�����G���W���i��FHibernate�j���L���b�V������悤�Ɏw������
     * �܂��A�擾�������R�[�h�ɑ΂��郍�b�N�͍s��Ȃ�
     * @param <T>	�擾����N���X�̃^�C�v
     * @param type	�擾����N���X�̃^�C�v
     * @param pk	��L�[
     * @return		��L�[�ɑΉ�����I�u�W�F�N�g�B�Ή�����I�u�W�F�N�g�������ꍇ��null��ԋp����
     */
    public <T> T findByPK(Class<T> type, Serializable pk) {
    	return findByPK(type, pk, false);
    }
    
    /**
     * �w�肳�ꂽ��L�[�ɑΉ�����I�u�W�F�N�g���擾����
     * �擾�����I�u�W�F�N�g�͉i�����G���W���i��FHibernate�j���L���b�V������悤�Ɏw������
     * @param <T>	�擾����N���X�̃^�C�v
     * @param type	�擾����N���X�̃^�C�v
     * @param pk	��L�[
     * @param lock	�擾�I�u�W�F�N�g�̃��b�N���[�h���w�肷��
     * @return		��L�[�ɑΉ�����I�u�W�F�N�g�B�Ή�����I�u�W�F�N�g�������ꍇ��null��ԋp����
     */
    public <T> T findByPK(Class<T> type, Serializable pk, RecordLock lock) {
    	return findByPK(type, pk, false, lock);
    }

    /**
     * �w�肳�ꂽ��L�[�ɑΉ�����I�u�W�F�N�g���擾����
     * �擾�������R�[�h�ɑ΂��郍�b�N�͍s��Ȃ�
     * @param <T>		�擾����N���X�̃^�C�v
     * @param type		�擾����N���X�̃^�C�v
     * @param pk		��L�[
     * @param isNoCache	true�F�i�����G���W���i��FHibernate�j���L���b�V�����Ȃ� false:�L���b�V������	
     * @return			��L�[�ɑΉ�����I�u�W�F�N�g�B�Ή�����I�u�W�F�N�g�������ꍇ��null��ԋp����
     */
    public <T> T findByPK(Class<T> type, Serializable pk, boolean isNoCache) {
    	return findByPK(type, pk, isNoCache, RecordLock.NONE);
    }
    
    /**
     * �w�肳�ꂽ��L�[�ɑΉ�����I�u�W�F�N�g���擾����
     * �Ȃ��A���R�[�h���b�N�A�I�u�W�F�N�g�L���b�V���Ɋւ��Ă͎g�p����i�����G���W���ɂ���Ă�
     * �������K�p����Ȃ��ꍇ������܂��B
     * @param <T>		�擾����N���X�̃^�C�v
     * @param type		�擾����N���X�̃^�C�v
     * @param pk		��L�[
     * @param isNoCache	true�F�i�����G���W���i��FHibernate�j���L���b�V�����Ȃ� false:�L���b�V������	
     * @param lock		�擾�I�u�W�F�N�g�̃��b�N���[�h���w�肷��
     * @return			��L�[�ɑΉ�����I�u�W�F�N�g�B�Ή�����I�u�W�F�N�g�������ꍇ��null��ԋp����
     */
    abstract public <T> T findByPK(Class<T> type, Serializable pk, boolean isNoCache, RecordLock lock);

    /**
     * ���������ɑΉ�����I�u�W�F�N�g�ꗗ���擾����
     * �擾�����I�u�W�F�N�g�͉i�����G���W���i��FHibernate�j���L���b�V������悤�Ɏw������
     * �܂��A�擾�������R�[�h�ɑ΂��郍�b�N�͍s��Ȃ�
     * @param <T>		�擾����I�u�W�F�N�g�̃^�C�v
     * @param extractor	��������
     * @return			���������ɑΉ�����I�u�W�F�N�g�ꗗ�B�P�����Y������f�[�^��������Ηv�f���O�̃��X�g��ԋp����
     */
    public <T> List<T> findByExtractor(Extractor extractor) {
    	return findByExtractor(extractor, false);
    }

    /**
     * ���������ɑΉ�����I�u�W�F�N�g�ꗗ���擾����
     * �擾�����I�u�W�F�N�g�͉i�����G���W���i��FHibernate�j���L���b�V������悤�Ɏw������
     * @param <T>		�擾����I�u�W�F�N�g�̃^�C�v
     * @param extractor	��������
     * @param lock		�擾�I�u�W�F�N�g�̃��b�N���[�h���w�肷��
     * @return			���������ɑΉ�����I�u�W�F�N�g�ꗗ�B�P�����Y������f�[�^��������Ηv�f���O�̃��X�g��ԋp����
     */
    public <T> List<T> findByExtractor(Extractor extractor, RecordLock lock) {
    	return findByExtractor(extractor, false, lock);
    }

    /**
     * ���������ɑΉ�����I�u�W�F�N�g�ꗗ���擾����
     * �擾�������R�[�h�ɑ΂��郍�b�N�͍s��Ȃ�
     * @param <T>		�擾����I�u�W�F�N�g�̃^�C�v
     * @param extractor	��������
     * @param isNoCache	true�F�i�����G���W���i��FHibernate�j���L���b�V�����Ȃ� false:�L���b�V������	
     * @return			���������ɑΉ�����I�u�W�F�N�g�ꗗ�B�P�����Y������f�[�^��������Ηv�f���O�̃��X�g��ԋp����
     */
    public <T> List<T> findByExtractor(Extractor extractor, boolean isNoCache) {
    	return findByExtractor(extractor.target, extractor, isNoCache, RecordLock.NONE);
    }
    
    /**
     * ���������ɑΉ�����I�u�W�F�N�g�ꗗ���擾����
     * @param <T>		�擾����I�u�W�F�N�g�̃^�C�v
     * @param extractor	��������
     * @param isNoCache	true�F�i�����G���W���i��FHibernate�j���L���b�V�����Ȃ� false:�L���b�V������	
     * @param lock		�擾�I�u�W�F�N�g�̃��b�N���[�h���w�肷��
     * @return			���������ɑΉ�����I�u�W�F�N�g�ꗗ�B�P�����Y������f�[�^��������Ηv�f���O�̃��X�g��ԋp����
     */
    public <T> List<T> findByExtractor(Extractor extractor, boolean isNoCache, RecordLock lock) {
    	return findByExtractor(extractor.target, extractor, isNoCache, lock);
    }

    abstract protected <T> List<T> findByExtractor(Class<T> type, Extractor extractor, boolean isNoCache, RecordLock lock);

    /**
     * ���������ɑΉ�����I�u�W�F�N�g�ꗗ�ƁA���������ɍ��v���鑍�������擾����
     * �擾�����I�u�W�F�N�g�͉i�����G���W���i��FHibernate�j���L���b�V������悤�Ɏw������
     * �܂��A�擾�������R�[�h�ɑ΂��郍�b�N�͍s��Ȃ�
     * @param <T>		�擾����I�u�W�F�N�g�̃^�C�v
     * @param extractor	��������
     * @return			��������
     */
	public <T> FindResult<T> findByExtractorWithCount(Extractor extractor) {
    	return findByExtractorWithCount(extractor, false);
    }

	/**
     * ���������ɑΉ�����I�u�W�F�N�g�ꗗ�ƁA���������ɍ��v���鑍�������擾����
     * �擾�����I�u�W�F�N�g�͉i�����G���W���i��FHibernate�j���L���b�V������悤�Ɏw������
     * @param <T>		�擾����I�u�W�F�N�g�̃^�C�v
     * @param extractor	��������
     * @param lock		�擾�I�u�W�F�N�g�̃��b�N���[�h���w�肷��
     * @return			��������
	 */
	public <T> FindResult<T> findByExtractorWithCount(Extractor extractor, RecordLock lock) {
    	return findByExtractorWithCount(extractor, false, lock);
    }

	/**
     * ���������ɑΉ�����I�u�W�F�N�g�ꗗ�ƁA���������ɍ��v���鑍�������擾����
     * �擾�������R�[�h�ɑ΂��郍�b�N�͍s��Ȃ�
     * @param <T>		�擾����I�u�W�F�N�g�̃^�C�v
     * @param extractor	��������
     * @param isNoCache	true�F�i�����G���W���i��FHibernate�j���L���b�V�����Ȃ� false:�L���b�V������	
     * @return			��������
	 */
	public <T> FindResult<T> findByExtractorWithCount(Extractor extractor, boolean isNoCache) {
    	return findByExtractorWithCount(extractor.target, extractor, isNoCache, RecordLock.NONE);
    }

	/**
     * ���������ɑΉ�����I�u�W�F�N�g�ꗗ�ƁA���������ɍ��v���鑍�������擾����
     * @param <T>		�擾����I�u�W�F�N�g�̃^�C�v
     * @param extractor	��������
     * @param isNoCache	true�F�i�����G���W���i��FHibernate�j���L���b�V�����Ȃ� false:�L���b�V������	
     * @param lock		�擾�I�u�W�F�N�g�̃��b�N���[�h���w�肷��
     * @return			��������
	 */
	public <T> FindResult<T> findByExtractorWithCount(Extractor extractor, boolean isNoCache, RecordLock lock) {
    	return findByExtractorWithCount(extractor.target, extractor, isNoCache, lock);
    }

	/**
	 * ���������ɍ��v����i���I�u�W�F�N�g�̌������擾����
	 * @param <T>		�����擾�ΏۃI�u�W�F�N�g�^�C�v
	 * @param extractor	��������
	 * @return			���������ɍ��v����i���I�u�W�F�N�g�̌���
	 */
	public <T> long getCountByExtractor(Extractor extractor) {
		int backup = extractor.getLimit();
		extractor.setLimit(0);
		try {
			return findByExtractorWithCount(extractor).count;
		} finally {
			extractor.setLimit(backup);
		}
	}
	
    abstract protected <T> FindResult<T> findByExtractorWithCount(Class<T> type, Extractor extractor, boolean isNoCache, RecordLock lock);
    
    /**
     * �w�肳�ꂽ�N���X�̉i�����I�u�W�F�N�g��S�Ď擾����
     * �擾�����I�u�W�F�N�g�͉i�����G���W���i��FHibernate�j���L���b�V������悤�Ɏw������
     * �܂��A�擾�������R�[�h�ɑ΂��郍�b�N�͍s��Ȃ��A�t�F�b�`�T�C�Y�͉��ʃ��C�u�����Ɉˑ�����
     * @param <T>	�擾�ΏۃN���X�^�C�v
     * @param type	�擾�ΏۃN���X�^�C�v
     * @return		�擾�ΏۃN���X�̉i���I�u�W�F�N�g�ꗗ�B�P���������ꍇ�͗v�f���O�̃��X�g��ԋp����
     */
    public <T> List<T> findAll(Class<T> type) {
    	return findAll(type, false, Extractor.DEFAULT_FETCH_SIZE);
    }

    /**
     * �w�肳�ꂽ�N���X�̉i�����I�u�W�F�N�g��S�Ď擾����
     * �擾�����I�u�W�F�N�g�͉i�����G���W���i��FHibernate�j���L���b�V������悤�Ɏw������
     * �܂��A�擾�������R�[�h�ɑ΂��郍�b�N�͍s��Ȃ�
     * @param <T>		�擾�ΏۃN���X�^�C�v
     * @param type		�擾�ΏۃN���X�^�C�v
     * @param fetchSize	�t�F�b�`�T�C�Y�i�����I�ȐU�镑���j
     * @return			�擾�ΏۃN���X�̉i���I�u�W�F�N�g�ꗗ�B�P���������ꍇ�͗v�f���O�̃��X�g��ԋp����
     */
    public <T> List<T> findAll(Class<T> type, int fetchSize) {
    	return findAll(type, false, fetchSize);
    }
    
    /**
     * �w�肳�ꂽ�N���X�̉i�����I�u�W�F�N�g��S�Ď擾����
     * �擾�������R�[�h�ɑ΂��郍�b�N�͍s��Ȃ����A�t�F�b�`�T�C�Y�͉��ʃ��C�u�����Ɉˑ�����
     * @param <T>		�擾�ΏۃN���X�^�C�v
     * @param type		�擾�ΏۃN���X�^�C�v
     * @param isNoCache	true�F�i�����G���W���i��FHibernate�j���L���b�V�����Ȃ� false:�L���b�V������	
     * @return			�擾�ΏۃN���X�̉i���I�u�W�F�N�g�ꗗ�B�P���������ꍇ�͗v�f���O�̃��X�g��ԋp����
     */
    public <T> List<T> findAll(Class<T> type, boolean isNoCache) {
    	return findAll(type, isNoCache, Extractor.DEFAULT_FETCH_SIZE);
    }

    /**
     * �w�肳�ꂽ�N���X�̉i�����I�u�W�F�N�g��S�Ď擾����
     * �擾�������R�[�h�ɑ΂��郍�b�N�͍s��Ȃ�
     * @param <T>		�擾�ΏۃN���X�^�C�v
     * @param type		�擾�ΏۃN���X�^�C�v
     * @param isNoCache	true�F�i�����G���W���i��FHibernate�j���L���b�V�����Ȃ� false:�L���b�V������	
     * @param fetchSize	�t�F�b�`�T�C�Y�i�����I�ȐU�镑���j
     * @return			�擾�ΏۃN���X�̉i���I�u�W�F�N�g�ꗗ�B�P���������ꍇ�͗v�f���O�̃��X�g��ԋp����
     */
    abstract public <T> List<T> findAll(Class<T> type, boolean isNoCache, int fetchSize);
    
    /**
     * �w�肳�ꂽ�I�u�W�F�N�g�i�Q�j���i����(INSERT)����
     * ��L�[��String��������Number���p������I�u�W�F�N�g�ł���null�̏ꍇ�͎�L�[�͎����I��
     * ���j�[�N�Ȓl���̔Ԃ����
     * @param <T>		�i�����I�u�W�F�N�g�̃^�C�v
     * @param objects	�i�����I�u�W�F�N�g�Q
     * @throws MessagesIncludingException	���ؗ�O�i�P�ȏ�̃v���p�e�B�̌��؂Ɏ��s�j
     */
    abstract public <T> void insert(T... objects) throws MessagesIncludingException;

    /**
     * �w�肳�ꂽ�I�u�W�F�N�g�i�Q�j���i�����iUPDATE�j����
     * @param <T>		�i�����I�u�W�F�N�g�̃^�C�v
     * @param objects	�i�����I�u�W�F�N�g�Q
     * @throws MessagesIncludingException	���ؗ�O�i�P�ȏ�̃v���p�e�B�̌��؂Ɏ��s�j
     */
    abstract public <T> void update(T... objects) throws VersionUnmuchException, MessagesIncludingException;

    /**
     * ���������ɍ��v����I�u�W�F�N�g�Q���폜����
     * @param extractor						�폜�I�u�W�F�N�g�̌�������
     * @throws VersionUnmuchException		�_���폜���̊y�ϓI���b�L���O�G���[
     * @throws MessagesIncludingException	�_���폜���̌��ؗ�O
     */
    public void deleteByExtractor(Extractor extractor) throws VersionUnmuchException, MessagesIncludingException {
    	List list = findByExtractor(extractor, true);
    	delete(list.toArray());
    }
    
    /**
     * �w�肵���N���X�̑S�i���I�u�W�F�N�g���폜����
     * @param <T>	�폜�Ώۉi�����^�C�v
     * @param cl	�폜�Ώۉi�����^�C�v
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
     * �w�肳�ꂽ�I�u�W�F�N�g�Q���폜����
     * @param objects						�폜�ΏۃI�u�W�F�N�g
     * @throws VersionUnmuchException		�_���폜���̊y�ϓI���b�L���O�G���[
     * @throws MessagesIncludingException	�_���폜���̌��ؗ�O
     */
    abstract public void delete(Object... objects) throws VersionUnmuchException, MessagesIncludingException;

    /**
     * �w�肳�ꂽ�N���X�̎w�肳�ꂽ��L�[�Ɉ�v����I�u�W�F�N�g���폜����
     * @param <T>	�폜�Ώۉi�����^�C�v
     * @param type	�폜�Ώۉi�����^�C�v
     * @param pk	�폜�ΏۃI�u�W�F�N�g�̎�L�[
     * @throws VersionUnmuchException		�_���폜���̊y�ϓI���b�L���O�G���[
     * @throws MessagesIncludingException	�_���폜���̌��ؗ�O
     */
    public <T> void deleteByPK(Class<T> type, Serializable pk) throws VersionUnmuchException, MessagesIncludingException {
        T o = findByPK(type, pk, true);
        if(o != null) {
            delete(o);
        }
    }

    /**
     * �I�u�W�F�N�g�̉i�����ۂ����؂��s��
     * @param o		���ؑΏۃI�u�W�F�N�g
     * @param when	�i���������i�ǉ�/�X�V�j
     * @return		���؎��s�����ꍇ�̌������b�Z�[�W�Q
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
                	if(log.isDebugEnabled()) {
                		log.debug(String.format("CallBack EventType[%s]:%s(%s)#%s()", eventType, o.getClass().getName(), m.getDeclaringClass().getName(), m.getName()));
                	}
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
                    log.warn(m.toString() + "�͈����^�C�v������Ă��邽�߃R�[���o�b�N���\�b�h�Ƃ��ĔF�����܂���B");
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
                        log.debug("�ő咷���߃G���[:" + ml.property());
	    				ret.add(ml.property(), new Message("errors.maxlength", ResourceManager.getResource().getString(ml.property()), "" + ml.length()));
	    			}
	    		}
	    		NotNull nn = m.getAnnotation(NotNull.class);
	    		if(nn != null) {
	    			Object val = m.invoke(o);
	    			if(val == null) {
                        log.debug("�K�{�����G���[:" + nn.property());
	    				ret.add(nn.property(), new Message("errors.required", ResourceManager.getResource().getString(nn.property())));
	    			} else if(val instanceof String) {
	    				if(getLength((String)val) == 0) {
                            log.debug("�K�{�����G���[:" + nn.property());
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

	/**
	 * �i���ΏۃI�u�W�F�N�g�̃��j�[�N�������؂���
	 * @param o		���ؑΏۃI�u�W�F�N�g
     * @param when	�i���������i�ǉ�/�X�V�j
     * @return		���؎��s�����ꍇ�̌������b�Z�[�W�Q
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
				continue;	//�T�C�Y�O�Ȃ̂Ŗ������ɂn�j
			}
			if(when == WhenVerifier.INSERT) {
				String targetProperty = u.entity() + "." + check.properties()[0];
				//�o�^���ɂP���ȏ゠��̂Ŗ������ɃG���[
				ret.add(targetProperty, new Message("errors.duplicate", 
						ResourceManager.getResource().getString(targetProperty)));
			} else {
				//�X�V��
				if(list.size() > 1) {
					String targetProperty = u.entity() + "." + check.properties()[0];
					ret.add(targetProperty, new Message("errors.duplicate", 
							ResourceManager.getResource().getString(targetProperty)));
				} else if(!o.equals(list.get(0))){
					String targetProperty = u.entity() + "." + check.properties()[0];
	    			ret.add(targetProperty, new Message("errors.duplicate", 
	    					ResourceManager.getResource().getString(targetProperty)));
				}
			}
		}
		return ret;
	}

	protected List getMutchingObjects(Object o, Check check) {
		try {
			Extractor ex = new Extractor(o.getClass());
			for(String property : check.properties()) {
				Object value;
					Method m = jp.rough_diamond.commons.util.PropertyUtils.getGetterMethod(o, property);
					value = m.invoke(o);
				if(value == null) {
					ex.add(Condition.isNull(new Property(property)));
				} else {
					ex.add(Condition.eq(new Property(property), value));
				}
			}		
			return findByExtractor(ex, RecordLock.FOR_UPDATE);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
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
    		log.warn("���؃��\�b�h" + m.toString() + "�͖߂�l��ԋp���܂���B");
    	} else if(!Messages.class.isAssignableFrom(returnType)) {
    		log.warn("���؃��\�b�h" + m.toString() + "�͓K�؂ȕԋp�l�������Ȃ����ߌ��؃��\�b�h�Ƃ͔F�����܂���B");
    		//��O������ׂ����Ȃ��E�E�E
    		return false;
    	}
    	Class[] paramTypes = m.getParameterTypes();
    	if(paramTypes.length > 1) {
    		log.warn("���؃��\�b�h" + m.toString() + "�͓K�؂ȃp�����^�ł͂Ȃ����ߌ��؃��\�b�h�Ƃ͔F�����܂���B");
    		return false;
    	} else if(paramTypes.length == 1 && !WhenVerifier.class.isAssignableFrom(paramTypes[0])) {
    		log.warn("���؃��\�b�h" + m.toString() + "�͓K�؂ȃp�����^�ł͂Ȃ����ߌ��؃��\�b�h�Ƃ͔F�����܂���B");
    		return false;
    	}
		return true;
	}
}
