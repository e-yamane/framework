/*
 * 作成日: 2005/05/09
 *
 * TODO この生成されたファイルのテンプレートを変更するには次へジャンプ:
 * ウィンドウ - 設定 - Java - コード・スタイル - コード・テンプレート
 */
package org.dbunit.ant;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.StringTokenizer;

import org.dbunit.database.IDatabaseConnection;

/**
 * @author e-yamane
 *
 */
public class RegexpUtil {
	static RegexpUtil instance = new RegexpUtil();

	public static String[] getQueries(IDatabaseConnection idcon, String query, String schema) throws SQLException {
    	StringTokenizer tokenizer = new StringTokenizer(query, " ,.", true);
    	StringBuffer buf = new StringBuffer();
    	int replaceIndex = 0;
    	LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
    	while(tokenizer.hasMoreTokens()) {
    		String token = tokenizer.nextToken();
    		if(token.indexOf("%") != -1 && token.indexOf("'") == -1) {
    			token = token.toUpperCase();
    			if(map.containsKey(token)) {
    				int tmp = ((Integer)map.get(token)).intValue();
        			buf.append("{" + tmp + "}");
    			}
    			else 
    			{
        			buf.append("{" + replaceIndex + "}");
        			map.put(token, new Integer(replaceIndex));
        			replaceIndex++;
    			}
    		}
    		else
    		{
    			token = token.replaceAll("'", "''");
    			buf.append(token);
    		}
    	}
    	if(map.isEmpty()) {
    		return new String[]{query};
    	}
    	Iterator<String> iterator = map.keySet().iterator();
    	List<String[]> list = new ArrayList<String[]>();
    	while(iterator.hasNext()) 
    	{
    		String key = (String)iterator.next();
    		String[] tableNames = getNames(idcon, key, schema);
    		list.add(tableNames);
    	}
    	String[][] params = getParams((String[][])list.toArray(new String[list.size()][]));
    	list.clear();
    	String template = buf.toString();
    	for(int i = 0 ; i < params.length ; i++) 
    	{
    		list.add(new String[]{MessageFormat.format(template, (Object[])params[i])});
    	}
    	return (String[])list.toArray(new String[list.size()]);
    }

    @SuppressWarnings("unchecked")
    public static String[][] getParams(String[][] tableNames) {
    	List tmp = new ArrayList();
    	tmp = getParams(0, tmp, tableNames);
    	return (String[][])tmp.toArray(new String[tmp.size()][]);
    }
    
    @SuppressWarnings("unchecked")
    public static List getParams(int index, List tmp, String[][] tableNames) {
    	if(index == tableNames.length) 
    	{
    		List tmp2 = new ArrayList();
    		tmp2.add(tmp.toArray(new String[0]));
    		return tmp2;
    	}
    	List ret = new ArrayList();
    	for(int i = 0 ; i < tableNames[index].length ; i++) {
    		List tmp2 = new ArrayList(tmp);
    		tmp2.add(tableNames[index][i]);
    		tmp2 = getParams(index + 1, tmp2, tableNames);
    		ret.addAll(tmp2);
    	}
    	return ret;
    }

    public static String[] getNames(IDatabaseConnection idcon, String tableName, String schema) throws SQLException {
    	return instance.getNames2(idcon, tableName, schema);
    }
    
    String[] getNames2(IDatabaseConnection idcon, String tableName, String schema) throws SQLException {
    	LinkedHashSet<String> ret = new LinkedHashSet<String>();
    	Connection con = idcon.getConnection();
    	DatabaseMetaData dmd = con.getMetaData();
    	ResultSet rs = dmd.getTables(null, schema, tableName.toUpperCase(), new String[]{"TABLE", "VIEW"});
    	while(rs.next()) {
    		String tableNameReal = rs.getString(3).toUpperCase();
    		ret.add(tableNameReal);
    	}
    	rs.close();
    	rs = dmd.getTables(null, schema, tableName.toLowerCase(), new String[]{"TABLE", "VIEW"});
    	while(rs.next()) {
    		String tableNameReal = rs.getString(3).toUpperCase();
    		ret.add(tableNameReal);
    	}
    	return (String[])ret.toArray(new String[ret.size()]);
    }
}
