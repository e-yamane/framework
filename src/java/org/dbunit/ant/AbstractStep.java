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
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.CachedResultSetTableFactory;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.ForwardOnlyResultSetTableFactory;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.IResultSetTableFactory;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.CachedDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvProducer;
import org.dbunit.dataset.stream.IDataSetProducer;
import org.dbunit.dataset.stream.StreamingDataSet;
import org.dbunit.dataset.xml.FlatDtdProducer;
import org.dbunit.dataset.xml.FlatXmlProducer;
import org.dbunit.dataset.xml.XmlProducer;
import org.xml.sax.InputSource;

/**
 * @author Manuel Laflamme
 * @since Apr 3, 2004
 * @version $Revision: 157 $
 */
public abstract class AbstractStep implements DbUnitTaskStep
{
    public static final String FORMAT_FLAT = "flat";
    public static final String FORMAT_XML = "xml";
    public static final String FORMAT_DTD = "dtd";
    public static final String FORMAT_CSV = "csv";
    public static final String FORMAT_EXCEL = "excel";

    @SuppressWarnings("unchecked")
    protected IDataSet getDatabaseDataSet(IDatabaseConnection connection,
            List tables, boolean forwardonly) throws DatabaseUnitException
    {
        try
        {
            // Setup the ResultSet table factory
            IResultSetTableFactory factory = null;
            if (forwardonly)
            {
                factory = new ForwardOnlyResultSetTableFactory();
            }
            else
            {
                factory = new CachedResultSetTableFactory();
            }
            DatabaseConfig config = connection.getConfig();
            config.setProperty(DatabaseConfig.PROPERTY_RESULTSET_TABLE_FACTORY,
                    factory);

            // Retrieve the complete database if no tables or queries specified.
            if (tables.size() == 0)
            {
                return connection.createDataSet();
            }

            QueryDataSet queryDataset = new QueryDataSet(connection);
            for (Iterator it = tables.iterator(); it.hasNext();)
            {
                Object item = it.next();
                if (item instanceof Query)
                {
                    Query queryItem = (Query)item;
                    if(queryItem.isRegexp())
                    {
                    	addTables(connection, queryItem, queryDataset);
                    }
                    else
                    {
                        queryDataset.addTable(queryItem.getName(), queryItem.getSql());
                    }
                }
                else
                {
                    Table tableItem = (Table)item;
                    if(tableItem.isRegexp()) 
                    {
                    	String[] names = getNames(connection, tableItem);
                    	for(int i = 0 ; i < names.length ; i++) {
                            queryDataset.addTable(names[i]);
                    	}
                    }
                    else
                    {
                        queryDataset.addTable(tableItem.getName());
                    }
                }
            }

            return queryDataset;
        }
        catch (SQLException e)
        {
            throw new DatabaseUnitException(e);
        }
    }

    private void addTables(IDatabaseConnection idcon, Query query, QueryDataSet qds) throws SQLException {
    	String[] queries = RegexpUtil.getQueries(idcon, query.getSql(), getParent().getSchema());
    	String name = query.getName();
    	for(int i = 0 ; i < queries.length ; i++) {
    		qds.addTable(name + i, queries[i]);
    	}
    }

    String[] getNames(IDatabaseConnection idcon, Table table) throws SQLException {
    	return RegexpUtil.getNames(idcon, table.getName(), getParent().getSchema());
    }
    
    @SuppressWarnings("deprecation")
	protected IDataSet getSrcDataSet(File src, String format,
            boolean forwardonly) throws DatabaseUnitException
    {
        try
        {
            IDataSetProducer producer = null;
            if (format.equalsIgnoreCase(FORMAT_XML))
            {
                producer = new XmlProducer(new InputSource(src.toURL().toString()));
            }
            else if (format.equalsIgnoreCase(FORMAT_CSV))
            {
                producer = new CsvProducer(src);
            }
            else if (format.equalsIgnoreCase(FORMAT_FLAT))
            {
                producer = new FlatXmlProducer(new InputSource(src.toURL().toString()));
            }
            else if (format.equalsIgnoreCase(FORMAT_DTD))
            {
                producer = new FlatDtdProducer(new InputSource(src.toURL().toString()));
            }
            else
            {
                throw new IllegalArgumentException("Type must be either 'flat'(default), 'xml', 'csv' or 'dtd' but was: " + format);
            }

            if (forwardonly)
            {
                return new StreamingDataSet(producer);
            }
            return new CachedDataSet(producer);
        }
        catch (IOException e)
        {
            throw new DatabaseUnitException(e);
        }
    }
    
    static class MultipleQuery {
    	String query;
    	String[][] tables;
    }

    private DbUnitTask parent;

    public DbUnitTask getParent() {
        return this.parent;
    }

    public void setParent(DbUnitTask parent) {
        this.parent = parent;
    }
}
