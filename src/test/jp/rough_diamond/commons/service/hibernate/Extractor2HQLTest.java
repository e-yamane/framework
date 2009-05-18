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
import jp.rough_diamond.commons.extractor.ExtractValue;
import jp.rough_diamond.commons.extractor.Extractor;
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

	public void testFetchSize�̎w�肪�������s���Ă��邱��() throws Exception {
		//�p�X�݂̂̊m�F�ł��B
		//������ƃ��W�b�N���򂵂Ă��邱�Ƃ�Cobertura�Ŗڎ��Ŋm�F����̂�
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
		assertEquals("�ԋp��������Ă��܂��B", 1, list.size());
		assertEquals("�l�̎擾�Ɏ��s���܂����B", "�}�C��", list.get(0).get("name"));
	}
	
	public void testQueryUsingProperty() throws Exception {
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("name", new Property(Unit.NAME)));
		ex.add(Condition.eq(new Property(Unit.ID), 3L));
		List<Map<String, Object>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("�ԋp��������Ă��܂��B", 1, list.size());
		assertEquals("�l�̎擾�Ɏ��s���܂����B", "�}�C��", list.get(0).get("name"));
	}

	public void testQueryUsingMax() throws Exception {
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue(
				"max", new Max(new Property(Unit.RATE + ScalableNumber.VALUE))));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		List<Map<String, Long>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("�ԋp��������Ă��܂��B", 1, list.size());
		assertEquals("�l�̎擾�Ɏ��s���܂����B", 1609344L, list.get(0).get("max").longValue());
	}
	
	public void testQueryUsingMin() throws Exception {
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue(
				"min", new Min(new Property(Unit.RATE + ScalableNumber.VALUE))));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		List<Map<String, Long>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("�ԋp��������Ă��܂��B", 1, list.size());
		assertEquals("�l�̎擾�Ɏ��s���܂����B", 1L, list.get(0).get("min").longValue());
	}
	
	public void testQueryUsingSum() throws Exception {
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue(
				"sum", new Sum(new Property(Unit.RATE + ScalableNumber.VALUE))));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		List<Map<String, Long>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("�ԋp��������Ă��܂��B", 1, list.size());
		assertEquals("�l�̎擾�Ɏ��s���܂����B", 1610347L, list.get(0).get("sum").longValue());
	}
	
	public void testQueryUsingAvg() throws Exception {
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue(
				"avg", new Avg(new Property(Unit.RATE + ScalableNumber.VALUE))));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		List<Map<String, Double>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("�ԋp��������Ă��܂��B", 1, list.size());
		System.out.println(list.get(0).get("avg"));
		//�덷������̂�assert�͂��Ȃ�
	}

	public void testQueryUsingSumAndGroupBy() throws Exception {
		Extractor ex = new Extractor(Unit.class);
		ex.addExtractValue(new ExtractValue("base", new Property(Unit.BASE + "." + Unit.ID)));
		ex.addExtractValue(new ExtractValue(
				"sum", new Sum(new Property(Unit.RATE + ScalableNumber.VALUE))));
		ex.add(Condition.le(new Property(Unit.ID), 5L));
		ex.addOrder(Order.asc(Unit.BASE + "." + Unit.ID));
		List<Map<String, Long>> list = BasicService.getService().findByExtractor(ex);
		assertEquals("�ԋp��������Ă��܂��B", 2, list.size());
		assertEquals("�l������Ă��܂��B", 1610346L, list.get(0).get("sum").longValue());
		assertEquals("�l������Ă��܂��B", 1L, list.get(1).get("sum").longValue());
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
		assertEquals("�ԋp��������Ă��܂��B", 1, list.size());
		assertEquals("�l������Ă��܂��B", 1610346L, list.get(0).get("sum").longValue());
	}
	
	public static class CreateQueryService implements Service {
		public void getQuery(int fetchSize) {
			Extractor ex = new Extractor(Numbering.class);
			ex.setFetchSize(fetchSize);
			BasicService.getService().findByExtractor(ex);
		}
	}
}
