package jp.rough_diamond.commons.entity.base;




import  java.io.Serializable;


/**
 * �i���o�����O�e�[�u����Hibernate�}�b�s���O�x�[�X�N���X
 * @hibernate.class
 *    table="NUMBERING"
 *    realClass="jp.rough_diamond.commons.entity.Numbering"
**/

public abstract class BaseNumbering  implements Serializable {
    public BaseNumbering() {
    }

    /**
     * �h�c
    **/ 
    private String id;

    public final static String ID = "id";
    /**
     * �h�c���擾����
     * @hibernate.id
     *    generator-class="assigned"
     *    column="ID"
     *    not-null="true"
     *    length="128"
     * @return �h�c
    **/

    public String getId() {
        return id;
    }

    /**
     * �h�c��ݒ肷��
     * @param id  �h�c
    **/
    public void setId(String id) {
        this.id = id;
    }
    
    public int hashCode() {
        if(getId() == null) {
            return super.hashCode();
        } else {
            return getId().hashCode();
        }
    }
    
    public boolean equals(Object o) {
        if(o instanceof BaseNumbering) {
            if(hashCode() == o.hashCode()) {
                BaseNumbering obj = (BaseNumbering)o;
                if(getId() == null) {
                    return super.equals(o);
                }
                return getId().equals(obj.getId());
            }
        }
        return false;
    }

    /**
     * ���݊��蓖�ĂĂ���ԍ�
    **/ 
    private Long nextNumber;
    public final static String NEXT_NUMBER = "nextNumber";

    /**
     * ���݊��蓖�ĂĂ���ԍ����擾����
     * @hibernate.property
     *    column="NEXT_NUMBER"
     *    not-null="true"
     *    length="20"
     * @return ���݊��蓖�ĂĂ���ԍ�
    **/
    @jp.rough_diamond.commons.service.annotation.NotNull(property="Numbering.nextNumber")
    public Long getNextNumber() {
        return nextNumber;
    }

    /**
     * ���݊��蓖�ĂĂ���ԍ���ݒ肷��
     * @param nextNumber  ���݊��蓖�ĂĂ���ԍ�
    **/
    public void setNextNumber(Long nextNumber) {
        this.nextNumber = nextNumber;
    }



    
    private static final long serialVersionUID = 1L;
}
