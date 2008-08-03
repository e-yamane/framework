/*
 * 作成日: 2005/05/08
 *
 * TODO この生成されたファイルのテンプレートを変更するには次へジャンプ:
 * ウィンドウ - 設定 - Java - コード・スタイル - コード・テンプレート
 */
package org.dbunit.ant;

import java.sql.SQLException;

import junit.framework.TestCase;

import org.dbunit.database.IDatabaseConnection;

/**
 * @author e-yamane
 *
 * TODO この生成された型コメントのテンプレートを変更するには次へジャンプ:
 * ウィンドウ - 設定 - Java - コード・スタイル - コード・テンプレート
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
		assertEquals("返却数が誤っています。", 6, queries.length);
		assertEquals("ＳＱＬ[0]が誤っています。", "select HOGE_001.hoge, POGE_001.poge from HOGE_001, POGE_001 where 'hoge%' like 'hoge%'", queries[0]);
		assertEquals("ＳＱＬ[1]が誤っています。", "select HOGE_001.hoge, POGE_002.poge from HOGE_001, POGE_002 where 'hoge%' like 'hoge%'", queries[1]);
		assertEquals("ＳＱＬ[2]が誤っています。", "select HOGE_001.hoge, POGE_003.poge from HOGE_001, POGE_003 where 'hoge%' like 'hoge%'", queries[2]);
		assertEquals("ＳＱＬ[3]が誤っています。", "select HOGE_002.hoge, POGE_001.poge from HOGE_002, POGE_001 where 'hoge%' like 'hoge%'", queries[3]);
		assertEquals("ＳＱＬ[4]が誤っています。", "select HOGE_002.hoge, POGE_002.poge from HOGE_002, POGE_002 where 'hoge%' like 'hoge%'", queries[4]);
		assertEquals("ＳＱＬ[5]が誤っています。", "select HOGE_002.hoge, POGE_003.poge from HOGE_002, POGE_003 where 'hoge%' like 'hoge%'", queries[5]);
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
