/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.extractor;

import java.util.Collection;

/**
 * And������\��Condition
 * @author $Author: Yamane_Eiji@bp.ogis-ri.co.jp $
 * @Date $Date: 2006-02-08 10:33:45 +0900 (水, 08 2 2006) $
 */
public class Or extends CombineCondition {
	private static final long serialVersionUID = 1L;

	/**
	 * Or�I�u�W�F�N�g�𐶐�����
	 * ��������钆�g�͊k�Ƃ���
	 */
	public Or() {
		super();
	}
	
	/**
	 * Or�I�u�W�F�N�g�𐶐�����
	 * @param conditions	Condition�Q nulll�̏ꍇ��NullPointerException���X���[����
	 */
	public Or(Collection<Condition> conditions) {
		super(conditions);
	}
}
