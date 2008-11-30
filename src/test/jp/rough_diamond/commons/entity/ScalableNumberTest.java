package jp.rough_diamond.commons.entity;

import java.math.BigDecimal;

import junit.framework.TestCase;

public class ScalableNumberTest extends TestCase {
	public void testIt() throws Exception {
		BigDecimal bd = new BigDecimal("10.01");
		ScalableNumber sn = new ScalableNumber();
		sn.setDecimal(bd);
		assertEquals("valueが誤っています。", 1001L, sn.getValue().longValue());
		assertEquals("scaleが誤っています。", 2, sn.getScale().intValue());
		sn.setScale(-1);
		assertEquals("数値が誤っています。", 10010L, sn.longValue());
		
		//生成子のテスト
		sn = new ScalableNumber();
		assertEquals("int値が誤っています。", 0, sn.intValue());
		sn = new ScalableNumber(1L, -1);
		assertEquals("long値が誤っています。", 10L, sn.longValue());
		sn = new ScalableNumber(new BigDecimal("100"));
		assertEquals("float値が誤っています。", 100F, sn.floatValue());
		assertEquals("double値が誤っています。", 100D, sn.doubleValue());
		
		sn = new ScalableNumber(new BigDecimal(0.0105D));
		assertEquals("unscaledValueが誤っています。", 1050000000000000065L, sn.getValue().longValue());
		assertEquals("scaleが誤っています。", 20, sn.getScale().intValue());

		sn = new ScalableNumber(new BigDecimal(0.93D));
		assertEquals("unscaledValueが誤っています。", 930000000000000049L, sn.getValue().longValue());
		assertEquals("scaleが誤っています。", 18, sn.getScale().intValue());
	}
}
