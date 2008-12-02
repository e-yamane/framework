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
 * トランザクションマネージャ
 * JDK 5.0以前のバージョンでは本マネージャを使用すること
 * トランザクション属性は、setTransactionMapに渡された情報に基づいて行われる。
 * transactionMapは、キーはクラス名、値はトランザクション属性文字列
 * （REQUIRED or REQUIRED_NEW or NOP)を指定する。
 * 省略された場合は、「REQUIRED」である
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
	 * トランザクション属性マップをセットする（DI用）
	 * キーはクラス名、値はトランザクション属性文字列（REQUIRED or REQUIRED_NEW or NOP)
	 * @param map
	 */
	public void setTransactionMap(Map map) {
		this.transactionMap = map;
	}
	
	/**
	 * トランザクション属性マップを取得する
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
	 * 何らかのトランザクション無いか否かを返却する 
	 */
	public static boolean isInTransaction() {
		return !transactionContext.get().isEmpty();
	}
	
    /**
     * トランザクションに関連するコンテキストマップを取得する
     */
    public static Map<Object, Object> getTransactionContext() {
        Stack<Map> stack2 = transactionContext.get();
        return (stack2.empty()) ? null : stack2.peek();
    }

    /**
	 * トランザクションを開始したInterceptorをスタックに積む
	 * @param ti
	 */
    public static void pushTransactionBeginingInterceptor(TransactionInterceptor ti) {
        Stack stack = (Stack)transactionBeginingStack.get();
        stack.push(ti);
        Stack<Map> stack2 = transactionContext.get();
        stack2.push(new HashMap());
    }
    
    /**
     * トランザクションを開始したInterceptorをスタックから除去する
     *
     */
    public static void popTransactionBeginingInterceptor() {
        Stack stack = (Stack)transactionBeginingStack.get();
        stack.pop();
        Stack<Map> stack2 = transactionContext.get();
        stack2.pop();
    }
    
    /**
     * trueの場合、現在のトランザクションは必ずロールバックされる
     * @return
     */
    public static boolean isRollbackOnly() {
        Stack stack = (Stack)transactionBeginingStack.get();
        TransactionInterceptor ti = (TransactionInterceptor)stack.peek();
        return ti.isRollbackOnly();
    }
    
    /**
     * 呼び出した時点のトランザクションはロールバックオンリーとなる
     */
    public static void setRollBackOnly() {
        Stack stack = (Stack)transactionBeginingStack.get();
        TransactionInterceptor ti = (TransactionInterceptor)stack.peek();
        ti.setRollbackOnly();
    }
}
