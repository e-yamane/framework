package org.dbunit.ant;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;

@SuppressWarnings("unused")
public class QueryTask extends AbstractStep {
	private String query = "";
	private String file = null;
	private String encoding = null;
    private String message;
	private boolean isRegexp = false;
	private Project project;

	public QueryTask(Project project) {
		this.project = project;
	}

	public void setQuery(String query) {
		this.query = query;
	}

    public void addText(String msg) {
    	query = query + ";" + msg;
    }

    public void setFile(String file) {
    	this.file = file;
    }

    public void setEncoding(String encoding) {
    	this.encoding = encoding;
    }

	public boolean isRegexp() {
		return isRegexp;
	}
	public void setRegexp(boolean isRegexp) {
		this.isRegexp = isRegexp;
	}

	public void execute(IDatabaseConnection idcon) throws DatabaseUnitException {
    	readFile();
    	try {
	    	String[] queries = query.split(";");
	    	for(int i = 0 ; i < queries.length ; i++) {
	    		String query = queries[i].trim();
	    		queries[i] = query;
	    		if(query.length() == 0) {
	    			continue;
	    		}
    			if(isRegexp())
    			{
    				executeRegexpQuery(idcon, query);
    			}
    			else
    			{
    	    		executeQuery(idcon, query);
    			}
	    	}
    	}
    	catch(SQLException e)
		{
    		throw new DatabaseUnitException(e);
    	}
    }

    private File rootDir;
    
	private void executeQuery(IDatabaseConnection idcon, String query) {
		try
		{
			Statement stmt = idcon.getConnection().createStatement();
			stmt.execute(query);
		} catch(SQLException e) {
			System.out.println("NG:" + query + "[" + e.getMessage() + "]");
		}
	}

	private void executeRegexpQuery(IDatabaseConnection idcon, String query) throws SQLException {
		String[] queries2 = RegexpUtil.getQueries(idcon, query, getParent().getSchema());
		for(int i = 0 ; i < queries2.length ; i++)
		{
            System.out.println(queries2[i]);
			executeQuery(idcon, queries2[i]);
		}
	}

	public String getLogMessage() {
		return
			"executing query task:\n" +
			"\tquery=" + this.query + "\n" +
			"\tfileName=" + this.file + "\n" +
			"\tencoding=" + this.encoding + "\n";
	}

	private void readFile() throws DatabaseUnitException {
		try {
			if(file == null)
			{
				return;
			}
			DirectoryScanner ds = FileScanUtil.getDirectoryScanner(file);
			rootDir = ds.getBasedir();
			String[] names = ds.getIncludedFiles();
			if(names != null) {
                System.out.println(names.length);
				StringBuffer buf = new StringBuffer(this.query);
				for(int i = 0 ; i < names.length ; i++) {
                    System.out.println(names[i]);
					buf.append(";");
					buf.append(readFile(names[i]));
				}
				this.query = buf.toString();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new DatabaseUnitException(e);
		}
	}

	private String readFile(String name) throws IOException {
		System.out.println(name);
		File f = new File(rootDir, name);
		final int size = 1024;
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f), size);
		try
		{
			byte[] array = new byte[size];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int readSize;
			do
			{
				readSize = bis.read(array);
				if(readSize == -1) {
					break;
				}
				baos.write(array, 0, readSize);
			} while(readSize == size);
			baos.close();
			array = baos.toByteArray();
			String query = (encoding == null) ? new String(array) : new String(array, encoding);
			return query;
		}
		finally
		{
			bis.close();
		}
	}
    
    public static void main(String[] args) throws Exception {
        String hoge = "./aaa**/*.sql";
        File f = new File(hoge);
        StringBuffer pattern = new StringBuffer();
        File dir;
        File tmp = f.getAbsoluteFile();
        String delimiter = "";
        Map<File, String> map = new HashMap<File, String>();
        File lastWildCardFile = null;
        while(tmp.getParentFile() != null) {
            if(!".".equals(tmp.getName())) {
                System.out.println(tmp.getName());
                pattern.insert(0, delimiter);
                pattern.insert(0, tmp.getName());
                delimiter = "/";
                map.put(tmp, pattern.toString());
                if(tmp.getName().indexOf('*') != -1) {
                    lastWildCardFile = tmp;
                }
            }
            tmp = tmp.getParentFile();
        }
        System.out.println(lastWildCardFile.getParent());
        System.out.println(map.get(lastWildCardFile));
//        System.out.println(f.isAbsolute());
//        System.out.println(dir.getAbsolutePath());

//        String zzz = "./torque-gen/./src/sql/wrapper-schema.sql".replaceAll("^\\./", "");
//        zzz = zzz.replaceAll("/\\./", "/");
//        System.out.println(zzz);
    }
}
