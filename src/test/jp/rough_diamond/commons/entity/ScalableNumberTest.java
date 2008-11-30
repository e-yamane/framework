package jp.rough_diamond.commons.entity;

import java.math.BigDecimal;

import junit.framework.TestCase;

public class ScalableNumberTest extends TestCase {
	public void testIt() throws Exception {
		BigDecimal bd = new BigDecimal("10.01");
		ScalableNumber sn = new ScalableNumber();
		sn.setDecimal(bd);
		assertEquals("value������Ă��܂��B", 1001L, sn.getValue().longValue());
		assertEquals("scale������Ă��܂��B", 2, sn.getScale().intValue());
		sn.setScale(-1);
		assertEquals("���l������Ă��܂��B", 10010L, sn.longValue());
		
		//�����q�̃e�X�g
		sn = new ScalableNumber();
		assertEquals("int�l������Ă��܂��B", 0, sn.intValue());
		sn = new ScalableNumber(1L, -1);
		assertEquals("long�l������Ă��܂��B", 10L, sn.longValue());
		sn = new ScalableNumber(new BigDecimal("100"));
		assertEquals("float�l������Ă��܂��B", 100F, sn.floatValue());
		assertEquals("double�l������Ă��܂��B", 100D, sn.doubleValue());
		
		sn = new ScalableNumber(new BigDecimal(0.0105D));
		assertEquals("unscaledValue������Ă��܂��B", 1050000000000000065L, sn.getValue().longValue());
		assertEquals("scale������Ă��܂��B", 20, sn.getScale().intValue());

		sn = new ScalableNumber(new BigDecimal(0.93D));
		assertEquals("unscaledValue������Ă��܂��B", 930000000000000049L, sn.getValue().longValue());
		assertEquals("scale������Ă��܂��B", 18, sn.getScale().intValue());
	}
}
