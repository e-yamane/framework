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
package jp.rough_diamond.framework.transaction;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * �g�����U�N�V�����}�l�[�W��
 * JDK 5.0�ȑO�̃o�[�W�����ł͖{�}�l�[�W�����g�p���邱��
 * �g�����U�N�V���������́AsetTransactionMap�ɓn���ꂽ���Ɋ�Â��čs����B
 * transactionMap�́A�L�[�̓N���X���A�l�̓g�����U�N�V��������������
 * �iREQUIRED or REQUIRED_NEW or NOP)���w�肷��B
 * �ȗ����ꂽ�ꍇ�́A�uREQUIRED�v�ł���
 * @author e-yamane
 */
@SuppressWarnings("unchecked")
public class TransactionManager implements MethodInterceptor {
	private final static Map TRANSACTION_INTERCEPTORS;
    
    private static ThreadLocal transactionBeginingStack = new ThreadLocal() {
        protected Object initialValue() {
            return new Stack();
        }
    };
    
    private static ThreadLocal<Stack<Map>> 
    	transactionContext = new ThreadLocal<Stack<Map>>() {
    		protected Stack<Map> initialValue() {
    			return new Stack<Map>();
    		}
    	};

	static {
		Map tmp = new HashMap();
		tmp.put("REQUIRED", new RequiredInterceptor());
		tmp.put("REQUIRED_NEW", new RequiredNewInterceptor());
		tmp.put("NOP", new NopInterceptor());
		TRANSACTION_INTERCEPTORS = Collections.unmodifiableMap(tmp);
	}
	
	private Map transactionMap = new HashMap();
	
	/**
	 * �g�����U�N�V���������}�b�v���Z�b�g����iDI�p�j
	 * �L�[�̓N���X���A�l�̓g�����U�N�V��������������iREQUIRED or REQUIRED_NEW or NOP)
	 * @param map
	 */
	public void setTransactionMap(Map map) {
		this.transactionMap = map;
	}
	
	/**
	 * �g�����U�N�V���������}�b�v���擾����
	 * @return
	 */
	public Map getTransactionMap() {
		return transactionMap;
	}
	
	protected Map getTransactionInterceptors() {
		return TRANSACTION_INTERCEPTORS;
	}
	
	public Object invoke(MethodInvocation arg0) throws Throwable {
		String className = arg0.getThis().getClass().getName();
		String transactionAttr = (String)transactionMap.get(className);
		if(transactionAttr == null) {
			transactionAttr = "REQUIRED";
		}
		TransactionInterceptor ti = (TransactionInterceptor)getTransactionInterceptors().get(transactionAttr);
        return ti.invoke(arg0);
	}
    
	/**
	 * ���炩�̃g�����U�N�V�����������ۂ���ԋp���� 
	 */
	public static boolean isInTransaction() {
		return !transactionContext.get().isEmpty();
	}
	
    /**
     * �g�����U�N�V�����Ɋ֘A����R���e�L�X�g�}�b�v���擾����
     */
    public static Map<Object, Object> getTransactionContext() {
        Stack<Map> stack2 = transactionContext.get();
        return (stack2.empty()) ? null : stack2.peek();
    }

    /**
	 * �g�����U�N�V�������J�n����Interceptor���X�^�b�N�ɐς�
	 * @param ti
	 */
    public static void pushTransactionBeginingInterceptor(TransactionInterceptor ti) {
        Stack stack = (Stack)transactionBeginingStack.get();
        stack.push(ti);
        Stack<Map> stack2 = transactionContext.get();
        stack2.push(new HashMap());
    }
    
    /**
     * �g�����U�N�V�������J�n����Interceptor���X�^�b�N���珜������
     *
     */
    public static void popTransactionBeginingInterceptor() {
        Stack stack = (Stack)transactionBeginingStack.get();
        stack.pop();
        Stack<Map> stack2 = transactionContext.get();
        stack2.pop();
    }
    
    /**
     * true�̏ꍇ�A���݂̃g�����U�N�V�����͕K�����[���o�b�N�����
     * @return
     */
    public static boolean isRollbackOnly() {
        Stack stack = (Stack)transactionBeginingStack.get();
        TransactionInterceptor ti = (TransactionInterceptor)stack.peek();
        return ti.isRollbackOnly();
    }
    
    /**
     * �Ăяo�������_�̃g�����U�N�V�����̓��[���o�b�N�I�����[�ƂȂ�
     */
    public static void setRollBackOnly() {
        Stack stack = (Stack)transactionBeginingStack.get();
        TransactionInterceptor ti = (TransactionInterceptor)stack.peek();
        ti.setRollbackOnly();
    }
}
