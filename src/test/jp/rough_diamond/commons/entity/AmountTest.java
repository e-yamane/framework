package jp.rough_diamond.commons.entity;

import java.math.BigDecimal;

import junit.framework.TestCase;

public class AmountTest extends TestCase {
	public void testQuantity() throws Exception {
		BigDecimal bd = new BigDecimal("10.01");
		Amount a = new Amount();
		a.setQuantity(bd);
		assertEquals("value������Ă��܂��B", 1001L, a.getValue().longValue());
		assertEquals("scale������Ă��܂��B", 2, a.getScale().intValue());
		a.setScale(-1);
		assertEquals("���l������Ă��܂��B", 10010L, a.getQuantity().longValue());
	}
}
