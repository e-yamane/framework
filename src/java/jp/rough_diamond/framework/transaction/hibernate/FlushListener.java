/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */

package jp.rough_diamond.framework.transaction.hibernate;

import java.util.Stack;

import org.hibernate.HibernateException;
import org.hibernate.event.AutoFlushEvent;
import org.hibernate.event.AutoFlushEventListener;
import org.hibernate.event.FlushEntityEvent;
import org.hibernate.event.FlushEntityEventListener;
import org.hibernate.event.def.DefaultAutoFlushEventListener;
import org.hibernate.event.def.DefaultFlushEntityEventListener;

public class FlushListener {
	private final static ThreadLocal<Stack<Boolean>> isFlushing = new ThreadLocal<Stack<Boolean>>(){
		@Override
		protected Stack<Boolean> initialValue() {
			return new Stack<Boolean>();
		}
	};
	
	public static boolean isFlushing() {
		return (isFlushing.get().size() != 0);
	}
	
	public static class FlushListenerInner implements FlushEntityEventListener {
		private static final long serialVersionUID = 1L;
		FlushEntityEventListener baseListener;
		public FlushListenerInner() {
			this(new DefaultFlushEntityEventListener());
		}
		public FlushListenerInner(FlushEntityEventListener listener) {
			baseListener = listener;
		}
		@Override
		public void onFlushEntity(FlushEntityEvent event) throws HibernateException {
			isFlushing.get().push(Boolean.TRUE);
			try {
				baseListener.onFlushEntity(event);
			} finally {
				isFlushing.get().pop();
			}
		}
	}

	public static class AutoFlushListenerInner implements AutoFlushEventListener {
		private static final long serialVersionUID = 1L;
		AutoFlushEventListener baseListener;
		public AutoFlushListenerInner() {
			this(new DefaultAutoFlushEventListener());
		}
		public AutoFlushListenerInner(AutoFlushEventListener listener) {
			baseListener = listener;
		}
		@Override
		public void onAutoFlush(AutoFlushEvent event) throws HibernateException {
			isFlushing.get().push(Boolean.TRUE);
			try {
				baseListener.onAutoFlush(event);
			} finally {
				isFlushing.get().pop();
			}
		}
	}
}
