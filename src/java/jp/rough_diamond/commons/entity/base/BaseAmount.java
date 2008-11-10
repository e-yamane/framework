package jp.rough_diamond.commons.entity.base;



import  java.io.Serializable;


/**
 * �ʂ�Hibernate�}�b�s���O�x�[�X�N���X
**/

public abstract class BaseAmount  implements Serializable {
    public BaseAmount() {
    }


    /**
     * ��(����)
    **/ 
    private Long value;
    public final static String VALUE = "value";

    /**
     * ��(����)���擾����
     * @hibernate.property
     *    column="VALUE"
     *    not-null="true"
     * @return ��(����)
    **/
    @jp.rough_diamond.commons.service.annotation.NotNull(property="Amount.value")
    public Long getValue() {
        return value;
    }

    /**
     * ��(����)��ݒ肷��
     * @param value  ��(����)
    **/
    public void setValue(Long value) {
        this.value = value;
    }


    /**
     * �����_�ʒu�B���̐��Ȃ獶�ցA���̐��Ȃ�E�ֈړ�������
    **/ 
    private Integer scale;
    public final static String SCALE = "scale";

    /**
     * �����_�ʒu�B���̐��Ȃ獶�ցA���̐��Ȃ�E�ֈړ���������擾����
     * @hibernate.property
     *    column="SCALE"
     *    not-null="true"
     * @return �����_�ʒu�B���̐��Ȃ獶�ցA���̐��Ȃ�E�ֈړ�������
    **/
    @jp.rough_diamond.commons.service.annotation.NotNull(property="Amount.scale")
    public Integer getScale() {
        return scale;
    }

    /**
     * �����_�ʒu�B���̐��Ȃ獶�ցA���̐��Ȃ�E�ֈړ��������ݒ肷��
     * @param scale  �����_�ʒu�B���̐��Ȃ獶�ցA���̐��Ȃ�E�ֈړ�������
    **/
    public void setScale(Integer scale) {
        this.scale = scale;
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