/*
 * �쐬��: 2005/05/08
 *
 * TODO ���̐������ꂽ�t�@�C���̃e���v���[�g��ύX����ɂ͎��փW�����v:
 * �E�B���h�E - �ݒ� - Java - �R�[�h�E�X�^�C�� - �R�[�h�E�e���v���[�g
 */
package org.dbunit.ant;

import java.sql.SQLException;

import junit.framework.TestCase;

import org.dbunit.database.IDatabaseConnection;

/**
 * @author e-yamane
 *
 * TODO ���̐������ꂽ�^�R�����g�̃e���v���[�g��ύX����ɂ͎��փW�����v:
 * �E�B���h�E - �ݒ� - Java - �R�[�h�E�X�^�C�� - �R�[�h�E�e���v���[�g
 */
public class RegexpUtilTest extends TestCase {
	private RegexpUtilExt target;
	
	protected void setUp() throws Exception {
		super.setUp();
		target = new RegexpUtilExt();
		RegexpUtil.instance = target;
	}

	public void testIt() throws Exception {
		String sql = "select HOGE%.hoge, POGE%.poge from hoge%, POGE% where 'hoge%' like 'hoge%'";
		String[] queries = RegexpUtil.getQueries(null, sql, null);
		assertEquals("�ԋp��������Ă��܂��B", 6, queries.length);
		assertEquals("�r�p�k[0]������Ă��܂��B", "select HOGE_001.hoge, POGE_001.poge from HOGE_001, POGE_001 where 'hoge%' like 'hoge%'", queries[0]);
		assertEquals("�r�p�k[1]������Ă��܂��B", "select HOGE_001.hoge, POGE_002.poge from HOGE_001, POGE_002 where 'hoge%' like 'hoge%'", queries[1]);
		assertEquals("�r�p�k[2]������Ă��܂��B", "select HOGE_001.hoge, POGE_003.poge from HOGE_001, POGE_003 where 'hoge%' like 'hoge%'", queries[2]);
		assertEquals("�r�p�k[3]������Ă��܂��B", "select HOGE_002.hoge, POGE_001.poge from HOGE_002, POGE_001 where 'hoge%' like 'hoge%'", queries[3]);
		assertEquals("�r�p�k[4]������Ă��܂��B", "select HOGE_002.hoge, POGE_002.poge from HOGE_002, POGE_002 where 'hoge%' like 'hoge%'", queries[4]);
		assertEquals("�r�p�k[5]������Ă��܂��B", "select HOGE_002.hoge, POGE_003.poge from HOGE_002, POGE_003 where 'hoge%' like 'hoge%'", queries[5]);
	}

	static class RegexpUtilExt extends RegexpUtil {
		String[] getNames2(IDatabaseConnection idcon, String tableName) throws SQLException {
			if("HOGE%".equals(tableName)) {
				return new String[]{"HOGE_001", "HOGE_002"};
			} else if("POGE%".equals(tableName)) {
				return new String[]{"POGE_001", "POGE_002", "POGE_003"};
			} else {
				throw new RuntimeException();
			}
		}
	}
}
