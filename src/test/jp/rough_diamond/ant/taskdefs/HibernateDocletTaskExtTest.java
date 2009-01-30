package jp.rough_diamond.ant.taskdefs;

import junit.framework.TestCase;

public class HibernateDocletTaskExtTest extends TestCase {
	public void testPatterns() throws Exception {
		HibernateDocletTaskExt.Patterns p = new HibernateDocletTaskExt.Patterns("I:**/entity/**/*.java");
		assertFalse("include�ł͂���܂���B", p.isExclude);
		assertEquals("�p�X������Ă��܂��B", "**/entity/**/*.java", p.pattern);

		p = new HibernateDocletTaskExt.Patterns("**/entity/**/*.java");
		assertFalse("include�ł͂���܂���B", p.isExclude);
		assertEquals("�p�X������Ă��܂��B", "**/entity/**/*.java", p.pattern);

		p = new HibernateDocletTaskExt.Patterns("i:**/entity/**/*.java");
		assertFalse("include�ł͂���܂���B", p.isExclude);
		assertEquals("�p�X������Ă��܂��B", "**/entity/**/*.java", p.pattern);

		p = new HibernateDocletTaskExt.Patterns("E:jp/rough_diamond/account/entity/ActualTransaction.java");
		assertTrue("exclude�ł͂���܂���B", p.isExclude);
		assertEquals("�p�X������Ă��܂��B", "jp/rough_diamond/account/entity/ActualTransaction.java", p.pattern);

		p = new HibernateDocletTaskExt.Patterns("e:jp/rough_diamond/account/entity/ActualTransaction.java");
		assertTrue("exclude�ł͂���܂���B", p.isExclude);
		assertEquals("�p�X������Ă��܂��B", "jp/rough_diamond/account/entity/ActualTransaction.java", p.pattern);
	}
}
