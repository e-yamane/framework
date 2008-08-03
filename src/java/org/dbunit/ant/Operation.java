/*
 *
 * The DbUnit Database Testing Framework
 * Copyright (C)2002-2004, DbUnit.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package org.dbunit.ant;

import java.io.File;
import java.sql.SQLException;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.ext.mssql.InsertIdentityOperation;
import org.dbunit.operation.DatabaseOperation;

/**
 * The <code>Operation</code> class is the step that defines which
 * operation will be performed in the execution of the <code>DbUnitTask</code>
 * task.
 *
 * @author Timothy Ruppert
 * @author Ben Cox
 * @version $Revision: 157 $
 * @since Jun 10, 2002
 * @see org.dbunit.ant.DbUnitTaskStep
 */
public class Operation extends AbstractStep
{
    private static final String DEFAULT_FORMAT = FORMAT_FLAT;

    protected String _type = "CLEAN_INSERT";
    private String _format;
    private String _src;
    private DatabaseOperation _operation;
    private boolean _forwardOperation = true;
	private Project project;
    
    public Operation(Project p) {
    	this.project = p;
    }
    
    public String getType()
    {
        return _type;
    }

    public String getSrc()
    {
        return _src;
    }

    public DatabaseOperation getDbOperation()
    {
        return _operation;
    }

    public String getFormat()
    {
        return _format != null ? _format : DEFAULT_FORMAT;
    }

    public void setType(String type)
    {
        if ("UPDATE".equals(type))
        {
            _operation = DatabaseOperation.UPDATE;
            _forwardOperation = true;
        }
        else if ("INSERT".equals(type))
        {
            _operation = DatabaseOperation.INSERT;
            _forwardOperation = true;
        }
        else if ("REFRESH".equals(type))
        {
            _operation = DatabaseOperation.REFRESH;
            _forwardOperation = true;
        }
        else if ("DELETE".equals(type))
        {
            _operation = DatabaseOperation.DELETE;
            _forwardOperation = false;
        }
        else if ("DELETE_ALL".equals(type))
        {
            _operation = DatabaseOperation.DELETE_ALL;
            _forwardOperation = false;
        }
        else if ("CLEAN_INSERT".equals(type))
        {
            _operation = DatabaseOperation.CLEAN_INSERT;
            _forwardOperation = false;
        }
        else if ("NONE".equals(type))
        {
            _operation = DatabaseOperation.NONE;
            _forwardOperation = true;
        }
        else if ("MSSQL_CLEAN_INSERT".equals(type))
        {
            _operation = InsertIdentityOperation.CLEAN_INSERT;
            _forwardOperation = false;
        }
        else if ("MSSQL_INSERT".equals(type))
        {
            _operation = InsertIdentityOperation.INSERT;
            _forwardOperation = true;
        }
        else if ("MSSQL_REFRESH".equals(type))
        {
            _operation = InsertIdentityOperation.REFRESH;
            _forwardOperation = true;
        }
        else
        {
            throw new IllegalArgumentException("Type must be one of: UPDATE, INSERT,"
                    + " REFRESH, DELETE, DELETE_ALL, CLEAN_INSERT, MSSQL_INSERT, "
                    + " or MSSQL_REFRESH but was: " + type);
        }
        _type = type;
    }

    public void setSrc(String src)
    {
        _src = src;
    }

    public void setFormat(String format)
    {
        if (format.equalsIgnoreCase(FORMAT_FLAT)
                || format.equalsIgnoreCase(FORMAT_XML)
                || format.equalsIgnoreCase(FORMAT_CSV)
        )
        {
            _format = format;
        }
        else
        {
            throw new IllegalArgumentException("Type must be either 'flat'(default), 'xml' or 'csv' but was: " + format);
        }
    }

    public void execute(IDatabaseConnection connection) throws DatabaseUnitException
    {
        if (_operation == null)
        {
            throw new DatabaseUnitException("Operation.execute(): setType(String) must be called before execute()!");
        }

        if (_operation == DatabaseOperation.NONE)
        {
            return;
        }

        try
        {
        	String[] names = getNames();
        	for(int i = 0 ; i < names.length ; i++) {
        		System.out.println(names[i]);
                IDataSet dataset = getSrcDataSet(new File(".", names[i]),
                        getFormat(), _forwardOperation);
                _operation.execute(connection, dataset);
        		System.out.println("done.");
        	}
        }
        catch (SQLException e)
        {
            throw new DatabaseUnitException(e);
        }
    }
    
    private String[] getNames() {
		try {
			FileSet set = new FileSet();
			set.setDir(new File("."));
			String tmp = _src.replaceAll("^\\./", "");
			tmp = tmp.replaceAll("/\\./", "/");
			System.out.println(tmp);
			set.setIncludes(tmp);
			DirectoryScanner ds = set.getDirectoryScanner(project);
			ds.scan();
			String[] names = ds.getIncludedFiles();
			System.out.println(names.length);
			return names;
		} catch(RuntimeException re) {
			re.printStackTrace();
			throw re;
		}
    }
    
    public String getLogMessage()
    {
        return "Executing operation: " + _type
                + "\n          on   file: " + ((_src == null) ? null : _src)
                + "\n          with format: " + _format;
    }


    public String toString()
    {
        StringBuffer result = new StringBuffer();
        result.append("Operation: ");
        result.append(" type=" + _type);
        result.append(", format=" + _format);
        result.append(", src=" + _src == null ? null : _src);
        result.append(", operation = " + _operation);

        return result.toString();
    }

    public static void main(String[] args) {
    	System.out.println("./etc/./export/*.xml".replaceAll("^\\./", ""));
    }
}

