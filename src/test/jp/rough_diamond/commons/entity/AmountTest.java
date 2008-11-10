package jp.rough_diamond.commons.entity;

import java.math.BigDecimal;

import junit.framework.TestCase;

public class AmountTest extends TestCase {
	public void testQuantity() throws Exception {
		BigDecimal bd = new BigDecimal("10.01");
		Amount a = new Amount();
		a.setQuantity(bd);
		assertEquals("valueが誤っています。", 1001L, a.getValue().longValue());
		assertEquals("scaleが誤っています。", 2, a.getScale().intValue());
		a.setScale(-1);
		assertEquals("数値が誤っています。", 10010L, a.getQuantity().longValue());
	}
}
