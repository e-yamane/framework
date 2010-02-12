/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */

package jp.rough_diamond.commons.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.rough_diamond.commons.di.CompositeDIContainer;
import jp.rough_diamond.commons.di.DIContainer;
import jp.rough_diamond.commons.di.DIContainerFactory;
import jp.rough_diamond.commons.di.MapDIContainer;
import jp.rough_diamond.commons.entity.Unit;
import jp.rough_diamond.commons.extractor.Condition;
import jp.rough_diamond.commons.extractor.ExtractValue;
import jp.rough_diamond.commons.extractor.Extractor;
import jp.rough_diamond.commons.extractor.Property;
import jp.rough_diamond.commons.service.annotation.PostLoad;
import jp.rough_diamond.commons.testdata.NumberingLoader;
import jp.rough_diamond.commons.testdata.UnitLoader;
import jp.rough_diamond.commons.testing.DataLoadingTestCase;
import jp.rough_diamond.commons.testing.Loader;
import junit.framework.Assert;

/**
 *
 */
public class BasicServiceTest extends DataLoadingTestCase {
	protected void setUp() throws Exception {
		super.setUp();
		Loader.load(UnitLoader.class);
		Loader.load(NumberingLoader.class);
	}

	public void testFindByExtractorWithCount() throws Exception {
		Extractor e = new Extractor(Unit.class);
		e.setLimit(1);
		FindResult<Unit> fr = BasicService.getService().findByExtractorWithCount(e);
		assertEquals("�S�̌���������Ă܂��B", 5, fr.count);
		assertEquals("�擾����������Ă��܂��B", 1, fr.list.size());
	}
	
	public void testEventCallback() throws Exception {
		List<Object> list2 = new ArrayList<Object>();
		list2.add(new CallbackListener());
		DIContainer real = DIContainerFactory.getDIContainer();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put(BasicService.PERSISTENCE_EVENT_LISTENERS, list2);
		MapDIContainer wrapper = new MapDIContainer(map);
		DIContainerFactory.setDIContainer(new CompositeDIContainer(
				Arrays.asList(new DIContainer[]{wrapper, real})));
		System.out.println(DIContainerFactory.getDIContainer().getClass().getName());
		try {
			Extractor e = new Extractor(Unit.class);
			e.addExtractValue(new ExtractValue("id", new Property(Unit.ID)));
			e.add(Condition.eq(new Property(Unit.ID), 1));
			e.setReturnType(CallbackTestType.class);
			System.out.println(DIContainerFactory.getDIContainer().getClass().getName());
			List<CallbackTestType> list = BasicService.getService().findByExtractor(e);
			System.out.println(DIContainerFactory.getDIContainer().getClass().getName());
			assertEquals("�ԋp�l����������Ă��܂��B", 1, list.size());
			assertEquals("�R�[���o�b�N���\�b�h�Ăяo���񐔂�����Ă��܂��B", 6, list.get(0).list.size());
			int index = 0;
			assertEquals("�R�[���o�b�N���\�b�h�Ăяo������������Ă��܂�.", "CallbackListener#postLoad5", 	list.get(0).list.get(index++));
			assertEquals("�R�[���o�b�N���\�b�h�Ăяo������������Ă��܂�.", "postLoad3", 					list.get(0).list.get(index++));
			assertEquals("�R�[���o�b�N���\�b�h�Ăяo������������Ă��܂�.", "CallbackListener#apostLoad3", 	list.get(0).list.get(index++));
			assertEquals("�R�[���o�b�N���\�b�h�Ăяo������������Ă��܂�.", "CallbackListener#postLoad3", 	list.get(0).list.get(index++));
			assertEquals("�R�[���o�b�N���\�b�h�Ăяo������������Ă��܂�.", "postLoad1", 					list.get(0).list.get(index++));
			assertEquals("�R�[���o�b�N���\�b�h�Ăяo������������Ă��܂�.", "CallbackListener#postLoad1", 	list.get(0).list.get(index++));
		} finally {
			DIContainerFactory.setDIContainer(real);
		}
	}
	
	public static class CallbackListener {
		//���ȎQ�ƃC�x���g����ɗ��邱��
		@PostLoad(priority = 3)
		public void postLoad3(CallbackTestType object) {
			object.list.add("CallbackListener#postLoad3");
		}
		//postLoad3�ƗD�揇�ʂ��ꏏ�̏ꍇ�͕����R�[�h�̏����Ɏ��s����邱��
		@PostLoad(priority = 3)
		public void aPostLoad3(CallbackTestType object) {
			object.list.add("CallbackListener#apostLoad3");
		}
		//�����Ȃ��̏ꍇ�͌Ăяo����Ȃ�����
		@PostLoad(priority = 3)
		public void postLoad3() { 
			Assert.fail("�Ă΂�Ă͂����Ȃ����\�b�h���Ăяo����Ă��܂�");
		}
		//�Ⴄ�N���X�̏ꍇ�͌Ăяo����Ȃ�����
		@PostLoad(priority = 3)
		public void postLoad3(String str) { 
			Assert.fail("�Ă΂�Ă͂����Ȃ����\�b�h���Ăяo����Ă��܂�");
		}
		//�e�N���X�̏ꍇ�͎󂯓���\�ł��邱��
		@PostLoad(priority=1)
		public void postLoad1(Object o) {
			((CallbackTestType)o).list.add("CallbackListener#postLoad1");
		}
		//EventType���󂯂鎞���Ăяo����邱��
		@PostLoad(priority=5)
		public void postLoad5(Object o, CallbackEventType type) {
			((CallbackTestType)o).list.add("CallbackListener#postLoad5");
		}
		//�����R�ȏ�͌Ăяo����Ȃ�����
		@PostLoad(priority=5)
		public void postLoad5(Object o, CallbackEventType type, String hoge) {
			Assert.fail("�Ă΂�Ă͂����Ȃ����\�b�h���Ăяo����Ă��܂�");
		}
		//�R�[���o�b�N�A�m�e�[�V�������Ȃ��̂ŌĂяo����Ȃ�����
		public void foo(Object o) {
			Assert.fail("�Ă΂�Ă͂����Ȃ����\�b�h���Ăяo����Ă��܂�");
		}
	}
	
	public static class CallbackTestType {
		private List<String> list = new ArrayList<String>();
		public CallbackTestType(Long id) { }
		
		@PostLoad(priority = 3)
		public void postLoad3() {
			list.add("postLoad3");
		}

		@PostLoad(priority = 1)
		public void postLoad1() {
			list.add("postLoad1");
		}
	}
}
