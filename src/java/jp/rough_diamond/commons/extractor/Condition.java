/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.extractor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

/**
 * ���o������\���N���X
 * @author $Author: Yamane_Eiji@bp.ogis-ri.co.jp $
 * @date $Date: 2006-02-14 17:11:42 +0900 (火, 14 2 2006) $
 */
@SuppressWarnings("unchecked")
abstract public class Condition implements Serializable{
	private static final long serialVersionUID = 1L;

	/**
	 * And�����I�u�W�F�N�g�𐶐�����
	 * @return And�����I�u�W�F�N�g
	 */
	public static CombineCondition and() {
		return new And();
	}
	
	/**
	 * And�����I�u�W�F�N�g�𐶐�����
	 * @param conditions	And������������Q
	 * @return 			And�����I�u�W�F�N�g
	 */
	public static CombineCondition and(Collection<Condition> conditions) {
		return new And(conditions);
	}
	
	/**
	 * And�����I�u�W�F�N�g�𐶐�����
	 * @param conditions	And������������Q
	 * @return 			And�����I�u�W�F�N�g
	 */
	public static CombineCondition and(Condition... conditions) {
		return new And(Arrays.asList(conditions));
	}
	
	/**
	 * Eq�����I�u�W�F�N�g�𐶐�����
	 * @param propertyName �v���p�e�B��
	 * @param value		�l
	 * @return				Eq�����I�u�W�F�N�g
	 */
	public static ValueHoldingCondition eq(String propertyName, Object value) {
		return new Eq(propertyName, null, null, value);
	}
	
    /**
     * Eq�����I�u�W�F�N�g�𐶐�����
     * @param propertyName �v���p�e�B��
     * @param target    �v���p�e�B��ێ����Ă���G���e�B�e�B�N���X
     * @param aliase    �G���e�B�e�B�̕ʖ�
     * @param value     �l
     * @return              Eq�����I�u�W�F�N�g
     */
    public static ValueHoldingCondition eq(String propertyName, Class target, String aliase, Object value) {
        return new Eq(propertyName, target, aliase, value);
    }
    
	/**
	 * Ge�����I�u�W�F�N�g�𐶐�����
	 * @param propertyName �v���p�e�B��
     * @param target    �v���p�e�B��ێ����Ă���G���e�B�e�B�N���X
     * @param aliase    �G���e�B�e�B�̕ʖ�
	 * @param value		�l
	 * @return				Ge�����I�u�W�F�N�g
	 */
	public static ValueHoldingCondition ge(String propertyName, Object value) {
		return new Ge(propertyName, null, null, value);
	}

    /**
     * Ge�����I�u�W�F�N�g�𐶐�����
     * @param propertyName �v���p�e�B��
     * @param value     �l
     * @return              Ge�����I�u�W�F�N�g
     */
    public static ValueHoldingCondition ge(String propertyName, Class target, String aliase, Object value) {
        return new Ge(propertyName, target, aliase, value);
    }

	/**
	 * Gt�����I�u�W�F�N�g�𐶐�����
	 * @param propertyName �v���p�e�B��
	 * @param value		�l
	 * @return				Gt�����I�u�W�F�N�g
	 */
	public static ValueHoldingCondition gt(String propertyName, Object value) {
		return new Gt(propertyName, null, null, value);
	}

    /**
     * Gt�����I�u�W�F�N�g�𐶐�����
     * @param propertyName �v���p�e�B��
     * @param target    �v���p�e�B��ێ����Ă���G���e�B�e�B�N���X
     * @param aliase    �G���e�B�e�B�̕ʖ�
     * @param value     �l
     * @return              Gt�����I�u�W�F�N�g
     */
    public static ValueHoldingCondition gt(String propertyName, Class target, String aliase, Object value) {
        return new Gt(propertyName, target, aliase, value);
    }

	/**
	 * In�����I�u�W�F�N�g�𐶐�����
	 * @param propertyName �v���p�e�B��
	 * @param value		�l�Q
	 * @return				In�����I�u�W�F�N�g
	 */
	public static ValueHoldingCondition in(String propertyName, Object... values) {
		return in(propertyName, Arrays.asList(values));
	}

	/**
	 * In�����I�u�W�F�N�g�𐶐�����
	 * @param propertyName �v���p�e�B��
	 * @param value		�l�Q
	 * @return				In�����I�u�W�F�N�g
	 */
	public static ValueHoldingCondition in(String propertyName, Collection values) {
		return new In(propertyName, null, null, values);
	}

    /**
     * In�����I�u�W�F�N�g�𐶐�����
     * @param propertyName �v���p�e�B��
     * @param target    �v���p�e�B��ێ����Ă���G���e�B�e�B�N���X
     * @param aliase    �G���e�B�e�B�̕ʖ�
     * @param value     �l�Q
     * @return              In�����I�u�W�F�N�g
     */
    public static ValueHoldingCondition in(String propertyName, Class target, String aliase, Object... values) {
        return in(propertyName, target, aliase, Arrays.asList(values));
    }
    
    /**
     * NotIn�����I�u�W�F�N�g�𐶐�����
     * @param propertyName �v���p�e�B��
     * @param values �l�Q
     * @return NotIn	�����I�u�W�F�N�g
     */
    public static ValueHoldingCondition notIn(String propertyName, Collection values) {
    	return new NotIn(propertyName, null, null, values);
    }
    
    /**
     * NotIn�����I�u�W�F�N�g�𐶐�����
     * @param propertyName �v���p�e�B��
     * @param values �l�Q
     * @return NotIn	�����I�u�W�F�N�g
     */
    public static ValueHoldingCondition notIn(String propertyName, Object... values) {
    	return notIn(propertyName, null, null, values);
    }

    /**
     * NotIn�����I�u�W�F�N�g�𐶐�����
     * @param propertyName �v���p�e�B��
     * @param target    �v���p�e�B��ێ����Ă���G���e�B�e�B�N���X
     * @param aliase    �G���e�B�e�B�̕ʖ�
     * @param value     �l�Q
     * @return              NotIn�����I�u�W�F�N�g
     */
    public static ValueHoldingCondition notIn(String propertyName, Class target, String aliase, Object... values) {
        return new NotIn(propertyName, target, aliase, Arrays.asList(values));
    }

    /**
     * In�����I�u�W�F�N�g�𐶐�����
     * @param propertyName �v���p�e�B��
     * @param target    �v���p�e�B��ێ����Ă���G���e�B�e�B�N���X
     * @param aliase    �G���e�B�e�B�̕ʖ�
     * @param value     �l�Q
     * @return              In�����I�u�W�F�N�g
     */
    public static ValueHoldingCondition in(String propertyName, Class target, String aliase, Collection values) {
        return new In(propertyName, target, aliase, values);
    }
    
    /**
     * ���������I�u�W�F�N�g�𐶐�����
     * @param target            �^�[�Q�b�g�N���X
     * @param targetProperty    �^�[�Q�b�g�v���p�e�B null�A�󔒂̏ꍇ��target�ƒ��ڌ�������
     * @param targetAlias       �^�[�Q�b�g�G�C���A�X null�A�󔒂̏ꍇ�̓G�C���A�X�Ȃ�
     * @param joined            ������N���X
     * @param joinedProperty   �����Ή��v���p�e�B null�A�󔒂̏ꍇ��joined�ƒ��ڌ�������
     * @param alias             ������G�C���A�X null�A�󔒂̏ꍇ�̓G�C���A�X�Ȃ�
     * @return ���������I�u�W�F�N�g
     */
    public static InnerJoin innerJoin(Class target, String targetProperty, String targetAlias, Class joined, String joinedProperty, String joinedAlias) {
       return new InnerJoin(target, targetProperty, targetAlias, joined, joinedProperty, joinedAlias); 
    }
    
	/**
	 * IsNotNull�����I�u�W�F�N�g�𐶐�����
	 * @param propertyName �v���p�e�B��
	 * @return				IsNotNull�����I�u�W�F�N�g
	 */
	public static LabelHoldingCondition isNotNull(String propertyName) {
		return new IsNotNull(propertyName, null, null);
	}

    /**
     * IsNotNull�����I�u�W�F�N�g�𐶐�����
     * @param propertyName �v���p�e�B��
     * @param target    �v���p�e�B��ێ����Ă���G���e�B�e�B�N���X
     * @param aliase    �G���e�B�e�B�̕ʖ�
     * @return              IsNotNull�����I�u�W�F�N�g
     */
    public static LabelHoldingCondition isNotNull(String propertyName, Class target, String aliase) {
        return new IsNotNull(propertyName, target, aliase);
    }

	/**
	 * IsNull�����I�u�W�F�N�g�𐶐�����
	 * @param propertyName �v���p�e�B��
	 * @return				IsNull�����I�u�W�F�N�g
	 */
	public static LabelHoldingCondition isNull(String propertyName) {
		return new IsNull(propertyName, null, null);
	}

    /**
     * IsNull�����I�u�W�F�N�g�𐶐�����
     * @param propertyName �v���p�e�B��
     * @param target    �v���p�e�B��ێ����Ă���G���e�B�e�B�N���X
     * @param aliase    �G���e�B�e�B�̕ʖ�
     * @return              IsNull�����I�u�W�F�N�g
     */
    public static LabelHoldingCondition isNull(String propertyName, Class target, String aliase) {
        return new IsNull(propertyName, target, aliase);
    }

	/**
	 * �O�����������I�u�W�F�N�g�𐶐�����
	 * @param entityName 	�O���G���e�B�e�B��
	 * @return				�O�����������I�u�W�F�N�g
	 */
	public static Join join(String entityName) {
		return new Join(entityName);
	}

	/**
	 * Le�����I�u�W�F�N�g�𐶐�����
	 * @param propertyName �v���p�e�B��
     * @param target    �v���p�e�B��ێ����Ă���G���e�B�e�B�N���X
     * @param aliase    �G���e�B�e�B�̕ʖ�
	 * @param value		�l
	 * @return				Le�����I�u�W�F�N�g
	 */
	public static ValueHoldingCondition le(String propertyName, Object value) {
		return new Le(propertyName, null, null, value);
	}

    /**
     * Le�����I�u�W�F�N�g�𐶐�����
     * @param propertyName �v���p�e�B��
     * @param value     �l
     * @return              Le�����I�u�W�F�N�g
     */
    public static ValueHoldingCondition le(String propertyName, Class target, String aliase, Object value) {
        return new Le(propertyName, target, aliase, value);
    }

	/**
	 * Like�����I�u�W�F�N�g�𐶐�����
	 * @param propertyName �v���p�e�B��
	 * @param value		�l
	 * @return				Like�����I�u�W�F�N�g
	 */
	public static ValueHoldingCondition like(String propertyName, Object value) {
		return new Like(propertyName, null, null, value);
	}

    /**
     * Like�����I�u�W�F�N�g�𐶐�����
     * @param propertyName �v���p�e�B��
     * @param target    �v���p�e�B��ێ����Ă���G���e�B�e�B�N���X
     * @param aliase    �G���e�B�e�B�̕ʖ�
     * @param value     �l
     * @return              Like�����I�u�W�F�N�g
     */
    public static ValueHoldingCondition like(String propertyName, Class target, String aliase, Object value) {
        return new Like(propertyName, target, aliase, value);
    }

	/**
	 * Lt�����I�u�W�F�N�g�𐶐�����
	 * @param propertyName �v���p�e�B��
	 * @param value		�l
	 * @return				Lt�����I�u�W�F�N�g
	 */
	public static ValueHoldingCondition lt(String propertyName, Object value) {
		return new Lt(propertyName, null, null, value);
	}
	
    /**
     * Lt�����I�u�W�F�N�g�𐶐�����
     * @param propertyName �v���p�e�B��
     * @param target    �v���p�e�B��ێ����Ă���G���e�B�e�B�N���X
     * @param aliase    �G���e�B�e�B�̕ʖ�
     * @param value     �l
     * @return              Lt�����I�u�W�F�N�g
     */
    public static ValueHoldingCondition lt(String propertyName, Class target, String aliase, Object value) {
        return new Lt(propertyName, target, aliase, value);
    }
    
    /**
     * NotEq�����I�u�W�F�N�g�𐶐�����
     * @param propertyName �v���p�e�B��
     * @param value     �l
     * @return              NotEq�����I�u�W�F�N�g
     */
    public static ValueHoldingCondition notEq(String propertyName, Object value) {
        return new NotEq(propertyName, null, null, value);
    }

	/**
	 * NotEq�����I�u�W�F�N�g�𐶐�����
	 * @param propertyName �v���p�e�B��
     * @param target    �v���p�e�B��ێ����Ă���G���e�B�e�B�N���X
     * @param aliase    �G���e�B�e�B�̕ʖ�
	 * @param value		�l
	 * @return				NotEq�����I�u�W�F�N�g
	 */
	public static ValueHoldingCondition notEq(String propertyName, Class target, String aliase, Object value) {
		return new NotEq(propertyName, target, aliase, value);
	}

	/**
	 * Or�����I�u�W�F�N�g�𐶐�����
	 * @return 			Or�����I�u�W�F�N�g
	 */
	public static CombineCondition or() {
		return new Or();
	}

	/**
	 * Or�����I�u�W�F�N�g�𐶐�����
	 * @param conditions	Or������������Q
	 * @return 			Or�����I�u�W�F�N�g
	 */
    public static CombineCondition or(Collection<Condition> conditions) {
		return new Or(conditions);
	}
	
	/**
	 * Or�����I�u�W�F�N�g�𐶐�����
	 * @param conditions	Or������������Q
	 * @return 			Or�����I�u�W�F�N�g
	 */
	public static CombineCondition or(Condition... conditions) {
		return or(Arrays.asList(conditions));
	}

    /**
     * RegEx�����I�u�W�F�N�g�𐶐�����
     * @param propertyName �v���p�e�B��
     * @param target    �v���p�e�B��ێ����Ă���G���e�B�e�B�N���X
     * @param aliase    �G���e�B�e�B�̕ʖ�
     * @param value     �l
     * @return              RegExp�����I�u�W�F�N�g
     */
    public static ValueHoldingCondition regex(String propertyName, Object value) {
        return regex(propertyName, null, null, value);
    }

    /**
     * RegEx�����I�u�W�F�N�g�𐶐�����
     * @param propertyName �v���p�e�B��
     * @param target    �v���p�e�B��ێ����Ă���G���e�B�e�B�N���X
     * @param aliase    �G���e�B�e�B�̕ʖ�
     * @param value     �l
     * @return              RegExp�����I�u�W�F�N�g
     */
    public static ValueHoldingCondition regex(String propertyName, Class target, String aliase, Object value) {
        return new RegularExp(propertyName, target, aliase, value);
    }
}
