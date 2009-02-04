package jp.rough_diamond.commons.service;

import jp.rough_diamond.commons.entity.Numbering;
import jp.rough_diamond.commons.service.NumberingService.CashingStrategy;
import jp.rough_diamond.commons.testdata.NumberingLoader;
import jp.rough_diamond.commons.testdata.UnitLoader;
import jp.rough_diamond.commons.testing.DataLoadingTestCase;
import jp.rough_diamond.framework.service.ServiceLocator;

public class NumberingServiceTest extends DataLoadingTestCase {

	protected void setUp() throws Exception {
		super.setUp();
		UnitLoader.init();
		NumberingLoader.init();
	}

	public void testGetNumberInNonCasingStrategy() throws Exception {
		NumberingService.CashingStrategy strategy = ServiceLocator.getService(NumberingService.CashingStrategy.class, NumberingService.NonCashingStrategy.class);
		
		assertEquals("�ԋp�l������Ă��܂��B", strategy.getNumber("yamane"), 1L);
		Numbering numbering = BasicService.getService().findByPK(Numbering.class, "yamane");
		assertEquals("�ԋp�l������Ă��܂��B", numbering.getNextNumber().longValue(), 1L);
		assertEquals("�ԋp�l������Ă��܂��B", strategy.getNumber("yamane"), 2L);
		numbering = BasicService.getService().findByPK(Numbering.class, "yamane");
		assertEquals("�ԋp�l������Ă��܂��B", numbering.getNextNumber().longValue(), 2L);
		assertEquals("�ԋp�l������Ă��܂��B", strategy.getNumber("yamane"), 3L);
		numbering = BasicService.getService().findByPK(Numbering.class, "yamane");
		assertEquals("�ԋp�l������Ă��܂��B", numbering.getNextNumber().longValue(), 3L);
		assertEquals("�ԋp�l������Ă��܂��B", strategy.getNumber("yamane"), 4L);
		numbering = BasicService.getService().findByPK(Numbering.class, "yamane");
		assertEquals("�ԋp�l������Ă��܂��B", numbering.getNextNumber().longValue(), 4L);
		assertEquals("�ԋp�l������Ă��܂��B", strategy.getNumber("yamane"), 5L);
		numbering = BasicService.getService().findByPK(Numbering.class, "yamane");
		assertEquals("�ԋp�l������Ă��܂��B", numbering.getNextNumber().longValue(), 5L);
		assertEquals("�ԋp�l������Ă��܂��B", strategy.getNumber("yamane"), 6L);
		numbering = BasicService.getService().findByPK(Numbering.class, "yamane");
		assertEquals("�ԋp�l������Ă��܂��B", numbering.getNextNumber().longValue(), 6L);
		assertEquals("�ԋp�l������Ă��܂��B", strategy.getNumber("yamane"), 7L);
		numbering = BasicService.getService().findByPK(Numbering.class, "yamane");
		assertEquals("�ԋp�l������Ă��܂��B", numbering.getNextNumber().longValue(), 7L);
		assertEquals("�ԋp�l������Ă��܂��B", strategy.getNumber("yamane"), 8L);
		numbering = BasicService.getService().findByPK(Numbering.class, "yamane");
		assertEquals("�ԋp�l������Ă��܂��B", numbering.getNextNumber().longValue(), 8L);
		assertEquals("�ԋp�l������Ă��܂��B", strategy.getNumber("yamane"), 9L);
		numbering = BasicService.getService().findByPK(Numbering.class, "yamane");
		assertEquals("�ԋp�l������Ă��܂��B", numbering.getNextNumber().longValue(), 9L);
		assertEquals("�ԋp�l������Ă��܂��B", strategy.getNumber("yamane"), 10L);
		numbering = BasicService.getService().findByPK(Numbering.class, "yamane");
		assertEquals("�ԋp�l������Ă��܂��B", numbering.getNextNumber().longValue(), 10L);
		assertEquals("�ԋp�l������Ă��܂��B", strategy.getNumber("yamane"), 11L);
		numbering = BasicService.getService().findByPK(Numbering.class, "yamane");
		assertEquals("�ԋp�l������Ă��܂��B", numbering.getNextNumber().longValue(), 11L);
	}
	
	public void testGetNumberInCasingStrategy() throws Exception {
		NumberingService.NumberCashingStrategy strategy = ServiceLocator.getService(NumberingService.NumberCashingStrategy.class, NumberCashingStrategyExt.class);

		assertEquals("�ԋp�l������Ă��܂��B", strategy.getNumber("yamane"), 1L);
		Numbering numbering = BasicService.getService().findByPK(Numbering.class, "yamane");
		assertEquals("�ԋp�l������Ă��܂��B", numbering.getNextNumber().longValue(), 10L);
		assertEquals("�ԋp�l������Ă��܂��B", strategy.getNumber("yamane"), 2L);
		numbering = BasicService.getService().findByPK(Numbering.class, "yamane");
		assertEquals("�ԋp�l������Ă��܂��B", numbering.getNextNumber().longValue(), 10L);
		assertEquals("�ԋp�l������Ă��܂��B", strategy.getNumber("yamane"), 3L);
		numbering = BasicService.getService().findByPK(Numbering.class, "yamane");
		assertEquals("�ԋp�l������Ă��܂��B", numbering.getNextNumber().longValue(), 10L);
		assertEquals("�ԋp�l������Ă��܂��B", strategy.getNumber("yamane"), 4L);
		numbering = BasicService.getService().findByPK(Numbering.class, "yamane");
		assertEquals("�ԋp�l������Ă��܂��B", numbering.getNextNumber().longValue(), 10L);
		assertEquals("�ԋp�l������Ă��܂��B", strategy.getNumber("yamane"), 5L);
		numbering = BasicService.getService().findByPK(Numbering.class, "yamane");
		assertEquals("�ԋp�l������Ă��܂��B", numbering.getNextNumber().longValue(), 10L);
		assertEquals("�ԋp�l������Ă��܂��B", strategy.getNumber("yamane"), 6L);
		numbering = BasicService.getService().findByPK(Numbering.class, "yamane");
		assertEquals("�ԋp�l������Ă��܂��B", numbering.getNextNumber().longValue(), 10L);
		assertEquals("�ԋp�l������Ă��܂��B", strategy.getNumber("yamane"), 7L);
		numbering = BasicService.getService().findByPK(Numbering.class, "yamane");
		assertEquals("�ԋp�l������Ă��܂��B", numbering.getNextNumber().longValue(), 10L);
		assertEquals("�ԋp�l������Ă��܂��B", strategy.getNumber("yamane"), 8L);
		numbering = BasicService.getService().findByPK(Numbering.class, "yamane");
		assertEquals("�ԋp�l������Ă��܂��B", numbering.getNextNumber().longValue(), 10L);
		assertEquals("�ԋp�l������Ă��܂��B", strategy.getNumber("yamane"), 9L);
		numbering = BasicService.getService().findByPK(Numbering.class, "yamane");
		assertEquals("�ԋp�l������Ă��܂��B", numbering.getNextNumber().longValue(), 10L);
		assertEquals("�ԋp�l������Ă��܂��B", strategy.getNumber("yamane"), 10L);
		numbering = BasicService.getService().findByPK(Numbering.class, "yamane");
		assertEquals("�ԋp�l������Ă��܂��B", numbering.getNextNumber().longValue(), 10L);
		assertEquals("�ԋp�l������Ă��܂��B", strategy.getNumber("yamane"), 11L);
		numbering = BasicService.getService().findByPK(Numbering.class, "yamane");
		assertEquals("�ԋp�l������Ă��܂��B", numbering.getNextNumber().longValue(), 20L);
	}
	
	public static class NumberCashingStrategyExt extends NumberingService.NumberCashingStrategy {
		@Override
		protected int getCashSize() {
			return 10;
		}
		@Override
		protected CashingStrategy getStrategy() {
			return this;
		}
	}
}
