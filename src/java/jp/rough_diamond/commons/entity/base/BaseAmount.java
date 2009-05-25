/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */

package jp.rough_diamond.commons.entity.base;




import  java.io.Serializable;


/**
 * �ʂ�Hibernate�}�b�s���O�x�[�X�N���X
**/
public abstract class BaseAmount extends java.lang.Number implements Serializable {
    public BaseAmount() {
    }



    /**
     * ��
    **/ 
    private jp.rough_diamond.commons.entity.ScalableNumber quantity =  new jp.rough_diamond.commons.entity.ScalableNumber();

    public final static String Q = "quantity.";

    /**
     * �ʂ��擾����
     * @hibernate.component
     *    prefix="Q_"
     * @return ��
    **/
    @jp.rough_diamond.commons.service.annotation.NotNull(property="Amount.quantity")
    @jp.rough_diamond.commons.service.annotation.NestedComponent(property="Amount.quantity")
    public jp.rough_diamond.commons.entity.ScalableNumber getQuantity() {
        return quantity;
    }

    /**
     * �ʂ�ݒ肷��
     * @param quantity  ��
    **/
    public void setQuantity(jp.rough_diamond.commons.entity.ScalableNumber quantity) {
        this.quantity = quantity;
    }


    
    private jp.rough_diamond.commons.entity.Unit unit;
    public final static String UNIT = "unit";

    /**
     * Get the associated Unit object
     * @hibernate.many-to-one
     *   outer-join = "true"
     * @hibernate.column name = "UNIT_ID"
     *
     * @return the associated Unit object
     */
    public jp.rough_diamond.commons.entity.Unit getUnit() {
        return this.unit;
    }

    /**
     * Declares an association between this object and a Unit object
     *
     * @param v Unit
     */
    public void setUnit(jp.rough_diamond.commons.entity.Unit v) {
        this.unit = v;
    }

    @jp.rough_diamond.commons.service.annotation.PostLoad
    public void loadUnit() {
        jp.rough_diamond.commons.entity.Unit unit = getUnit();
        if(unit != null) {
            Long pk = unit.getId();
            setUnit(
                    jp.rough_diamond.commons.service.BasicService.getService().findByPK(jp.rough_diamond.commons.entity.Unit.class, pk)
            );
        }
    }

    private static final long serialVersionUID = 1L;
}
