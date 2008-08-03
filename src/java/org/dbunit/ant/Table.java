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

/**
 * The <code>Table</code> class is just a step placeholder for a table name
 * within an <code>Export</code>.
 *
 * @author Timothy Ruppert 
 * @author Ben Cox
 * @version $Revision: 6 $
 * @since Jun 10, 2002
 */
public class Table
{

    private String name;
    private boolean regexp;
    private Export parent;
    
    public Table()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isRegexp() {
    	return regexp;
    }
    
    public void setRegExp(boolean regexp) {
    	this.regexp = regexp;
    }
    
    public String toString()
    {
        StringBuffer result = new StringBuffer();
        result.append("Table: ");
        result.append(" name=" + name);

        return result.toString();
    }
    public Export getParent() {
        return this.parent;
    }
    public void setParent(Export parent) {
        this.parent = parent;
    }
}












