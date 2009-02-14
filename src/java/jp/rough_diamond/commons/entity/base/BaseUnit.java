package jp.rough_diamond.commons.entity.base;




import  java.io.Serializable;


/**
 * ���ʎړx��Hibernate�}�b�s���O�x�[�X�N���X
 * @hibernate.class
 *    table="UNIT"
 *    realClass="jp.rough_diamond.commons.entity.Unit"
**/

public abstract class BaseUnit  implements Serializable {
    public BaseUnit() {
    }

    /**
     * OID
    **/ 
    private Long id;

    public final static String ID = "id";
    /**
     * OID���擾����
     * @hibernate.id
     *    generator-class="assigned"
     *    column="ID"
     *    not-null="true"
     *    length="20"
     * @return OID
    **/

    public Long getId() {
        return id;
    }

    /**
     * OID��ݒ肷��
     * @param id  OID
    **/
    public void setId(Long id) {
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
        if(o instanceof BaseUnit) {
            if(hashCode() == o.hashCode()) {
                BaseUnit obj = (BaseUnit)o;
                if(getId() == null) {
                    return super.equals(o);
                }
                return getId().equals(obj.getId());
            }
        }
        return false;
    }

    /**
     * ���ʎړx��
    **/ 
    private String name;
    public final static String NAME = "name";

    /**
     * ���ʎړx�����擾����
     * @hibernate.property
     *    column="NAME"
     *    not-null="true"
     *    length="32"
     * @return ���ʎړx��
    **/
    @jp.rough_diamond.commons.service.annotation.MaxLength(length=32, property="Unit.name")
    @jp.rough_diamond.commons.service.annotation.NotNull(property="Unit.name")
    public String getName() {
        return name;
    }

    /**
     * ���ʎړx����ݒ肷��
     * @param name  ���ʎړx��
    **/
    public void setName(String name) {
        this.name = name;
    }


    /**
     * �P�ʐ���
    **/ 
    private String description;
    public final static String DESCRIPTION = "description";

    /**
     * �P�ʐ������擾����
     * @hibernate.property
     *    column="DESCRIPTION"
     *    not-null="false"
     *    length="64"
     * @return �P�ʐ���
    **/
    @jp.rough_diamond.commons.service.annotation.MaxLength(length=64, property="Unit.description")
    public String getDescription() {
        return description;
    }

    /**
     * �P�ʐ�����ݒ肷��
     * @param description  �P�ʐ���
    **/
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * �ϊ��W��
    **/ 
    private jp.rough_diamond.commons.entity.ScalableNumber rate =  new jp.rough_diamond.commons.entity.ScalableNumber();

    public final static String RATE = "rate.";

    /**
     * �ϊ��W�����擾����
     * @hibernate.component
     *    prefix="RATE_"
     * @return �ϊ��W��
    **/
    @jp.rough_diamond.commons.service.annotation.NotNull(property="Unit.rate")
    @jp.rough_diamond.commons.service.annotation.NestedComponent(property="Unit.rate")
    public jp.rough_diamond.commons.entity.ScalableNumber getRate() {
        return rate;
    }

    /**
     * �ϊ��W����ݒ肷��
     * @param rate  �ϊ��W��
    **/
    public void setRate(jp.rough_diamond.commons.entity.ScalableNumber rate) {
        this.rate = rate;
    }

    /**
     * �ϊ����ɕێ����鏭�����x�B�������w�肷��Ɛ����̐؎̂Ĕ��f����
    **/ 
    private Integer scale;
    public final static String SCALE = "scale";

    /**
     * �ϊ����ɕێ����鏭�����x�B�������w�肷��Ɛ����̐؎̂Ĕ��f������擾����
     * @hibernate.property
     *    column="SCALE"
     *    not-null="true"
     *    length="10"
     * @return �ϊ����ɕێ����鏭�����x�B�������w�肷��Ɛ����̐؎̂Ĕ��f����
    **/
    @jp.rough_diamond.commons.service.annotation.NotNull(property="Unit.scale")
    public Integer getScale() {
        return scale;
    }

    /**
     * �ϊ����ɕێ����鏭�����x�B�������w�肷��Ɛ����̐؎̂Ĕ��f�����ݒ肷��
     * @param scale  �ϊ����ɕێ����鏭�����x�B�������w�肷��Ɛ����̐؎̂Ĕ��f����
    **/
    public void setScale(Integer scale) {
        this.scale = scale;
    }


    /**
     * �y�ϓI���b�L���O�L�[
    **/ 
    private Long version;
    public final static String VERSION = "version";

    /**
     * �y�ϓI���b�L���O�L�[���擾����
     * @hibernate.version
     *    column="VERSION"
     * @return �y�ϓI���b�L���O�L�[
    **/
    public Long getVersion() {
        return version;
    }

    /**
     * �y�ϓI���b�L���O�L�[��ݒ肷��
     * @param version  �y�ϓI���b�L���O�L�[
    **/
    public void setVersion(Long version) {
        this.version = version;
    }



    
    private jp.rough_diamond.commons.entity.Unit base;
    public final static String BASE = "base";

    /**
     * Get the associated Unit object
     * @hibernate.many-to-one
     *   outer-join = "true"
     * @hibernate.column name = "BASE_UNIT_ID"
     *
     * @return the associated Unit object
     */
    @jp.rough_diamond.commons.service.annotation.NotNull(property="Unit.baseUnitId")
    public jp.rough_diamond.commons.entity.Unit getBase() {
        return this.base;
    }

    /**
     * Declares an association between this object and a Unit object
     *
     * @param v Unit
     */
    public void setBase(jp.rough_diamond.commons.entity.Unit v) {
        this.base = v;
    }

    @jp.rough_diamond.commons.service.annotation.PostLoad
    public void loadBases() {
        jp.rough_diamond.commons.entity.Unit base = getBase();
        if(base != null) {
            jp.rough_diamond.commons.entity.Unit tmp = base.getBase();
            if(tmp != null) {
                Long pk = tmp.getId();
                base.setBase(
                        jp.rough_diamond.commons.service.BasicService.getService().findByPK(jp.rough_diamond.commons.entity.Unit.class, pk));
            }
        }
    }

    private static final long serialVersionUID = 1L;
}
