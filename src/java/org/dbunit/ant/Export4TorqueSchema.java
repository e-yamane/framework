package org.dbunit.ant;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Export4TorqueSchema extends Export {
	private String schema;
	private boolean perTable = true;
	
	public Export4TorqueSchema(Project project) {
	}

	public String getSchema() {
		return schema;
	}
	
	public void setSchema(String schema) {
		this.schema = schema;
	}

	public boolean isPerTable() {
		return perTable;
	}

	public void setPerTable(boolean perTable) {
		this.perTable = perTable;
	}

	private File baseDest;
    public void execute(IDatabaseConnection connection) throws DatabaseUnitException {
    	baseDest = getDest();
    	try {
			if(schema == null) {
				super.execute(connection);
			} else {
				System.out.println(schema);
				String[] schemaNames = getFileNames();
				for(int i = 0 ; i < schemaNames.length ; i++) {
					System.out.println("exporting..." + schemaNames[i]);
					execute(connection, schemaNames[i]);
				}
			}
    	} finally {
    		setDest(baseDest);
    	}
    }

	private void execute(IDatabaseConnection connection, String schemaName) {
		try {
			System.out.println(schemaName);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(false);
			DocumentBuilder db = dbf.newDocumentBuilder();
			File f = new File(rootDir, schemaName);
			Document doc = db.parse(f);
			Element el = doc.getDocumentElement();
			NodeList list = el.getChildNodes();
			for(int i = 0 ; i < list.getLength() ; i++) {
				Node node = (Node)list.item(i);
				if(node instanceof Element) {
					Element child = (Element)node;
					if("table".equals(child.getNodeName()) && !Boolean.valueOf(child.getAttribute("skipSql")).booleanValue()) {
                        
						executeIt(connection, child.getAttribute("name").toUpperCase());
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	private List<Object> baseTables;
	@Override
	public List<Object> getTables() {
		return super.getTables();
	}
	private void executeIt(IDatabaseConnection connection, String tableName) {
		baseTables = new ArrayList<Object>(getTables());
		try {
			cloneQueries(tableName);
			Table t = new Table();
			t.setName(tableName);
			addTable(t);
			if(isPerTable()) {
				String fileName = MessageFormat.format(baseDest.getCanonicalPath(), (Object[]) new String[]{tableName});
				System.out.println(fileName);
				setDest(new File(fileName));
			}
			super.execute(connection);
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			setTables(baseTables);
		}
	}
	
	private void cloneQueries(String tableName) {
		try {
			getTables().clear();
			String[] vals = new String[]{tableName};
			Iterator<Object> iterator = baseTables.iterator();
			while(iterator.hasNext()) {
				Object org = iterator.next();
				if(org instanceof Query) {
					Object copy = org.getClass().newInstance();
					BeanUtils.copyProperties(copy, org);
					Query q = (Query)copy;
					q.setName(MessageFormat.format(q.getName(), (Object[])vals));
					q.setSql(MessageFormat.format(q.getSql(), (Object[])vals));
					addQuery(q);
				}
			}
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	private File rootDir;
	private String[] getFileNames() {
		try {
			DirectoryScanner ds = FileScanUtil.getDirectoryScanner(schema);
			rootDir = ds.getBasedir();
			return ds.getIncludedFiles();
			
//			FileSet set = new FileSet();
//			set.setDir(new File("."));
//			String tmp = schema.replaceAll("^\\./", "");
//			tmp = tmp.replaceAll("/\\./", "/");
//			set.setIncludes(tmp);
//			DirectoryScanner ds = set.getDirectoryScanner(project);
//			ds.scan();
//			String[] names = ds.getIncludedFiles();
//			return names;
		} catch(RuntimeException re) {
			re.printStackTrace();
			throw re;
		}
	}
}
