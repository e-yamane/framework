/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */

package jp.rough_diamond.commons.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * �R�[���o�b�N�C�x���g���X�i�[
 */
@SuppressWarnings("unchecked")
abstract class CallbackEventListener implements Comparable<CallbackEventListener>{
	private static Log log = LogFactory.getLog(CallbackEventListener.class);
	final Class annotationType;
	final Method method;

	CallbackEventListener(Method m, Class annotationType) {
		this.method = m;
		this.annotationType = annotationType;
	}
	
	abstract void callback(Object eventSource, CallbackEventType type) 
						throws IllegalAccessException, InvocationTargetException;
	abstract boolean isSelfCallback();

	int getPriority() {
        try {
            Annotation a = method.getAnnotation(annotationType);
            return (Integer)a.getClass().getMethod("priority").invoke(a);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
	}
	
	public int compareTo(CallbackEventListener other) {
        try {
        	//�D��x�̍�����
        	int p1 = this.getPriority();
        	int p2 = other.getPriority();
            if(p1 == p2) {
            	//���ȃR�[���o�b�N�̕����D��
            	p1 = this.isSelfCallback() ? 1 : 0;
            	p2 = other.isSelfCallback() ? 1 : 0;
            	if(p1 == p2) {
            		//���O�Ń\�[�g
            		String s1 = this.method.toString();
            		String s2 = other.method.toString();
            		return s1.compareTo(s2);
            	}
            }
            return p2 - p1;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
	}
	static boolean isEventListener(Object listener, Class srcType, Method m, Class annotationType) {
		Annotation annotation = m.getAnnotation(annotationType);
		//�R�[���o�b�N�A�m�e�[�V�������Ȃ����\�b�h�Ȃ̂őΏۊO
	    if(annotation == null) {
	    	return false;
	    }
		List<Class> paramType;
		if(listener == null) {
			paramType = Arrays.asList(m.getParameterTypes());
		} else {
			if(m.getParameterTypes().length == 0) {
				log.warn(m.toString() + "�͈������Ȃ��̂ŃR�[���o�b�N���\�b�h�Ƃ��ĔF�����܂���B");
				return false;
			}
			if(!m.getParameterTypes()[0].isAssignableFrom(srcType)) {
				log.warn(m.toString() + "�͑�P�����̃^�C�v���s��v�Ȃ̂ŃR�[���o�b�N���\�b�h�Ƃ��ĔF�����܂���B");
				return false;
			}
			paramType = Arrays.asList(m.getParameterTypes());
			paramType = paramType.subList(1, paramType.size());
		}
		if(paramType.size() == 0) {
			return true;
		} else if(paramType.size() == 1 && CallbackEventType.class.isAssignableFrom(paramType.get(0))) {
			return true;
		} else {
			log.warn(m.toString() + "�͈����^�C�v������Ă��邽�߃R�[���o�b�N���\�b�h�Ƃ��ĔF�����܂���B");
		}
		return false;
	}
	
	static class SelfEventListener extends CallbackEventListener {
		SelfEventListener(Method m, Class annotationType) {
			super(m, annotationType);
		}
		@Override
		void callback(Object eventSource, CallbackEventType type)
				throws IllegalAccessException, InvocationTargetException {
        	if(log.isDebugEnabled()) {
        		log.debug(String.format("CallBack EventType[%s]:%s(%s)#%s()", 
        				type, eventSource.getClass().getName(), method.getDeclaringClass().getName(), method.getName()));
        	}
        	if(method.getParameterTypes().length == 0) {
        		method.invoke(eventSource);
        	} else {
        		method.invoke(eventSource, type);
        	}
		}
		@Override
		boolean isSelfCallback() {
			return true;
		}
	};
	
	static class EventAdapter extends CallbackEventListener {
		final Object listener;
		EventAdapter(Object listener, Method m, Class annotationType) {
			super(m, annotationType);
			this.listener = listener;
		}
		@Override
		void callback(Object eventSource, CallbackEventType type)
				throws IllegalAccessException, InvocationTargetException {
        	if(log.isDebugEnabled()) {
        		log.debug(String.format("CallBack EventType[%s]:%s(%s)#%s(%s)", 
        				type, listener.getClass().getName(), method.getDeclaringClass().getName(), method.getName(), eventSource.getClass().getName()));
        	}
        	if(method.getParameterTypes().length == 1) {
        		log.debug(method.getParameterTypes()[0].getName());
        		log.debug(eventSource.getClass().getName());
        		method.invoke(listener, eventSource);
        	} else {
        		method.invoke(listener, eventSource, type);
        	}
		}
		@Override
		boolean isSelfCallback() {
			return false;
		}
	}
}
