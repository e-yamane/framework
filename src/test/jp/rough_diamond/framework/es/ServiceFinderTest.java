package jp.rough_diamond.framework.es;


import jp.rough_diamond.framework.service.Service;
import junit.framework.TestCase;

public class ServiceFinderTest extends TestCase {

	public void testIsTarget() throws Exception {
		// TODO �e�X�g���@�m����ɍēx�e�X�g
//		assertFalse(ServiceFinder.isTarget(Service1.class));
//		assertTrue(ServiceFinder.isTarget(Service2.class));
//		assertFalse(ServiceFinder.isTarget(Service3.class));
//		
//		ServiceFinder sf = new ServiceFinder();
//		Service2 s = sf.getService(Service2.class);
//		s.hoge("xyz");
//		s.poge();
	}
	
	public static interface Service1 extends Service { }
	public static interface Service2 extends EnterpriseService {
		@ServiceConnecter(serviceName="hoge")
		public void hoge(String xyz);
		public void poge();
	}
	public static class Service3 implements EnterpriseService { }
}
