package jp.rough_diamond.framework.transaction;

import jp.rough_diamond.framework.service.Service;
import jp.rough_diamond.framework.service.ServiceLocator;
import junit.framework.TestCase;

public class TransactionManagerTest extends TestCase {
	public void testIsInTransaction() {
		assertFalse("トランザクション外なのにtrueが返却されています。", TransactionManager.isInTransaction());
		ServiceLocator.getService(ServiceExt.class).doIt();
	}

	public static class ServiceExt implements Service {
		public void doIt() {
			assertTrue("トランザクション内なのにtrueが返却されています", TransactionManager.isInTransaction());
		}
	}
}
