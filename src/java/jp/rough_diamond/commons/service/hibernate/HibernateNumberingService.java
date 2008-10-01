package jp.rough_diamond.commons.service.hibernate;

import java.io.Serializable;
import java.util.Iterator;

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
import jp.rough_diamond.commons.service.FindResult;
import jp.rough_diamond.commons.service.NumberingService;
import jp.rough_diamond.framework.transaction.TransactionAttribute;
import jp.rough_diamond.framework.transaction.TransactionAttributeType;
import jp.rough_diamond.framework.transaction.hibernate.HibernateUtils;

public class HibernateNumberingService extends NumberingService {
    /**
     * �w�肳�ꂽ�N���X�ɑΉ�����e�[�u���̎�L�[��
     * ���p����Ă��Ȃ��œK�ȃi���o�[��ԋp����
     * ���̍ۂɗ��p�����L�[�̓G���e�B�e�B���ƂȂ�iHibernate�ˑ��j
     * @param entityClass
     * @return  �i���o�[
     */
	@Override
	@SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.REQUIRED_NEW)
    public synchronized <T> Serializable getNumber(Class<T> entityClass) {
        Session session = HibernateUtils.getSession();
        SessionFactory sf = session.getSessionFactory();
        ClassMetadata cm = sf.getClassMetadata(entityClass);
        Supplimenter supplimenter = NUMBERING_ALLOWED_CLASSES.get(cm.getIdentifierType().getReturnedClass());
        if(supplimenter == null) {
            throw new RuntimeException("��L�[�������̔ԑΏۃI�u�W�F�N�g�ł͂���܂���");
        }
        Configuration config = HibernateUtils.getConfig();
        PersistentClass pc = config.getClassMapping(cm.getMappedClass(EntityMode.POJO).getName());
        Property prop = pc.getIdentifierProperty();
        Iterator iter = prop.getColumnIterator();
        int length = -1;
        if(iter.hasNext()) {
        	Column col = (Column)iter.next();
        	length = col.getLength();
        } else {
        	throw new RuntimeException("�Ȃ񂩕�");
        }
        long ret = getNumber(cm.getEntityName());
        long stop = ret - 1;
        BasicService bs = BasicService.getService();
        while(ret != stop) {
            Serializable ser = supplimenter.suppliment(ret, length);
        	Extractor ex = new Extractor(entityClass);
        	ex.add(Condition.eq(prop.getName(), ser));
        	ex.setLimit(0);
        	FindResult<T> fr = bs.findByExtractorWithCount(ex);
        	if(fr.count == 0) {
        		return ser;
        	}
            ret = getNumber(cm.getEntityName());
        }
        throw new RuntimeException("�����ς������ς��ł�");
    }
}
