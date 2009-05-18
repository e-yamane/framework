/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.extractor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * ���o�����i�[�I�u�W�F�N�g
 */
@SuppressWarnings("unchecked")
public class Extractor implements Serializable {
	private static final long serialVersionUID = 1L;

    public final static int		DEFAULT_FETCH_SIZE = -1;

    /**
	 * ���o�ΏۃG���e�B�e�B�N���X
	 */
	public final Class target;

    /**
     * ���o�ΏۃG�C���A�X
     */
    public final String targetAlias;
    
	private List<Order> 	     						orders = new ArrayList<Order>();
	private List<Condition<Property>> 					condition = new ArrayList<Condition<Property>>();
    private List<InnerJoin>     						innerJoins = new ArrayList<InnerJoin>();
    private List<ExtractValue>  						values = new ArrayList<ExtractValue>();
    private List<Condition<? extends SummaryFunction>>	having = new ArrayList<Condition<? extends SummaryFunction>>();
    
	private int 	offset = 0;
	private int		limit = -1;
	private int		fetchSize = DEFAULT_FETCH_SIZE;
	
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
	public List<Condition<Property>> getConditionIterator() {
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
	 * �t�F�b�`�T�C�Y���擾����
	 * @return	�t�F�b�`�T�C�Y
	 */
	public int getFetchSize() {
		return fetchSize;
	}

	/**
	 * �t�F�b�`�T�C�Y��ݒ肷��
	 * @param fetchSize
	 */
	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
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

	/**
	 * ���o����(having)��ǉ�����
	 * @param con	���o���� null�̏ꍇ��NullPointerException�𑗏o����
	 */
	public void addHaving(Condition<? extends SummaryFunction> con) {
		con.getClass();
		having.add(con);
	}
	
	/**
	 * �I�[�_�[������Iterator��ԋp����
	 * @return �I�[�_�[������Iterator
	 */
	public List<Condition<? extends SummaryFunction>> getHavingIterator() {
		return having;
	}
}
