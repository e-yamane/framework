package jp.rough_diamond.commons.service.hibernate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.EntityMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.metadata.ClassMetadata;

import jp.rough_diamond.commons.extractor.Condition;
import jp.rough_diamond.commons.extractor.Extractor;
import jp.rough_diamond.commons.service.BasicService;
import jp.rough_diamond.commons.service.NumberingService;
import jp.rough_diamond.framework.transaction.TransactionAttribute;
import jp.rough_diamond.framework.transaction.TransactionAttributeType;
import jp.rough_diamond.framework.transaction.hibernate.HibernateUtils;

public class HibernateNumberingService extends NumberingService {
    /**
     * 指定されたクラスに対応するテーブルの主キーで
     * 利用されていない最適なナンバーを返却する
     * その際に利用されるキーはエンティティ名となる（Hibernate依存）
     * @param entityClass
     * @return  ナンバー
     */
	@Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED_NEW)
    public synchronized <T> Serializable getNumber(Class<T> entityClass) {
		NumberGenerateInfo info = getGenerateInfo(entityClass);
        long ret = getNumber(info.entityName);
        long stop = ret - 1;
        BasicService bs = BasicService.getService();
        while(ret != stop) {
            Serializable ser = info.supplimenter.suppliment(ret, info.length);
        	Extractor ex = new Extractor(entityClass);
        	ex.add(Condition.eq(info.identifierName, ser));
        	if(bs.getCountByExtractor(ex) == 0) {
        		return ser;
        	}
            ret = getNumber(info.entityName);
        }
        throw new RuntimeException("いっぱいいっぱいです");
    }
	
	@SuppressWarnings("unchecked")
	NumberGenerateInfo getGenerateInfo(Class<?> cl) {
		NumberGenerateInfo ret = genMap.get(cl);
		if(ret != null) {
			return ret;
		}
        Session session = HibernateUtils.getSession();
        SessionFactory sf = session.getSessionFactory();
        ClassMetadata cm = sf.getClassMetadata(cl);
        Supplimenter supplimenter = NUMBERING_ALLOWED_CLASSES.get(cm.getIdentifierType().getReturnedClass());
        if(supplimenter == null) {
            throw new RuntimeException("主キーが自動採番対象オブジェクトではありません");
        }
        Configuration config = HibernateUtils.getConfig();
        PersistentClass pc = config.getClassMapping(cm.getMappedClass(EntityMode.POJO).getName());
        Property prop = pc.getIdentifierProperty();
        Iterator<Column> iter = prop.getColumnIterator();
        int length = -1;
        if(iter.hasNext()) {
        	Column col = iter.next();
        	length = col.getLength();
        } else {
        	throw new RuntimeException("なんか変");
        }
        ret = new NumberGenerateInfo(cm.getEntityName(), length, supplimenter, prop.getName());
        genMap.put(cl, ret);
        return ret;
	}
	
	static class NumberGenerateInfo {
		final String entityName;
		final int length;
		final Supplimenter supplimenter;
		final String identifierName;
		NumberGenerateInfo(String entityName, int length, Supplimenter supplimenter, String identifierName) {
			this.entityName = entityName;
			this.length = length;
			this.supplimenter = supplimenter;
			this.identifierName = identifierName;
		}
	}
	
	Map<Class<?>, NumberGenerateInfo> genMap = new HashMap<Class<?>, NumberGenerateInfo>();
}
