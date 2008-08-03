package jp.rough_diamond.commons.testing;

import java.io.InputStream;
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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.excel.XlsDataSet;
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
    private static Set<Class> modifiedClasses;
    private static TmpService service;
    
    static {
        service = ServiceLocator.getService(TmpService.class); 
        service.replaceInterceptor();
        list = new ArrayList<DBInitializer>();
        initializerObjects = new HashMap<Class, Set<DBInitializer>>();
        modifiedClasses = new HashSet<Class>();
    }
    
    static void clearModifiedClasses() {
        modifiedClasses.clear();
    }
    
    /**
     * 更新対象クラス名を追加する
     * 基本的には呼ばなくて良い
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
        
        public void clearData(DBInitializer initializer) throws Exception {
            initializer.delete();
        }
    }
    
    public void delete() throws Exception {
        execute(DatabaseOperation.DELETE_ALL, getResourceNames());
    }

    public void load() throws Exception {
        if(isAlreadyLoading()) {
            log.debug("既にロードされているため処理を行いません。");
            return;
        }
        String[] resourceNames = getResourceNames();
        execute(DatabaseOperation.INSERT, resourceNames);
        list.add(this);
        
        for(String name : resourceNames) {
        	try {
                InputStream is = this.getClass().getClassLoader().getResourceAsStream(name);
	            HSSFWorkbook wb = new HSSFWorkbook(is);
	            int sheetSize = wb.getNumberOfSheets();
	            for(int i = 0 ; i < sheetSize ; i++) {
	                String sheetName = wb.getSheetName(i).toUpperCase();
	                Class cl = entityMap.get(sheetName);
	                Set<DBInitializer> set = initializerObjects.get(cl);
	                if(set == null) {
	                    set = new HashSet<DBInitializer>();
	                    initializerObjects.put(cl, set);
	                }
	                set.add(this);
	            }
        	} catch(RuntimeException e) {
        		log.warn("以下のリソースを実行中に例外が発生しました。[" + name + "]");
        		throw e;
        	}
        }
        
    }
    
    public void cleanInsert() throws Exception {
        if(isAlreadyLoading()) {
            log.debug("既にロードされているため処理を行いません。");
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
    
    protected void execute(DatabaseOperation operation, String... resourceNames) throws Exception {
    	String schema = HibernateUtils.getConfig().getProperty(Environment.DEFAULT_SCHEMA);
    	
		String[] tmpArray = new String[resourceNames.length];
        System.arraycopy(resourceNames, 0, tmpArray, 0, tmpArray.length);
        List<String> tmp = Arrays.asList(tmpArray);
        if(DatabaseOperation.DELETE_ALL.equals(operation)) {
            Collections.reverse(tmp);
        }
        ConnectionManager cm = ConnectionManager.getConnectionManager();
        //TODO nullでよいかなぁ。。。
        IDatabaseConnection idc = new DatabaseConnection(cm.getCurrentConnection(null), schema);
        for(String name : tmp) {
        	try {
        		System.out.println(name + ":" + operation.getClass().getName());
	            operation.execute(idc, new XlsDataSet(
	                    this.getClass().getClassLoader().getResourceAsStream(name)));
        	} catch(RuntimeException e) {
        		log.warn("以下のリソースを実行中に例外が発生しました：[" + name + "]");
        		throw e;
        	}
        }
    }

}
