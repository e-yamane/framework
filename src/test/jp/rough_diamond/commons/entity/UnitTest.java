package jp.rough_diamond.commons.entity;

import java.math.BigDecimal;
import java.util.List;

import jp.rough_diamond.commons.resource.Message;
import jp.rough_diamond.commons.resource.MessagesIncludingException;
import jp.rough_diamond.commons.service.BasicService;
import jp.rough_diamond.commons.testdata.UnitLoader;
import jp.rough_diamond.commons.testing.DataLoadingTestCase;

public class UnitTest extends DataLoadingTestCase {

	protected void setUp() throws Exception {
		super.setUp();
		UnitLoader.init();
	}

	public void testGateRate() throws Exception {
		Unit u = BasicService.getService().findByPK(Unit.class, 1L);
		BigDecimal bd = u.getRate();
		assertEquals("�l������Ă��܂��B", 2, bd.intValue() + 1);
	}
	
	public void testInsertNormalCase() throws Exception {
		Unit u = new Unit();
		u.setName("�ق��ق�");
		u.setBase(u);
		u.setRateStr("1");
		u.setScale(1);
		BasicService.getService().insert(u);
	}
	
	public void testCheckRateFormatWhenInsert() throws Exception {
		BasicService service = BasicService.getService();
		Unit u = new Unit();
		u.setName("�ق��ق�");
		u.setBase(u);
		u.setRateStr("xx");
		u.setScale(1);
		try {
			service.insert(u);
			fail("��O�����o����Ă��܂���B");
		} catch(MessagesIncludingException e) {
			List<Message> msgList = e.getMessages().get("Unit.rateStr");
			assertEquals("�ԋp��������Ă��܂��B", 1, msgList.size());
			assertEquals("�G���[���b�Z�[�W�L�[������Ă��܂��B", "errors.format.number", msgList.get(0).getKey());
		}
	}

	public void testCheckRateFormatWhenUpdate() throws Exception {
		BasicService service = BasicService.getService();
		Unit u = service.findByPK(Unit.class, 1L);
		u.setRateStr("xx");
		try {
			service.update(u);
			fail("��O�����o����Ă��܂���B");
		} catch(MessagesIncludingException e) {
			List<Message> msgList = e.getMessages().get("Unit.rateStr");
			assertEquals("�ԋp��������Ă��܂��B", 1, msgList.size());
			assertEquals("�G���[���b�Z�[�W�L�[������Ă��܂��B", "errors.format.number", msgList.get(0).getKey());
		}
	}
	
	public void testRiviseBaseRate() throws Exception {
		BasicService service = BasicService.getService();
		Unit u = service.findByPK(Unit.class, 1L);
		u.setName("xxx");
		u.setRateStr("10");
		service.update(u);
		u = service.findByPK(Unit.class, 1L);
		assertEquals("�X�V����Ă��܂���B", "xxx", u.getName());
		assertEquals("���[�g���␳����Ă��܂���B", "1", u.getRateStr());
	}
	
	public void testIsBaseUnit() throws Exception {
		Unit u = new Unit();
		assertFalse(u.isBaseUnit());
		u.setBase(u);
		assertTrue(u.isBaseUnit());
		Unit u2 = new Unit();
		u.setBase(u2);
		assertFalse(u.isBaseUnit());
		u.setId(1L);
		u2.setId(2L);
		assertFalse(u.isBaseUnit());
		u2.setId(1L);
		assertTrue(u.isBaseUnit());
	}
}
