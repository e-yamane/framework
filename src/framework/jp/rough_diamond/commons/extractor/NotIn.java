package jp.rough_diamond.commons.extractor;

import java.util.Collection;

@SuppressWarnings("unchecked")
public class NotIn extends ValueHoldingCondition {
	private static final long serialVersionUID = 1L;

	/**
	 * NotIn(��܁jCondition�𐶐�����
	 * @param propertyName	�v���p�e�B�� null�̏ꍇ��NullPointerException�𑗏o����
     * @param target    �v���p�e�B��ێ����Ă���G���e�B�e�B�N���X
     * @param aliase    �G���e�B�e�B�̕ʖ�
	 * @param values		�l�Q null�̏ꍇ��NullPointerException�𑗏o����
	 */
	public NotIn(String propertyName, Class target, String aliase, Collection values) {
		super(propertyName, target, aliase, values);
	}
}
