package jp.rough_diamond.framework.es;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * <p>
 * <a href="BaseListener.java.html"> <i>View Source </i> </a>
 * </p>
 */
public class MuleListener implements ServletContextListener {
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	public void contextInitialized(ServletContextEvent arg0) {
        ServiceBus.getInstance();
	}
}