/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */

package jp.rough_diamond.framework.transaction.hibernate;

import org.hibernate.event.SaveOrUpdateEvent;
import org.hibernate.event.SaveOrUpdateEventListener;
import org.hibernate.event.def.DefaultSaveEventListener;
import org.hibernate.event.def.DefaultSaveOrUpdateEventListener;
import org.hibernate.event.def.DefaultUpdateEventListener;

/**
 *
 */
public class SaveOrUpdateListener {
	private final static ThreadLocal<Boolean> isSaveingOrUpdating = new ThreadLocal<Boolean>();
	
	public static boolean isSaveingOrUpdating() {
		return (Boolean.TRUE.equals(isSaveingOrUpdating.get()));
	}

	static void onSaveOrUpdate(SaveOrUpdateEventListener listener, SaveOrUpdateEvent event) {
		isSaveingOrUpdating.set(Boolean.TRUE);
		try {
			listener.onSaveOrUpdate(event);
		} finally {
			isSaveingOrUpdating.remove();
		}
	}
	
	public static class SaveOrUpdateListenerInner implements SaveOrUpdateEventListener {
		private static final long serialVersionUID = 1L;
		SaveOrUpdateEventListener baseListener; 
		public SaveOrUpdateListenerInner() {
			this(new DefaultSaveOrUpdateEventListener());
		}
		public SaveOrUpdateListenerInner(SaveOrUpdateEventListener listener) {
			baseListener = listener;
		}
		@Override
		public void onSaveOrUpdate(SaveOrUpdateEvent event) {
			SaveOrUpdateListener.onSaveOrUpdate(baseListener, event);
		}
	}
	
	public static class SaveListenerInner implements SaveOrUpdateEventListener {
		private static final long serialVersionUID = 1L;
		SaveOrUpdateEventListener baseListener; 
		public SaveListenerInner() {
			this(new DefaultSaveEventListener());
		}
		public SaveListenerInner(SaveOrUpdateEventListener listener) {
			baseListener = listener;
		}
		@Override
		public void onSaveOrUpdate(SaveOrUpdateEvent event) {
			SaveOrUpdateListener.onSaveOrUpdate(baseListener, event);
		}
	}

	public static class UpdateListenerInner implements SaveOrUpdateEventListener {
		private static final long serialVersionUID = 1L;
		SaveOrUpdateEventListener baseListener;
		public UpdateListenerInner() {
			this(new DefaultUpdateEventListener());
		}
		public UpdateListenerInner(SaveOrUpdateEventListener listener) {
			baseListener = listener;
		}
		@Override
		public void onSaveOrUpdate(SaveOrUpdateEvent event) {
			SaveOrUpdateListener.onSaveOrUpdate(baseListener, event);
		}
	}
}
