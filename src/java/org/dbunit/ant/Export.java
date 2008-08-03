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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvDataSetWriter;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlWriter;
import org.dbunit.dataset.xml.XmlDataSet;

/**
 * The <code>Export</code> class is the step that facilitates exporting
 * the contents of the database and/or it's corresponding dtd to a file.
 * The export can be performed on a full dataset or a partial one if
 * specific table names are identified.
 *
 * @author Timothy Ruppert
 * @author Ben Cox
 * @version $Revision: 157 $
 * @since Jun 10, 2002
 * @see DbUnitTaskStep
 */
public class Export extends AbstractStep
{

    private File _dest;
    private String _format = FORMAT_FLAT;
    private String _doctype = null;
    private List<Object> _tables = new ArrayList<Object>();
    public Export()
    {
    }

    private String getAbsolutePath(File filename)
    {
        return filename != null ? filename.getAbsolutePath() : "null";
    }

    public File getDest()
    {
        return _dest;
    }

    public String getFormat()
    {
        return _format;
    }

    public List<Object> getTables()
    {
        return _tables;
    }

    protected void setTables(List<Object> tables) {
    	this._tables = tables;
    }
    
    public void setDest(File dest)
    {
        _dest = dest;
    }

    public void setFormat(String format)
    {
        if (format.equalsIgnoreCase(FORMAT_FLAT)
                || format.equalsIgnoreCase(FORMAT_XML)
                || format.equalsIgnoreCase(FORMAT_DTD)
				|| format.equalsIgnoreCase(FORMAT_EXCEL)
                || format.equalsIgnoreCase(FORMAT_CSV))
        {
            _format = format;
        }
        else
        {
            throw new IllegalArgumentException("Type must be one of: 'flat'(default), 'xml', or 'dtd' but was: " + format);
        }
    }

    public void addTable(Table table)
    {
        table.setParent(this);
        _tables.add(table);
    }

    public void addQuery(Query query)
    {
        query.setParent(this);
        _tables.add(query);
    }

    public String getDoctype()
    {
        return _doctype;
    }

    public void setDoctype(String doctype)
    {
        _doctype = doctype;
    }

    public void execute(IDatabaseConnection connection) throws DatabaseUnitException
    {
    	try
        {
            if (_dest == null)
            {
                throw new DatabaseUnitException("'_dest' is a required attribute of the <export> step.");
            }

            IDataSet dataset = getDatabaseDataSet(connection, _tables, false);

            // Write the dataset
            if (_format.equals(FORMAT_CSV))
            {
                CsvDataSetWriter.write(dataset, _dest);
            }
            else
            {
                OutputStream out = new FileOutputStream(_dest);
                try
                {
                    if (_format.equalsIgnoreCase(FORMAT_FLAT))
                    {
                        FlatXmlWriter writer = new FlatXmlWriter(out);
                        writer.setDocType(_doctype);
                        writer.write(dataset);
                    }
                    else if (_format.equalsIgnoreCase(FORMAT_XML))
                    {
                        XmlDataSet.write(dataset, out);
                    }
                    else if (_format.equalsIgnoreCase(FORMAT_DTD))
                    {
                        FlatDtdDataSet.write(dataset, out);
                    }
                    else if (_format.equalsIgnoreCase(FORMAT_EXCEL)) {
                    	XlsDataSet.write(dataset, out);
                    }
                }
                finally
                {
                    out.close();
                }
            }
        }
        catch (IOException e)
        {
            throw new DatabaseUnitException(e);
        }
    }

    public String getLogMessage()
    {
        return "Executing export: "
                + "\n      in format: " + _format
                + " to datafile: " + getAbsolutePath(_dest);
    }


    public String toString()
    {
        StringBuffer result = new StringBuffer();
        result.append("Export: ");
        result.append(" dest=" + getAbsolutePath(_dest));
        result.append(", format= " + _format);
        result.append(", doctype= " + _doctype);
        result.append(", tables= " + _tables);

        return result.toString();
    }
}

