/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.framework.es;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.MuleServer;
import org.mule.api.MuleContext;

import jp.rough_diamond.commons.di.DIContainerFactory;

public class ServiceBus {
	private final static Log log = LogFactory.getLog(ServiceBus.class);
	
	public static ServiceBus getInstance() {
		ServiceBus bus = (ServiceBus)DIContainerFactory.getDIContainer().getObject("serviceBus");
		bus.init();
		return bus;
	}
	
	private String config;
	public void setConfig(String config) {
		this.config = config;
	}
	
	public MuleContext getContext() {
		return MuleServer.getMuleContext();
	}
	
	synchronized void init() {
		MuleContext context = getContext();
		if(context == null) {
			startMule();
		}
	}

	private Object muleStartMonitor = new Object();
	private volatile boolean isStart = false;
	private void startMule() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					log.debug("MuleServerÇäJénÇµÇ‹Ç∑ÅB");
					MuleServer server;
					if(config == null) {
						server = new MuleServer();
					} else {
						server = new MuleServer(new String[]{"-config", config});
					}
					server.start(false, true);
				} catch(Exception e) {
					throw new RuntimeException(e);
				} finally {
					synchronized(muleStartMonitor) {
						isStart = true;
						muleStartMonitor.notifyAll();
					}
				}
			}
		});
		synchronized(muleStartMonitor) {
			isStart = false;
			t.setDaemon(true);
			t.start();
			try {
				while(true) {
					muleStartMonitor.wait(100);
					if(isStart) {
						break;
					}
				}
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
