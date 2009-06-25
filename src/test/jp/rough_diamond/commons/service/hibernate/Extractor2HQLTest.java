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
import jp.rough_diamond.commons.extractor.CombineCondition;
import jp.rough_diamond.commons.extractor.Condition;
import jp.rough_diamond.commons.extractor.Count;
import jp.rough_diamond.commons.extractor.ExtractValue;
import jp.rough_diamond.commons.extractor.Extractor;
import jp.rough_diamond.commons.extractor.FreeFormat;
import jp.rough_diamond.commons.extractor.InnerJoin;
import jp.rough_diamond.commons.extractor.Max;
import jp.rough_diamond.commons.extractor.Min;
import jp.rough_diamond.commons.extractor.Order;
import jp.rough_diamond.commons.extractor.Property;
import jp.rough_diamond.commons.extractor.Sum;
import jp.rough_diamond.commons.service.BasicService;
import jp.rough_diamond.commons.service.annotation.PostLoad;
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
		//select name from unit where id = 3;
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("name", Unit.class, null, Unit.NAME));
		ex.add(Condition.eq(Unit.ID, 3L));
		List<Map<String, Object>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 1, list.size());
		assertEquals("値の取得に失敗しました。", "マイル", list.get(0).get("name"));
	}
	
	public void testQueryUsingProperty() throws Exception {
		//select name from unit where id = 3;
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("name", new Property(Unit.NAME)));
		ex.add(Condition.eq(new Property(Unit.ID), 3L));
		List<Map<String, Object>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 1, list.size());
		assertEquals("値の取得に失敗しました。", "マイル", list.get(0).get("name"));
	}

	public void testQueryUsingMax() throws Exception {
		//select max(rate_value) from unit where id <= 5
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue(
				"max", new Max(new Property(Unit.RATE + ScalableNumber.VALUE))));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		List<Map<String, Long>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 1, list.size());
		assertEquals("値の取得に失敗しました。", 1609344L, list.get(0).get("max").longValue());
	}
	
	public void testQueryUsingMin() throws Exception {
		//select min(rate_value) from unit where id <= 5
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue(
				"min", new Min(new Property(Unit.RATE + ScalableNumber.VALUE))));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		List<Map<String, Long>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 1, list.size());
		assertEquals("値の取得に失敗しました。", 1L, list.get(0).get("min").longValue());
	}
	
	public void testQueryUsingSum() throws Exception {
		//select sum(rate_value) from unit where id <= 5
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue(
				"sum", new Sum(new Property(Unit.RATE + ScalableNumber.VALUE))));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		List<Map<String, Long>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 1, list.size());
		assertEquals("値の取得に失敗しました。", 1610347L, list.get(0).get("sum").longValue());
	}
	
	public void testQueryUsingAvg() throws Exception {
		//select avg(rate_value) from unit where id <= 5
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue(
				"avg", new Avg(new Property(Unit.RATE + ScalableNumber.VALUE))));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		List<Map<String, Double>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 1, list.size());
		System.out.println(list.get(0).get("avg"));
		//誤差があるのでintで切り捨て
		assertEquals("返却値が誤っています。", 322069, list.get(0).get("avg").intValue());
	}

	public void testQueryUsingSumAndGroupBy() throws Exception {
		//select base_unit_id, sum(rate_value) from unit where id <= 5 group by base_unit_id order by base_unit_id
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("base", new Property(Unit.BASE + "." + Unit.ID)));
		ex.addExtractValue(new ExtractValue(
				"sum", new Sum(new Property(Unit.RATE + ScalableNumber.VALUE))));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.BASE + "." + Unit.ID)));
		List<Map<String, Long>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 2, list.size());
		assertEquals("値が誤っています。", 1610346L, list.get(0).get("sum").longValue());
		assertEquals("値が誤っています。", 1L, list.get(1).get("sum").longValue());
	}
	
	public void testQueryUsingSumAndGroupByGreaterThanTwo() throws Exception {
		//select base_unit_id, sum(rate_value) from unit where id <= 5 group by base_unit_id having sum(rate_value) > 2 order by base_unit_id
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("base", new Property(Unit.BASE + "." + Unit.ID)));
		Sum sum = new Sum(new Property(Unit.RATE + ScalableNumber.VALUE));
		ex.addExtractValue(new ExtractValue("sum", sum));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.BASE + "." + Unit.ID)));
		ex.addHaving(Condition.gt(sum, 2L));
		List<Map<String, Long>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 1, list.size());
		assertEquals("値が誤っています。", 1610346L, list.get(0).get("sum").longValue());
	}
	
	public void testQueryUsingCount() throws Exception {
		//select base_unit_id, count(*) from unit where id <= 5 group by base_unit_id order by base_unit_id
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("base", new Property(Unit.BASE + "." + Unit.ID)));
		ex.addExtractValue(new ExtractValue("count", new Count()));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.BASE + "." + Unit.ID)));
		List<Map<String, Long>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 2, list.size());
		assertEquals("値が誤っています。", 4L, list.get(0).get("count").longValue());
		assertEquals("値が誤っています。", 1L, list.get(1).get("count").longValue());
		
		//select base_unit_id, count(rate_value) from unit where id <= 5 group by base_unit_id order by base_unit_id
		ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("base", new Property(Unit.BASE + "." + Unit.ID)));
		ex.addExtractValue(new ExtractValue("count", new Count(new Property(Unit.RATE + ScalableNumber.VALUE))));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.BASE + "." + Unit.ID)));
		list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 2, list.size());
		assertEquals("値が誤っています。", 4L, list.get(0).get("count").longValue());
		assertEquals("値が誤っています。", 1L, list.get(1).get("count").longValue());

		//select base_unit_id, count(distinct rate_value) from unit where id <= 5 group by base_unit_id order by base_unit_id
		ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("base", new Property(Unit.BASE + "." + Unit.ID)));
		ex.addExtractValue(new ExtractValue("count", new Count(new Property(Unit.RATE + ScalableNumber.VALUE), true)));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.BASE + "." + Unit.ID)));
		list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 2, list.size());
		assertEquals("値が誤っています。", 3L, list.get(0).get("count").longValue());
		assertEquals("値が誤っています。", 1L, list.get(1).get("count").longValue());
	}
	
	public void testFreeFormat() throws Exception {
		//select 3*(1 + 1) from unit where id <= 5 order by id
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("val", new FreeFormat("3*(1 + 1)")));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.ID)));
		List<Map<String, ? extends Number>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 5, list.size());
		assertEquals("値が誤っています。", 6L, list.get(0).get("val").longValue());

		//select 3*(2 + 1) from unit where id <= 5 order by id
		ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("val", new FreeFormat("3*(? + 1)", 2L)));
		ex.addOrder(Order.asc(new Property(Unit.ID)));
		list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 5, list.size());
		assertEquals("値が誤っています。", 9L, list.get(0).get("val").longValue());

		//select 3*(2 + rate_value) from unit where id <= 5 order by id
		ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("val", new FreeFormat("3*(? + ?)", 2L, new Property(Unit.RATE + ScalableNumber.VALUE))));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.ID)));
		list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 5, list.size());
		assertEquals("値が誤っています。", 9L, 			list.get(0).get("val").longValue());
		assertEquals("値が誤っています。", 3006L, 		list.get(1).get("val").longValue());
		assertEquals("値が誤っています。", 4828038L, 	list.get(2).get("val").longValue());
		assertEquals("値が誤っています。", 9L, 			list.get(3).get("val").longValue());
		assertEquals("値が誤っています。", 9L, 			list.get(4).get("val").longValue());

		//select rate_value*power(10, rate_scale) from unit where id <= 5 order by id
		ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("val", new FreeFormat("?*power(10, ?)", 
				new Property(Unit.RATE + ScalableNumber.VALUE), new Property(Unit.RATE + ScalableNumber.SCALE))));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.ID)));
		list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 5, list.size());
		assertEquals("値が誤っています。", 1L,			list.get(0).get("val").longValue());
		assertEquals("値が誤っています。", 1000L,		list.get(1).get("val").longValue());
		assertEquals("値が誤っています。", 1609344000L,	list.get(2).get("val").longValue());
	}
	
	public void testSumInFreeFormat() throws Exception {
		//select base_unit_id, sum(rate_value) from unit where id <= 5 group by base_unit_id having sum(rate_value) > 2 order by base_unit_id
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("base", new Property(Unit.BASE + "." + Unit.ID)));
		Sum sum = new Sum(new Property(Unit.RATE + ScalableNumber.VALUE));
		ex.addExtractValue(new ExtractValue("sum", new FreeFormat("?", sum)));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.BASE + "." + Unit.ID)));
		ex.addHaving(Condition.gt(sum, 2L));
		List<Map<String, Long>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 1, list.size());
		assertEquals("値が誤っています。", 1610346L, list.get(0).get("sum").longValue());
	}
	
	public void testSumInFreeFormatAndGrupByColumnInFreeFormat() throws Exception {
		//select base_unit_id, sum(rate_value) from unit where id <= 5 group by base_unit_id having sum(rate_value) > 2 order by base_unit_id
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("base", new FreeFormat("?", new Property(Unit.BASE + "." + Unit.ID))));
		Sum sum = new Sum(new Property(Unit.RATE + ScalableNumber.VALUE));
		ex.addExtractValue(new ExtractValue("sum", new FreeFormat("?", sum)));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.BASE + "." + Unit.ID)));
		ex.addHaving(Condition.gt(sum, 2L));
		List<Map<String, Long>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 1, list.size());
		assertEquals("値が誤っています。", 1610346L, list.get(0).get("sum").longValue());
	}
	
	public void testFreeFormatInWhereCondition() throws Exception {
		//select id * 3 from unit where id <= 5 and id * 3 < 10 order by id
		Extractor ex = new Extractor(Unit.class);
		Property p = new Property(Unit.ID);
		FreeFormat ff = new FreeFormat("? * 3", p);
		ex.addExtractValue(new ExtractValue("val", ff));
		ex.add(Condition.le(p, 5L));
		ex.add(Condition.lt(ff, 10));
		ex.addOrder(Order.asc(new Property(Unit.ID)));
		List<Map<String, ? extends Number>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 3, list.size());
	}
	
	public void testFreeFormatInHavingCondition() throws Exception {
		//select base_unit_id, sum(rate_value) * 3 from unit where id <= 5 group by base_unit_id having sum(rate_value) * 3 < 10 order by base_unit_id
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
	
	public void testSumInOrderBy() throws Exception {
		//select base_unit_id, sum(rate_value) from unit where id <= 5 group by base_unit_id order by sum(rate_value)
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("base", new Property(Unit.BASE + "." + Unit.ID)));
		Sum sum = new Sum(new Property(Unit.RATE + ScalableNumber.VALUE));
		ex.addExtractValue(new ExtractValue("sum", sum));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(sum));
		List<Map<String, Long>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 2, list.size());
		assertEquals("値が誤っています。", 1L, 		list.get(0).get("sum").longValue());
		assertEquals("値が誤っています。", 1610346L,	list.get(1).get("sum").longValue());
	}
	
	public void testConditionPropertyComparation() throws Exception {
		//select * from unit where id <= 5 and base_unit_id = rate_value order by id
		Extractor ex = new Extractor(Unit.class);
		ex.add(Condition.eq(new Property(Unit.BASE + "." + Unit.ID), 
				new Property(Unit.RATE + ScalableNumber.VALUE)));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.ID)));
		List<Map<String, Long>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 2, list.size());
	}
	
	public void testEqCondition() throws Exception {
		//select * from unit where base_unit_id = 1 and id <= 5 order by id
		Extractor ex = new Extractor(Unit.class);
		ex.add(Condition.eq(new Property(Unit.BASE + "." + Unit.ID), 1L));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.ID)));
		List<Unit> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 4, list.size());
		assertEquals("IDが誤っています。", 1L, list.get(0).getId().longValue());
		assertEquals("IDが誤っています。", 2L, list.get(1).getId().longValue());
		assertEquals("IDが誤っています。", 3L, list.get(2).getId().longValue());
		assertEquals("IDが誤っています。", 4L, list.get(3).getId().longValue());
	}
	
	public void testGeCondition() throws Exception {
		//select * from unit where base_unit_id >= 3 and id <= 5 order by id
		Extractor ex = new Extractor(Unit.class);
		ex.add(Condition.ge(new Property(Unit.BASE + "." + Unit.ID), 3L));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.ID)));
		List<Unit> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 1, list.size());
		assertEquals("IDが誤っています。", 5L, list.get(0).getId().longValue());
	}
	
	public void testGtCondition() throws Exception {
		//select * from unit where base_unit_id > 1 and id <= 5 order by id
		Extractor ex = new Extractor(Unit.class);
		ex.add(Condition.gt(new Property(Unit.BASE + "." + Unit.ID), 1L));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.ID)));
		List<Unit> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 1, list.size());
		assertEquals("IDが誤っています。", 5L, list.get(0).getId().longValue());
	}
	
	public void testInCondition() throws Exception {
		//select * from unit where base_unit_id in(1,2,3) and id <= 5 order by id
		Extractor ex = new Extractor(Unit.class);
		ex.add(Condition.in(new Property(Unit.BASE + "." + Unit.ID), 1L, 2L , 3L));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.ID)));
		List<Unit> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 4, list.size());
		assertEquals("IDが誤っています。", 1L, list.get(0).getId().longValue());
		assertEquals("IDが誤っています。", 2L, list.get(1).getId().longValue());
		assertEquals("IDが誤っています。", 3L, list.get(2).getId().longValue());
		assertEquals("IDが誤っています。", 4L, list.get(3).getId().longValue());
	}
	
	public void testIsNotNullCondition() throws Exception {
		//select * from unit where base_unit_id is not null and id <= 5 order by id
		Extractor ex = new Extractor(Unit.class);
		ex.add(Condition.isNotNull(new Property(Unit.BASE + "." + Unit.ID)));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.ID)));
		List<Unit> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 5, list.size());
		assertEquals("IDが誤っています。", 1L, list.get(0).getId().longValue());
		assertEquals("IDが誤っています。", 2L, list.get(1).getId().longValue());
		assertEquals("IDが誤っています。", 3L, list.get(2).getId().longValue());
		assertEquals("IDが誤っています。", 4L, list.get(3).getId().longValue());
		assertEquals("IDが誤っています。", 5L, list.get(4).getId().longValue());
	}
	
	public void testIsNullCondition() throws Exception {
		//select * from unit where base_unit_id is null and id <= 5 order by id
		Extractor ex = new Extractor(Unit.class);
		ex.add(Condition.isNull(new Property(Unit.BASE + "." + Unit.ID)));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.ID)));
		List<Unit> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 0, list.size());
	}
	
	public void testLeCondition() throws Exception {
		//select * from unit where base_unit_id <= 5 and id <= 5 order by id
		Extractor ex = new Extractor(Unit.class);
		ex.add(Condition.le(new Property(Unit.BASE + "." + Unit.ID), 5L));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.ID)));
		List<Unit> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 5, list.size());
		assertEquals("IDが誤っています。", 1L, list.get(0).getId().longValue());
		assertEquals("IDが誤っています。", 2L, list.get(1).getId().longValue());
		assertEquals("IDが誤っています。", 3L, list.get(2).getId().longValue());
		assertEquals("IDが誤っています。", 4L, list.get(3).getId().longValue());
		assertEquals("IDが誤っています。", 5L, list.get(4).getId().longValue());
	}
	
	public void testLtCondition() throws Exception {
		//select * from unit where base_unit_id < 5 and id <= 5 order by id
		Extractor ex = new Extractor(Unit.class);
		ex.add(Condition.lt(new Property(Unit.BASE + "." + Unit.ID), 5L));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.ID)));
		List<Unit> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 4, list.size());
		assertEquals("IDが誤っています。", 1L, list.get(0).getId().longValue());
		assertEquals("IDが誤っています。", 2L, list.get(1).getId().longValue());
		assertEquals("IDが誤っています。", 3L, list.get(2).getId().longValue());
		assertEquals("IDが誤っています。", 4L, list.get(3).getId().longValue());
	}
	
	public void testLikeCondition() throws Exception {
		//select * from unit where name like '%m%' and id <= 5 order by id
		Extractor ex = new Extractor(Unit.class);
		ex.add(Condition.like(new Property(Unit.NAME), "%m%"));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.ID)));
		List<Unit> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 3, list.size());
		assertEquals("IDが誤っています。", 1L, list.get(0).getId().longValue());
		assertEquals("IDが誤っています。", 2L, list.get(1).getId().longValue());
		assertEquals("IDが誤っています。", 4L, list.get(2).getId().longValue());
	}
	
	public void testNotEqCondition() throws Exception {
		//select * from unit where name<>'マイル' and id <= 5 order by id
		Extractor ex = new Extractor(Unit.class);
		ex.add(Condition.notEq(new Property(Unit.NAME), "マイル"));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.ID)));
		List<Unit> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 4, list.size());
		assertEquals("IDが誤っています。", 1L, list.get(0).getId().longValue());
		assertEquals("IDが誤っています。", 2L, list.get(1).getId().longValue());
		assertEquals("IDが誤っています。", 4L, list.get(2).getId().longValue());
		assertEquals("IDが誤っています。", 5L, list.get(3).getId().longValue());
	}
	
	public void testNotInCondition() throws Exception {
		//select * from unit where name not in('マイル','m') and id <= 5 order by id
		Extractor ex = new Extractor(Unit.class);
		ex.add(Condition.notIn(new Property(Unit.NAME), "マイル", "m"));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.ID)));
		List<Unit> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 3, list.size());
		assertEquals("IDが誤っています。", 2L, list.get(0).getId().longValue());
		assertEquals("IDが誤っています。", 4L, list.get(1).getId().longValue());
		assertEquals("IDが誤っています。", 5L, list.get(2).getId().longValue());
	}
	
	public void testRegexCondition() throws Exception {
		//for postgresql
		//  select * from unit where char_length(substring(description, '[.]*メートル[.]*'))>0 and id <= 5 order by id
		//for oracle 10g
		//  select * from unit where REGEXP_INSTR(description, '[.]*メートル[.]*')>0 and id <= 5 order by id
		Extractor ex = new Extractor(Unit.class);
		ex.add(Condition.regex(new Property(Unit.DESCRIPTION), "[.]*メートル[.]*"));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.ID)));
		List<Unit> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 3, list.size());
		assertEquals("IDが誤っています。", 1L, list.get(0).getId().longValue());
		assertEquals("IDが誤っています。", 2L, list.get(1).getId().longValue());
		assertEquals("IDが誤っています。", 4L, list.get(2).getId().longValue());
	}
	
	@SuppressWarnings("unchecked")
	public void testAndCondition() throws Exception {
		//select * from unit where description like '%メートル%' and rate_value = 1 and id <= 5 order by id 
		Extractor ex = new Extractor(Unit.class);
		CombineCondition condition = Condition.and();
		condition.add(Condition.like(new Property(Unit.DESCRIPTION), "%メートル%"));
		condition.add(Condition.eq(new Property(Unit.RATE + ScalableNumber.VALUE), 1));
		ex.add(condition);
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.ID)));
		List<Unit> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 2, list.size());
		assertEquals("IDが誤っています。", 1L, list.get(0).getId().longValue());
		assertEquals("IDが誤っています。", 4L, list.get(1).getId().longValue());
	}
	
	@SuppressWarnings("unchecked")
	public void testOrCondition() throws Exception {
		//select * from unit where (description like '%メートル%' or rate_value = 1) and id <= 5 order by id 
		Extractor ex = new Extractor(Unit.class);
		CombineCondition condition = Condition.or();
		condition.add(Condition.like(new Property(Unit.DESCRIPTION), "%メートル%"));
		condition.add(Condition.eq(new Property(Unit.RATE + ScalableNumber.VALUE), 1));
		ex.add(condition);
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.ID)));
		List<Unit> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 4, list.size());
		assertEquals("IDが誤っています。", 1L, list.get(0).getId().longValue());
		assertEquals("IDが誤っています。", 2L, list.get(1).getId().longValue());
		assertEquals("IDが誤っています。", 4L, list.get(2).getId().longValue());
		assertEquals("IDが誤っています。", 5L, list.get(3).getId().longValue());
	}
	
	public void testSpecificateReturnTypeWithConstructorInjection() throws Exception {
		//select sum(rate_value) from unit where id <= 5
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue(
				"sum", new Sum(new Property(Unit.RATE + ScalableNumber.VALUE))));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.setReturnType(Long.class);
		List<Long> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 1, list.size());
		assertEquals("値の取得に失敗しました。", 1610347L, list.get(0).longValue());

		ex.setReturnType(ReturnType2.class);
		List<ReturnType2> list2 = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 1, list2.size());
		assertEquals("値の取得に失敗しました。", 1610347L, list2.get(0).sum.longValue());
		assertTrue("コールバックされていません。", list2.get(0).isCallback);
	}
	
	public void testSpecificateReturnTypeWithSetterInjection() throws Exception {
		//select sum(rate_value) from unit where id <= 5
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue(
				"sum", new Sum(new Property(Unit.RATE + ScalableNumber.VALUE))));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.setReturnType(ReturnType1.class);
		List<ReturnType1> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 1, list.size());
		assertEquals("値の取得に失敗しました。", 1610347L, list.get(0).sum.longValue());
		assertTrue("コールバックされていません。", list.get(0).isCallback);
	}
	
	public void testNonExtractValueAndReturnTypeConstructorInjection() throws Exception {
		//select * from unit where base_unit_id = 1 and id <= 5 order by id
		Extractor ex = new Extractor(Unit.class);
		ex.add(Condition.eq(new Property(Unit.BASE + "." + Unit.ID), 1L));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.ID)));
		ex.setReturnType(ReturnType3.class);
		List<ReturnType3> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 4, list.size());
		assertEquals("IDが誤っています。", 1L, list.get(0).u.getId().longValue());
		assertEquals("IDが誤っています。", 2L, list.get(1).u.getId().longValue());
		assertEquals("IDが誤っています。", 3L, list.get(2).u.getId().longValue());
		assertEquals("IDが誤っています。", 4L, list.get(3).u.getId().longValue());
	}
	
	public void testNonExtractValueAndReturnTypeWithSetterInjection() throws Exception {
		//select * from unit where base_unit_id = 1 and id <= 5 order by id
		Extractor ex = new Extractor(Unit.class);
		ex.add(Condition.eq(new Property(Unit.BASE + "." + Unit.ID), 1L));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.ID)));
		ex.setReturnType(ReturnType4.class);
		List<ReturnType4> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 4, list.size());
		assertEquals("IDが誤っています。", 1L, list.get(0).u.getId().longValue());
		assertEquals("IDが誤っています。", 2L, list.get(1).u.getId().longValue());
		assertEquals("IDが誤っています。", 3L, list.get(2).u.getId().longValue());
		assertEquals("IDが誤っています。", 4L, list.get(3).u.getId().longValue());
	}
	
	public void testNonExtractValueAndReturnTypeWithTypeEquals() throws Exception {
		//select * from unit where base_unit_id = 1 and id <= 5 order by id
		Extractor ex = new Extractor(Unit.class);
		ex.add(Condition.eq(new Property(Unit.BASE + "." + Unit.ID), 1L));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(new Property(Unit.ID)));
		ex.setReturnType(Unit.class);
		List<Unit> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 4, list.size());
		assertEquals("IDが誤っています。", 1L, list.get(0).getId().longValue());
		assertEquals("IDが誤っています。", 2L, list.get(1).getId().longValue());
		assertEquals("IDが誤っています。", 3L, list.get(2).getId().longValue());
		assertEquals("IDが誤っています。", 4L, list.get(3).getId().longValue());
	}
	
	public void testUsingInnerJoin() throws Exception {
		//select * from unit 'target', unit 'joined' where target.id = joined.scale and joined.id = 3
		Extractor ex = new Extractor(Unit.class, "target");
		ex.addInnerJoin(new InnerJoin(new Property(Unit.class, "target", Unit.ID),
				new Property(Unit.class, "joined", Unit.SCALE)));
		ex.add(Condition.eq(new Property(Unit.class, "joined", Unit.ID), 3));
		List<Unit> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 1, list.size());
		assertEquals("IDが誤っています。", 2, list.get(0).getId().intValue());
	}
	
	public void testDistinct() throws Exception {
		//select base_unit_id, rate_value from unit where base_unit_id = 1 order by rate_value
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("baseId", new Property(Unit.BASE + "." + Unit.ID)));
		ex.addExtractValue(new ExtractValue("value", new Property(Unit.RATE + ScalableNumber.VALUE)));
		ex.add(Condition.eq(new Property(Unit.BASE + "." + Unit.ID), 1L));
		ex.addOrder(Order.asc(new Property(Unit.RATE + ScalableNumber.VALUE)));
		List<Map<String, Object>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 4, list.size());
		ex.setDistinct(true);
		//select distinct base_unit_id, rate_value from unit where base_unit_id = 1 order by rate_value
		list = BasicService.getService().findByExtractor(ex);
		assertEquals("返却数が誤っています。", 3, list.size());
	}
	
	public static class ReturnType1 {
		Long sum;
		public void setSum(Number sum) {
			this.sum = sum.longValue();
		}
		boolean isCallback;
		@PostLoad
		public void postLoad() {
			this.isCallback = true;
		}
	}
	
	public static class ReturnType2 {
		public ReturnType2(Number sum) {
			setSum(sum);
		}
		
		Long sum;
		public void setSum(Number sum) {
			this.sum = sum.longValue();
		}
		boolean isCallback;
		@PostLoad
		public void postLoad() {
			this.isCallback = true;
		}
	}

	public static class ReturnType3 {
		final Unit u;
		public ReturnType3(Unit unit) {
			u = unit;
		}
	}
	
	public static class ReturnType4 {
		Unit u;
		public void setEntity(Unit unit) {
			u = unit;
		}
	}
	
	public static class CreateQueryService implements Service {
		public void getQuery(int fetchSize) {
			Extractor ex = new Extractor(Numbering.class);
			ex.setFetchSize(fetchSize);
			BasicService.getService().findByExtractor(ex);
		}
	}
}
