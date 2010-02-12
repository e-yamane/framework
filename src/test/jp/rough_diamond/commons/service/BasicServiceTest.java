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
		assertEquals("全体件数が誤ってます。", 5, fr.count);
		assertEquals("取得件数が誤っています。", 1, fr.list.size());
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
			assertEquals("返却値が数が誤っています。", 1, list.size());
			assertEquals("コールバックメソッド呼び出し回数が誤っています。", 6, list.get(0).list.size());
			int index = 0;
			assertEquals("コールバックメソッド呼び出し順序が誤っています.", "CallbackListener#postLoad5", 	list.get(0).list.get(index++));
			assertEquals("コールバックメソッド呼び出し順序が誤っています.", "postLoad3", 					list.get(0).list.get(index++));
			assertEquals("コールバックメソッド呼び出し順序が誤っています.", "CallbackListener#apostLoad3", 	list.get(0).list.get(index++));
			assertEquals("コールバックメソッド呼び出し順序が誤っています.", "CallbackListener#postLoad3", 	list.get(0).list.get(index++));
			assertEquals("コールバックメソッド呼び出し順序が誤っています.", "postLoad1", 					list.get(0).list.get(index++));
			assertEquals("コールバックメソッド呼び出し順序が誤っています.", "CallbackListener#postLoad1", 	list.get(0).list.get(index++));
		} finally {
			DIContainerFactory.setDIContainer(real);
		}
	}
	
	public static class CallbackListener {
		//自己参照イベントより後に来ること
		@PostLoad(priority = 3)
		public void postLoad3(CallbackTestType object) {
			object.list.add("CallbackListener#postLoad3");
		}
		//postLoad3と優先順位が一緒の場合は文字コードの昇順に実行されること
		@PostLoad(priority = 3)
		public void aPostLoad3(CallbackTestType object) {
			object.list.add("CallbackListener#apostLoad3");
		}
		//引数なしの場合は呼び出されないこと
		@PostLoad(priority = 3)
		public void postLoad3() { 
			Assert.fail("呼ばれてはいけないメソッドが呼び出されています");
		}
		//違うクラスの場合は呼び出されないこと
		@PostLoad(priority = 3)
		public void postLoad3(String str) { 
			Assert.fail("呼ばれてはいけないメソッドが呼び出されています");
		}
		//親クラスの場合は受け入れ可能であること
		@PostLoad(priority=1)
		public void postLoad1(Object o) {
			((CallbackTestType)o).list.add("CallbackListener#postLoad1");
		}
		//EventTypeを受ける時も呼び出されること
		@PostLoad(priority=5)
		public void postLoad5(Object o, CallbackEventType type) {
			((CallbackTestType)o).list.add("CallbackListener#postLoad5");
		}
		//引数３つ以上は呼び出されないこと
		@PostLoad(priority=5)
		public void postLoad5(Object o, CallbackEventType type, String hoge) {
			Assert.fail("呼ばれてはいけないメソッドが呼び出されています");
		}
		//コールバックアノテーションがないので呼び出されないこと
		public void foo(Object o) {
			Assert.fail("呼ばれてはいけないメソッドが呼び出されています");
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
