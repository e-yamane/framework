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

package org.dbunit.dataset.datatype;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dbunit.dataset.ITable;

/**
 * @author Manuel Laflamme
 * @version $Revision: 157 $
 */
public class IntegerDataType extends AbstractDataType
{
    IntegerDataType(String name, int sqlType)
    {
        super(name, sqlType, Integer.class, true);
    }

    ////////////////////////////////////////////////////////////////////////////
    // DataType class

    public Object typeCast(Object value) throws TypeCastException
    {
        if (value == null || value == ITable.NO_VALUE)
        {
            return null;
        }

        if (value instanceof Number)
        {
            return new Integer(((Number)value).intValue());
        }

        try
        {
            return typeCast(new BigDecimal(value.toString().trim()));
        }
        catch (java.lang.NumberFormatException e)
        {
            throw new TypeCastException(value, this, e);
        }
    }

    public Object getSqlValue(int column, ResultSet resultSet)
            throws SQLException, TypeCastException
    {
        int value = resultSet.getInt(column);
        if (resultSet.wasNull())
        {
            return null;
        }
        return new Integer(value);
    }

    public void setSqlValue(Object value, int column, PreparedStatement statement)
            throws SQLException, TypeCastException
    {
        statement.setInt(column, ((Integer)typeCast(value)).intValue());
    }
}




