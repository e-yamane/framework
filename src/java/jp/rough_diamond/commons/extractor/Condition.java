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
 * 抽出条件を表すクラス
 * @author $Author: Yamane_Eiji@bp.ogis-ri.co.jp $
 * @date $Date: 2006-02-14 17:11:42 +0900 (轣ｫ, 14 2 2006) $
 */
@SuppressWarnings("unchecked")
abstract public class Condition implements Serializable{
	private static final long serialVersionUID = 1L;

	/**
	 * And条件オブジェクトを生成する
	 * @return And条件オブジェクト
	 */
	public static CombineCondition and() {
		return new And();
	}
	
	/**
	 * And条件オブジェクトを生成する
	 * @param conditions	And結合する条件群
	 * @return 			And条件オブジェクト
	 */
	public static CombineCondition and(Collection<Condition> conditions) {
		return new And(conditions);
	}
	
	/**
	 * And条件オブジェクトを生成する
	 * @param conditions	And結合する条件群
	 * @return 			And条件オブジェクト
	 */
	public static CombineCondition and(Condition... conditions) {
		return new And(Arrays.asList(conditions));
	}
	
	/**
	 * Eq条件オブジェクトを生成する
	 * @param propertyName プロパティ名
	 * @param value		値
	 * @return				Eq条件オブジェクト
	 */
	public static ValueHoldingCondition eq(String propertyName, Object value) {
		return new Eq(propertyName, null, null, value);
	}
	
    /**
     * Eq条件オブジェクトを生成する
     * @param propertyName プロパティ名
     * @param target    プロパティを保持しているエンティティクラス
     * @param aliase    エンティティの別名
     * @param value     値
     * @return              Eq条件オブジェクト
     */
    public static ValueHoldingCondition eq(String propertyName, Class target, String aliase, Object value) {
        return new Eq(propertyName, target, aliase, value);
    }
    
	/**
	 * Ge条件オブジェクトを生成する
	 * @param propertyName プロパティ名
     * @param target    プロパティを保持しているエンティティクラス
     * @param aliase    エンティティの別名
	 * @param value		値
	 * @return				Ge条件オブジェクト
	 */
	public static ValueHoldingCondition ge(String propertyName, Object value) {
		return new Ge(propertyName, null, null, value);
	}

    /**
     * Ge条件オブジェクトを生成する
     * @param propertyName プロパティ名
     * @param value     値
     * @return              Ge条件オブジェクト
     */
    public static ValueHoldingCondition ge(String propertyName, Class target, String aliase, Object value) {
        return new Ge(propertyName, target, aliase, value);
    }

	/**
	 * Gt条件オブジェクトを生成する
	 * @param propertyName プロパティ名
	 * @param value		値
	 * @return				Gt条件オブジェクト
	 */
	public static ValueHoldingCondition gt(String propertyName, Object value) {
		return new Gt(propertyName, null, null, value);
	}

    /**
     * Gt条件オブジェクトを生成する
     * @param propertyName プロパティ名
     * @param target    プロパティを保持しているエンティティクラス
     * @param aliase    エンティティの別名
     * @param value     値
     * @return              Gt条件オブジェクト
     */
    public static ValueHoldingCondition gt(String propertyName, Class target, String aliase, Object value) {
        return new Gt(propertyName, target, aliase, value);
    }

	/**
	 * In条件オブジェクトを生成する
	 * @param propertyName プロパティ名
	 * @param value		値群
	 * @return				In条件オブジェクト
	 */
	public static ValueHoldingCondition in(String propertyName, Object... values) {
		return in(propertyName, Arrays.asList(values));
	}

	/**
	 * In条件オブジェクトを生成する
	 * @param propertyName プロパティ名
	 * @param value		値群
	 * @return				In条件オブジェクト
	 */
	public static ValueHoldingCondition in(String propertyName, Collection values) {
		return new In(propertyName, null, null, values);
	}

    /**
     * In条件オブジェクトを生成する
     * @param propertyName プロパティ名
     * @param target    プロパティを保持しているエンティティクラス
     * @param aliase    エンティティの別名
     * @param value     値群
     * @return              In条件オブジェクト
     */
    public static ValueHoldingCondition in(String propertyName, Class target, String aliase, Object... values) {
        return in(propertyName, target, aliase, Arrays.asList(values));
    }
    
    /**
     * NotIn条件オブジェクトを生成する
     * @param propertyName プロパティ名
     * @param values 値群
     * @return NotIn	条件オブジェクト
     */
    public static ValueHoldingCondition notIn(String propertyName, Collection values) {
    	return new NotIn(propertyName, null, null, values);
    }
    
    /**
     * NotIn条件オブジェクトを生成する
     * @param propertyName プロパティ名
     * @param values 値群
     * @return NotIn	条件オブジェクト
     */
    public static ValueHoldingCondition notIn(String propertyName, Object... values) {
    	return notIn(propertyName, null, null, values);
    }

    /**
     * NotIn条件オブジェクトを生成する
     * @param propertyName プロパティ名
     * @param target    プロパティを保持しているエンティティクラス
     * @param aliase    エンティティの別名
     * @param value     値群
     * @return              NotIn条件オブジェクト
     */
    public static ValueHoldingCondition notIn(String propertyName, Class target, String aliase, Object... values) {
        return new NotIn(propertyName, target, aliase, Arrays.asList(values));
    }

    /**
     * In条件オブジェクトを生成する
     * @param propertyName プロパティ名
     * @param target    プロパティを保持しているエンティティクラス
     * @param aliase    エンティティの別名
     * @param value     値群
     * @return              In条件オブジェクト
     */
    public static ValueHoldingCondition in(String propertyName, Class target, String aliase, Collection values) {
        return new In(propertyName, target, aliase, values);
    }
    
    /**
     * 内部結合オブジェクトを生成する
     * @param target            ターゲットクラス
     * @param targetProperty    ターゲットプロパティ null、空白の場合はtargetと直接結合する
     * @param targetAlias       ターゲットエイリアス null、空白の場合はエイリアスなし
     * @param joined            結合先クラス
     * @param joinedProperty   結合対応プロパティ null、空白の場合はjoinedと直接結合する
     * @param alias             結合先エイリアス null、空白の場合はエイリアスなし
     * @return 内部結合オブジェクト
     */
    public static InnerJoin innerJoin(Class target, String targetProperty, String targetAlias, Class joined, String joinedProperty, String joinedAlias) {
       return new InnerJoin(target, targetProperty, targetAlias, joined, joinedProperty, joinedAlias); 
    }
    
	/**
	 * IsNotNull条件オブジェクトを生成する
	 * @param propertyName プロパティ名
	 * @return				IsNotNull条件オブジェクト
	 */
	public static LabelHoldingCondition isNotNull(String propertyName) {
		return new IsNotNull(propertyName, null, null);
	}

    /**
     * IsNotNull条件オブジェクトを生成する
     * @param propertyName プロパティ名
     * @param target    プロパティを保持しているエンティティクラス
     * @param aliase    エンティティの別名
     * @return              IsNotNull条件オブジェクト
     */
    public static LabelHoldingCondition isNotNull(String propertyName, Class target, String aliase) {
        return new IsNotNull(propertyName, target, aliase);
    }

	/**
	 * IsNull条件オブジェクトを生成する
	 * @param propertyName プロパティ名
	 * @return				IsNull条件オブジェクト
	 */
	public static LabelHoldingCondition isNull(String propertyName) {
		return new IsNull(propertyName, null, null);
	}

    /**
     * IsNull条件オブジェクトを生成する
     * @param propertyName プロパティ名
     * @param target    プロパティを保持しているエンティティクラス
     * @param aliase    エンティティの別名
     * @return              IsNull条件オブジェクト
     */
    public static LabelHoldingCondition isNull(String propertyName, Class target, String aliase) {
        return new IsNull(propertyName, target, aliase);
    }

	/**
	 * 外部結合条件オブジェクトを生成する
	 * @param entityName 	外部エンティティ名
	 * @return				外部条件結合オブジェクト
	 */
	public static Join join(String entityName) {
		return new Join(entityName);
	}

	/**
	 * Le条件オブジェクトを生成する
	 * @param propertyName プロパティ名
     * @param target    プロパティを保持しているエンティティクラス
     * @param aliase    エンティティの別名
	 * @param value		値
	 * @return				Le条件オブジェクト
	 */
	public static ValueHoldingCondition le(String propertyName, Object value) {
		return new Le(propertyName, null, null, value);
	}

    /**
     * Le条件オブジェクトを生成する
     * @param propertyName プロパティ名
     * @param value     値
     * @return              Le条件オブジェクト
     */
    public static ValueHoldingCondition le(String propertyName, Class target, String aliase, Object value) {
        return new Le(propertyName, target, aliase, value);
    }

	/**
	 * Like条件オブジェクトを生成する
	 * @param propertyName プロパティ名
	 * @param value		値
	 * @return				Like条件オブジェクト
	 */
	public static ValueHoldingCondition like(String propertyName, Object value) {
		return new Like(propertyName, null, null, value);
	}

    /**
     * Like条件オブジェクトを生成する
     * @param propertyName プロパティ名
     * @param target    プロパティを保持しているエンティティクラス
     * @param aliase    エンティティの別名
     * @param value     値
     * @return              Like条件オブジェクト
     */
    public static ValueHoldingCondition like(String propertyName, Class target, String aliase, Object value) {
        return new Like(propertyName, target, aliase, value);
    }

	/**
	 * Lt条件オブジェクトを生成する
	 * @param propertyName プロパティ名
	 * @param value		値
	 * @return				Lt条件オブジェクト
	 */
	public static ValueHoldingCondition lt(String propertyName, Object value) {
		return new Lt(propertyName, null, null, value);
	}
	
    /**
     * Lt条件オブジェクトを生成する
     * @param propertyName プロパティ名
     * @param target    プロパティを保持しているエンティティクラス
     * @param aliase    エンティティの別名
     * @param value     値
     * @return              Lt条件オブジェクト
     */
    public static ValueHoldingCondition lt(String propertyName, Class target, String aliase, Object value) {
        return new Lt(propertyName, target, aliase, value);
    }
    
    /**
     * NotEq条件オブジェクトを生成する
     * @param propertyName プロパティ名
     * @param value     値
     * @return              NotEq条件オブジェクト
     */
    public static ValueHoldingCondition notEq(String propertyName, Object value) {
        return new NotEq(propertyName, null, null, value);
    }

	/**
	 * NotEq条件オブジェクトを生成する
	 * @param propertyName プロパティ名
     * @param target    プロパティを保持しているエンティティクラス
     * @param aliase    エンティティの別名
	 * @param value		値
	 * @return				NotEq条件オブジェクト
	 */
	public static ValueHoldingCondition notEq(String propertyName, Class target, String aliase, Object value) {
		return new NotEq(propertyName, target, aliase, value);
	}

	/**
	 * Or条件オブジェクトを生成する
	 * @return 			Or条件オブジェクト
	 */
	public static CombineCondition or() {
		return new Or();
	}

	/**
	 * Or条件オブジェクトを生成する
	 * @param conditions	Or結合する条件群
	 * @return 			Or条件オブジェクト
	 */
    public static CombineCondition or(Collection<Condition> conditions) {
		return new Or(conditions);
	}
	
	/**
	 * Or条件オブジェクトを生成する
	 * @param conditions	Or結合する条件群
	 * @return 			Or条件オブジェクト
	 */
	public static CombineCondition or(Condition... conditions) {
		return or(Arrays.asList(conditions));
	}

    /**
     * RegEx条件オブジェクトを生成する
     * @param propertyName プロパティ名
     * @param target    プロパティを保持しているエンティティクラス
     * @param aliase    エンティティの別名
     * @param value     値
     * @return              RegExp条件オブジェクト
     */
    public static ValueHoldingCondition regex(String propertyName, Object value) {
        return regex(propertyName, null, null, value);
    }

    /**
     * RegEx条件オブジェクトを生成する
     * @param propertyName プロパティ名
     * @param target    プロパティを保持しているエンティティクラス
     * @param aliase    エンティティの別名
     * @param value     値
     * @return              RegExp条件オブジェクト
     */
    public static ValueHoldingCondition regex(String propertyName, Class target, String aliase, Object value) {
        return new RegularExp(propertyName, target, aliase, value);
    }
}
