/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */

package jp.rough_diamond.commons.service.hibernate;


import java.util.List;
import java.util.Map;

import jp.rough_diamond.commons.entity.Numbering;
import jp.rough_diamond.commons.entity.ScalableNumber;
import jp.rough_diamond.commons.entity.Unit;
import jp.rough_diamond.commons.extractor.Avg;
import jp.rough_diamond.commons.extractor.Condition;
import jp.rough_diamond.commons.extractor.Count;
import jp.rough_diamond.commons.extractor.ExtractValue;
import jp.rough_diamond.commons.extractor.Extractor;
import jp.rough_diamond.commons.extractor.FreeFormat;
import jp.rough_diamond.commons.extractor.Max;
import jp.rough_diamond.commons.extractor.Min;
import jp.rough_diamond.commons.extractor.Order;
import jp.rough_diamond.commons.extractor.Property;
import jp.rough_diamond.commons.extractor.Sum;
import jp.rough_diamond.commons.service.BasicService;
import jp.rough_diamond.commons.testdata.NumberingLoader;
import jp.rough_diamond.commons.testdata.UnitLoader;
import jp.rough_diamond.commons.testing.DataLoadingTestCase;
import jp.rough_diamond.framework.service.Service;
import jp.rough_diamond.framework.service.ServiceLocator;

/**
 *
 */
public class Extractor2HQLTest extends DataLoadingTestCase {
	protected void setUp() throws Exception {
		super.setUp();
		NumberingLoader.init();
		UnitLoader.init();
	}

	public void testFetchSizeの指定が正しく行われていること() throws Exception {
		//パスのみの確認です。
		//きちんとロジック分岐していることをCoberturaで目視で確認するのみ
		ServiceLocator.getService(CreateQueryService.class).getQuery(100);
		ServiceLocator.getService(CreateQueryService.class).getQuery(Extractor.DEFAULT_FETCH_SIZE);
		BasicService.getService().findAll(Numbering.class, 100);
		BasicService.getService().findAll(Numbering.class, Extractor.DEFAULT_FETCH_SIZE);
	}
	
	@SuppressWarnings("deprecation")
	public void testQueryUsingLegacyStyle() throws Exception {
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("name", Unit.class, null, Unit.NAME));
		ex.add(Condition.eq(Unit.ID, 3L));
		List<Map<String, Object>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 1, list.size());
		assertEquals("値の取得に失敗しました。", "マイル", list.get(0).get("name"));
	}
	
	public void testQueryUsingProperty() throws Exception {
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("name", new Property(Unit.NAME)));
		ex.add(Condition.eq(new Property(Unit.ID), 3L));
		List<Map<String, Object>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 1, list.size());
		assertEquals("値の取得に失敗しました。", "マイル", list.get(0).get("name"));
	}

	public void testQueryUsingMax() throws Exception {
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue(
				"max", new Max(new Property(Unit.RATE + ScalableNumber.VALUE))));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		List<Map<String, Long>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 1, list.size());
		assertEquals("値の取得に失敗しました。", 1609344L, list.get(0).get("max").longValue());
	}
	
	public void testQueryUsingMin() throws Exception {
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue(
				"min", new Min(new Property(Unit.RATE + ScalableNumber.VALUE))));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		List<Map<String, Long>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 1, list.size());
		assertEquals("値の取得に失敗しました。", 1L, list.get(0).get("min").longValue());
	}
	
	public void testQueryUsingSum() throws Exception {
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue(
				"sum", new Sum(new Property(Unit.RATE + ScalableNumber.VALUE))));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		List<Map<String, Long>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 1, list.size());
		assertEquals("値の取得に失敗しました。", 1610347L, list.get(0).get("sum").longValue());
	}
	
	public void testQueryUsingAvg() throws Exception {
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue(
				"avg", new Avg(new Property(Unit.RATE + ScalableNumber.VALUE))));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		List<Map<String, Double>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 1, list.size());
		System.out.println(list.get(0).get("avg"));
		//誤差があるのでassertはしない
	}

	public void testQueryUsingSumAndGroupBy() throws Exception {
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("base", new Property(Unit.BASE + "." + Unit.ID)));
		ex.addExtractValue(new ExtractValue(
				"sum", new Sum(new Property(Unit.RATE + ScalableNumber.VALUE))));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(Unit.BASE + "." + Unit.ID));
		List<Map<String, Long>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 2, list.size());
		assertEquals("値が誤っています。", 1610346L, list.get(0).get("sum").longValue());
		assertEquals("値が誤っています。", 1L, list.get(1).get("sum").longValue());
	}
	
	public void testQueryUsingSumAndGroupByGreaterThanTwo() throws Exception {
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("base", new Property(Unit.BASE + "." + Unit.ID)));
		Sum sum = new Sum(new Property(Unit.RATE + ScalableNumber.VALUE));
		ex.addExtractValue(new ExtractValue("sum", sum));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(Unit.BASE + "." + Unit.ID));
		ex.addHaving(Condition.gt(sum, 2L));
		List<Map<String, Long>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 1, list.size());
		assertEquals("値が誤っています。", 1610346L, list.get(0).get("sum").longValue());
	}
	
	public void testQueryUsingCount() throws Exception {
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("base", new Property(Unit.BASE + "." + Unit.ID)));
		ex.addExtractValue(new ExtractValue("count", new Count()));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(Unit.BASE + "." + Unit.ID));
		List<Map<String, Long>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 2, list.size());
		assertEquals("値が誤っています。", 4L, list.get(0).get("count").longValue());
		assertEquals("値が誤っています。", 1L, list.get(1).get("count").longValue());
		
		ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("base", new Property(Unit.BASE + "." + Unit.ID)));
		ex.addExtractValue(new ExtractValue("count", new Count(new Property(Unit.RATE + ScalableNumber.VALUE))));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(Unit.BASE + "." + Unit.ID));
		list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 2, list.size());
		assertEquals("値が誤っています。", 4L, list.get(0).get("count").longValue());
		assertEquals("値が誤っています。", 1L, list.get(1).get("count").longValue());

		ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("base", new Property(Unit.BASE + "." + Unit.ID)));
		ex.addExtractValue(new ExtractValue("count", new Count(new Property(Unit.RATE + ScalableNumber.VALUE), true)));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(Unit.BASE + "." + Unit.ID));
		list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 2, list.size());
		assertEquals("値が誤っています。", 3L, list.get(0).get("count").longValue());
		assertEquals("値が誤っています。", 1L, list.get(1).get("count").longValue());
	}
	
	public void testFreeFormat() throws Exception {
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("val", new FreeFormat("3*(1 + 1)")));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(Unit.ID));
		List<Map<String, ? extends Number>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 5, list.size());
		assertEquals("値が誤っています。", 6L, list.get(0).get("val").longValue());

		ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("val", new FreeFormat("3*(? + 1)", 2L)));
		ex.addOrder(Order.asc(Unit.ID));
		list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 5, list.size());
		assertEquals("値が誤っています。", 9L, list.get(0).get("val").longValue());

		ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("val", new FreeFormat("3*(? + ?)", 2L, new Property(Unit.RATE + ScalableNumber.VALUE))));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(Unit.ID));
		list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 5, list.size());
		assertEquals("値が誤っています。", 9L, 			list.get(0).get("val").longValue());
		assertEquals("値が誤っています。", 3006L, 		list.get(1).get("val").longValue());
		assertEquals("値が誤っています。", 4828038L, 	list.get(2).get("val").longValue());
		assertEquals("値が誤っています。", 9L, 			list.get(3).get("val").longValue());
		assertEquals("値が誤っています。", 9L, 			list.get(4).get("val").longValue());

		ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("val", new FreeFormat("?*power(10, ?)", 
				new Property(Unit.RATE + ScalableNumber.VALUE), new Property(Unit.RATE + ScalableNumber.SCALE))));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(Unit.ID));
		list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 5, list.size());
		assertEquals("値が誤っています。", 1L,			list.get(0).get("val").longValue());
		assertEquals("値が誤っています。", 1000L,		list.get(1).get("val").longValue());
		assertEquals("値が誤っています。", 1609344000L,	list.get(2).get("val").longValue());
	}
	
	public void testSumInFreeFormat() throws Exception {
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("base", new Property(Unit.BASE + "." + Unit.ID)));
		Sum sum = new Sum(new Property(Unit.RATE + ScalableNumber.VALUE));
		ex.addExtractValue(new ExtractValue("sum", new FreeFormat("?", sum)));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(Unit.BASE + "." + Unit.ID));
		ex.addHaving(Condition.gt(sum, 2L));
		List<Map<String, Long>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 1, list.size());
		assertEquals("値が誤っています。", 1610346L, list.get(0).get("sum").longValue());
	}
	
	public void testSumInFreeFormatAndGrupByColumnInFreeFormat() throws Exception {
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("base", new FreeFormat("?", new Property(Unit.BASE + "." + Unit.ID))));
		Sum sum = new Sum(new Property(Unit.RATE + ScalableNumber.VALUE));
		ex.addExtractValue(new ExtractValue("sum", new FreeFormat("?", sum)));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(Unit.BASE + "." + Unit.ID));
		ex.addHaving(Condition.gt(sum, 2L));
		List<Map<String, Long>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 1, list.size());
		assertEquals("値が誤っています。", 1610346L, list.get(0).get("sum").longValue());
	}
	
	public void testFreeFormatInWhereCondition() throws Exception {
		Extractor ex = new Extractor(Unit.class);
		Property p = new Property(Unit.ID);
		FreeFormat ff = new FreeFormat("? * 3", p);
		ex.addExtractValue(new ExtractValue("val", ff));
		ex.add(Condition.le(p, 5L));
		ex.add(Condition.lt(ff, 10));
		ex.addOrder(Order.asc(Unit.ID));
		List<Map<String, ? extends Number>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 3, list.size());
	}
	
	public void testFreeFormatInHavingCondition() throws Exception {
		Extractor ex = new Extractor(Unit.class);
		Property p = new Property(Unit.ID);
		Sum sum = new Sum(new Property(Unit.RATE + ScalableNumber.VALUE));
		FreeFormat ff = new FreeFormat("? * 3", sum);
		ex.addExtractValue(new ExtractValue("base", new Property(Unit.BASE + "." + Unit.ID)));
		ex.addExtractValue(new ExtractValue("val", ff));
		ex.add(Condition.le(p, 5L));
		ex.addHaving(Condition.lt(ff, 10));
		List<Map<String, ? extends Number>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 1, list.size());
	}
	
	public static class CreateQueryService implements Service {
		public void getQuery(int fetchSize) {
			Extractor ex = new Extractor(Numbering.class);
			ex.setFetchSize(fetchSize);
			BasicService.getService().findByExtractor(ex);
		}
	}
}
