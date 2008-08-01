package jp.rough_diamond.commons.extractor;

import java.util.Collection;

@SuppressWarnings("unchecked")
public class NotIn extends ValueHoldingCondition {
	private static final long serialVersionUID = 1L;

	/**
	 * NotIn(包含）Conditionを生成する
	 * @param propertyName	プロパティ名 nullの場合はNullPointerExceptionを送出する
     * @param target    プロパティを保持しているエンティティクラス
     * @param aliase    エンティティの別名
	 * @param values		値群 nullの場合はNullPointerExceptionを送出する
	 */
	public NotIn(String propertyName, Class target, String aliase, Collection values) {
		super(propertyName, target, aliase, values);
	}
}
