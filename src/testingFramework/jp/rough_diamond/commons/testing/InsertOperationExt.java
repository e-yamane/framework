package jp.rough_diamond.commons.testing;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableIterator;
import org.dbunit.operation.AbstractOperation;

public class InsertOperationExt extends AbstractOperation {
	private final DBInitializer initializer;
	InsertOperationExt(DBInitializer initializer) {
		this.initializer = initializer;
	}
	
	private Map<IDataSet, List<String>> tableNameMap = new HashMap<IDataSet, List<String>>();
	
    public void execute(IDatabaseConnection connection,
            IDataSet dataSet) throws DatabaseUnitException, SQLException {
    	INSERT.execute(connection, dataSet);
    	List<String> tableNames = getTableNames(connection, dataSet);
    	for(String tableName : tableNames) {
    		initializer.addInitializedObject(tableName);
    	}
    }

	List<String> getTableNames(IDatabaseConnection connection, IDataSet dataSet) throws DataSetException {
		List<String> ret = tableNameMap.get(dataSet);
		if(ret == null) {
			ret = makeTableNameList(connection, dataSet);
			tableNameMap.put(dataSet, ret);
		}
		return ret;
	}

	List<String> makeTableNameList(IDatabaseConnection connection, IDataSet dataSet) throws DataSetException {
		List<String> ret = new ArrayList<String>();
		ITableIterator iterator = dataSet.iterator();
        while (iterator.next())
        {
            ITable table = iterator.getTable();
            ret.add(table.getTableMetaData().getTableName());
        }
		return ret;
	}

}
