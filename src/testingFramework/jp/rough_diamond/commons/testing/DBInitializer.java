package jp.rough_diamond.commons.testing;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.rough_diamond.framework.service.Service;
import jp.rough_diamond.framework.service.ServiceLocator;
import jp.rough_diamond.framework.transaction.ConnectionManager;
import jp.rough_diamond.framework.transaction.hibernate.HibernateUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.ext.oracle.OracleDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Interceptor;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.mapping.PersistentClass;

@SuppressWarnings("unchecked")
abstract public class DBInitializer implements Service {
    private final static Log log = LogFactory.getLog(DBInitializer.class);
    private static List<DBInitializer> list;
    private static Map<String, Class> entityMap;
    private static Map<Class, Set<DBInitializer>> initializerObjects; 
    static Set<Class> modifiedClasses;
    private static TmpService service;
    
    private final DatabaseOperation INSERT = new InsertOperationExt(this);
    
    public final static String TEST_DATA_CONTROLER = "RDF_TEST_DATA_CONTROLER";
    
    static {
    	try {
	        service = ServiceLocator.getService(TmpService.class); 
	        service.init();
	        list = new ArrayList<DBInitializer>();
	        initializerObjects = new HashMap<Class, Set<DBInitializer>>();
	        modifiedClasses = new HashSet<Class>();
    	} catch(Exception e) {
    		throw new ExceptionInInitializerError(e);
    	}
    }
    
    static void clearModifiedClasses() {
        modifiedClasses.clear();
    }
    
    /**
     * �X�V�ΏۃN���X����ǉ�����
     * ��{�I�ɂ͌Ă΂Ȃ��ėǂ�
     * @param cl
     */
    public static void addModifiedClasses(Class cl) {
        modifiedClasses.add(cl);
    }
    
    static void clearModifiedData() throws Exception {
        int minimumIndex = Integer.MAX_VALUE;
        for(Class cl : modifiedClasses) {
            Set<DBInitializer> set = initializerObjects.get(cl);
            if(set == null) {
                continue;
            }
            for(int i = 0 ; i < list.size() ; i++) {
                if(set.contains(list.get(i))) {
                    minimumIndex = Math.min(minimumIndex, i);
                    break;
                }
            }
        }
        if(minimumIndex >= list.size()) {
            return;
        }
        for(int i = list.size() -1 ; i >= minimumIndex ; i--) {
            service.clearData(list.get(i));
        }
        list = list.subList(0, minimumIndex);
    }
    
    public static class TmpService implements Service {
        public void replaceInterceptor() {
            Configuration config = HibernateUtils.getConfig();
            Interceptor org = config.getInterceptor();
            Interceptor tmp = new InterceptorImpl(org);
            config.setInterceptor(tmp);
            HibernateUtils.rebuildSessionFactory();
            
            entityMap = new HashMap<String, Class>();
            Iterator iterator = HibernateUtils.getConfig().getClassMappings();
            while(iterator.hasNext()) {
                PersistentClass pc = (PersistentClass)iterator.next();
                entityMap.put(pc.getTable().getName().toUpperCase(), pc.getMappedClass());
            }
        }
        
        public void init() throws SQLException {
        	replaceInterceptor();
        	createTestDataTable();
        }
        
        void createTestDataTable() throws SQLException {
        	Connection con = ConnectionManager.getConnectionManager().getCurrentConnection(null);
        	String schema = HibernateUtils.getConfig().getProperty(Environment.DEFAULT_SCHEMA);
        	if(!DatabaseUtils.isExistsTable(con, schema, TEST_DATA_CONTROLER)) {
        		Statement stmt = con.createStatement();
        		try {
        			stmt.execute(
        					String.format("create table %s(name varchar(4000), test_table varchar(32), ts varchar(20))",
        							TEST_DATA_CONTROLER));
        		} finally {
        			stmt.close();
        		}
        	}
        }
        
        public void clearData(DBInitializer initializer) throws Exception {
            initializer.delete();
        }
    }
    
    public void delete() throws Exception {
        execute(DatabaseOperation.DELETE_ALL, getResourceNames());
    }

    public void load() throws Exception {
        if(isAlreadyLoading()) {
            log.debug("���Ƀ��[�h����Ă��邽�ߏ������s���܂���B");
            return;
        }
        String[] resourceNames = getResourceNames();
        execute(INSERT, resourceNames);
        list.add(this);
    }
    
    void addInitializedObject(String name) {
        Class cl = entityMap.get(name.toUpperCase());
        Set<DBInitializer> set = initializerObjects.get(cl);
        if(set == null) {
            set = new HashSet<DBInitializer>();
            initializerObjects.put(cl, set);
        }
        set.add(this);
    }
    
    public void cleanInsert() throws Exception {
        if(isAlreadyLoading()) {
            log.debug("���Ƀ��[�h����Ă��邽�ߏ������s���܂���B");
            return;
        }
        delete();
        load();
    }
    
    private boolean isAlreadyLoading() {
        for(DBInitializer target : list) {
            if(target == this) {
                return true;
            }
        }
        return false;
    }
    
    abstract protected String[] getResourceNames();
    
    private static Map<String, IDataSet> datasetMap = new HashMap<String, IDataSet>();
    protected void execute(DatabaseOperation operation, String... resourceNames) throws Exception {
    	String schema = HibernateUtils.getConfig().getProperty(Environment.DEFAULT_SCHEMA);
    	String driverClassName = HibernateUtils.getConfig().getProperty(Environment.DRIVER);
    	
		String[] tmpArray = new String[resourceNames.length];
        System.arraycopy(resourceNames, 0, tmpArray, 0, tmpArray.length);
        List<String> tmp = Arrays.asList(tmpArray);
        if(DatabaseOperation.DELETE_ALL.equals(operation)) {
            Collections.reverse(tmp);
        }
        ConnectionManager cm = ConnectionManager.getConnectionManager();
        //TODO null�ł悢���Ȃ��B�B�B
        IDatabaseConnection idc = new DatabaseConnection(cm.getCurrentConnection(null), schema);
        if(driverClassName.toUpperCase().indexOf("ORACLE") != -1) {
        	//Oracle�̊g��
        	idc.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new OracleDataTypeFactory());
        }
        for(String name : tmp) {
        	try {
        		System.out.println(name + ":" + operation.getClass().getName());
        		IDataSet dataset = datasetMap.get(name);
        		if(dataset == null) {
	        		if(name.endsWith("xls")) {
	        			dataset = new XlsDataSet(
	    	                    this.getClass().getClassLoader().getResourceAsStream(name)); 
	        		} else if(name.endsWith(".xml")) {
	        			dataset = new XmlDataSet(
	    	                    this.getClass().getClassLoader().getResourceAsStream(name)); 
	        		} else {
	        			throw new RuntimeException();
	        		}
	        		dataset = new DataSetProxy(name, dataset);
	        		datasetMap.put(name, dataset);
        		}
	            operation.execute(idc, dataset);
        	} catch(RuntimeException e) {
        		log.warn("�ȉ��̃��\�[�X�����s���ɗ�O���������܂����F[" + name + "]");
        		throw e;
        	}
        }
    }

}
