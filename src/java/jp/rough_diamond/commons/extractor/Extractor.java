/*
 * ====================================================================
 * 
 *  Copyright 2007 Eiji Yamane(yamane@super-gs.jp)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 */
/**
 * 
 */
package jp.rough_diamond.commons.extractor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * ���o�����i�[�I�u�W�F�N�g
 * @author $Author: Yamane_Eiji@bp.ogis-ri.co.jp $
 * @date $Date: 2006-02-14 17:11:42 +0900 (火, 14 2 2006) $
 */
@SuppressWarnings("unchecked")
public class Extractor implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ���o�ΏۃG���e�B�e�B�N���X
	 */
	public final Class target;

    /**
     * ���o�ΏۃG�C���A�X
     */
    public final String targetAlias;
    
	private List<Order> 	     orders = new ArrayList<Order>();
	private List<Condition>     condition = new ArrayList<Condition>();
    private List<InnerJoin>     innerJoins = new ArrayList<InnerJoin>();
    private List<ExtractValue>  values = new ArrayList<ExtractValue>();
    
	private int 	offset = 0;
	private int	limit = -1;
	
	/**
	 * ���o�����i�[�I�u�W�F�N�g�𐶐�����
	 * @param target ���o�ΏۃG���e�B�e�B�N���X null�̏ꍇ��NullPointerException�𑗏o����
	 */
	public Extractor(Class target) {
        this(target, null);
	}

    /**
     * ���o�����i�[�I�u�W�F�N�g�𐶐�����
     * @param target
     * @param alias
     */
    public Extractor(Class target, String alias) {
        target.getClass();  //NOP NullPointerException�𑗏o������������
        this.target = target;
        this.targetAlias = alias;
    }
    
	/**
	 * ���o������ǉ�����
	 * @param con	���o���� null�̏ꍇ��NullPointerException�𑗏o����
	 */
	public void add(Condition con) {
		con.getClass();
		condition.add(con);
	}
	
	/**
	 * �\�[�g������ǉ�����
	 * @param order �\�[�g���� null�̏ꍇ��NullPointerException�𑗏o����
	 */
	public void addOrder(Order order) {
		order.getClass();
		orders.add(order);
	}
	
	/**
	 * �\�[�g������Iterator��ԋp����
	 * @return ���o������Iterator
	 */
	public List<Order> getOrderIterator() {
		return orders;
	}

	/**
	 * �I�[�_�[������Iterator��ԋp����
	 * @return �I�[�_�[������Iterator
	 */
	public List<Condition> getConditionIterator() {
		return condition;
	}

	/**
	 * ���o��������擾����
	 * @return ���o�����
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * ���o�������ݒ肷��
	 * @param limit	���o����� �O�ȉ��̏ꍇ�͒��o��������Ƃ���
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * ���o�J�n�ʒu���擾����
	 * @return ���o�J�n�ʒu
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * ���o�J�n�ʒu��ݒ肷��
	 * @param offset	���o�J�n�ʒu �O�ȉ��̏ꍇ�͐擪����擾����
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
    
    /**
     * ���������I�u�W�F�N�g��ǉ�����
     * @param join
     */
    public void addInnerJoin(InnerJoin join) {
        innerJoins.add(join);
    }
    
    /**
     * ���������I�u�W�F�N�g�Q��ԋp����
     */
    public List<InnerJoin> getInnerJoins() {
        return innerJoins;
    }
    
    /**
     * ���o�l��ǉ�����
     * @param value
     */
    public void addExtractValue(ExtractValue value) {
        values.add(value);
    }
    
    /**
     * ���o�l�Q��ԋp����
     */
    public List<ExtractValue> getValues() {
        return values;
    }
}
