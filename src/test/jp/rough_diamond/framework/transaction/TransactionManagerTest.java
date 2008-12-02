package jp.rough_diamond.framework.transaction;

import jp.rough_diamond.framework.service.Service;
import jp.rough_diamond.framework.service.ServiceLocator;
import junit.framework.TestCase;

public class TransactionManagerTest extends TestCase {
	public void testIsInTransaction() {
		assertFalse("�g�����U�N�V�����O�Ȃ̂�true���ԋp����Ă��܂��B", TransactionManager.isInTransaction());
		ServiceLocator.getService(ServiceExt.class).doIt();
	}

	public static class ServiceExt implements Service {
		public void doIt() {
			assertTrue("�g�����U�N�V�������Ȃ̂�true���ԋp����Ă��܂�", TransactionManager.isInTransaction());
		}
	}
}
