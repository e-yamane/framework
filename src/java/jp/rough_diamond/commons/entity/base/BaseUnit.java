package jp.rough_diamond.commons.entity.base;




import  java.io.Serializable;


/**
 * 数量尺度のHibernateマッピングベースクラス
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
     * OIDを取得する
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
     * OIDを設定する
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
     * 数量尺度名
    **/ 
    private String name;
    public final static String NAME = "name";

    /**
     * 数量尺度名を取得する
     * @hibernate.property
     *    column="NAME"
     *    not-null="true"
     *    length="32"
     * @return 数量尺度名
    **/
    @jp.rough_diamond.commons.service.annotation.MaxLength(length=32, property="Unit.name")
    @jp.rough_diamond.commons.service.annotation.NotNull(property="Unit.name")
    public String getName() {
        return name;
    }

    /**
     * 数量尺度名を設定する
     * @param name  数量尺度名
    **/
    public void setName(String name) {
        this.name = name;
    }


    /**
     * 単位説明
    **/ 
    private String description;
    public final static String DESCRIPTION = "description";

    /**
     * 単位説明を取得する
     * @hibernate.property
     *    column="DESCRIPTION"
     *    not-null="false"
     *    length="64"
     * @return 単位説明
    **/
    @jp.rough_diamond.commons.service.annotation.MaxLength(length=64, property="Unit.description")
    public String getDescription() {
        return description;
    }

    /**
     * 単位説明を設定する
     * @param description  単位説明
    **/
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * 変換係数
    **/ 
    private jp.rough_diamond.commons.entity.ScalableNumber rate =  new jp.rough_diamond.commons.entity.ScalableNumber();

    public final static String RATE = "rate.";

    /**
     * 変換係数を取得する
     * @hibernate.component
     *    prefix="RATE_"
     * @return 変換係数
    **/
    @jp.rough_diamond.commons.service.annotation.NotNull(property="Unit.rate")
    @jp.rough_diamond.commons.service.annotation.NestedComponent(property="Unit.rate")
    public jp.rough_diamond.commons.entity.ScalableNumber getRate() {
        return rate;
    }

    /**
     * 変換係数を設定する
     * @param rate  変換係数
    **/
    public void setRate(jp.rough_diamond.commons.entity.ScalableNumber rate) {
        this.rate = rate;
    }

    /**
     * 変換時に保持する少数精度。負数を指定すると整数の切捨て判断する
    **/ 
    private Integer scale;
    public final static String SCALE = "scale";

    /**
     * 変換時に保持する少数精度。負数を指定すると整数の切捨て判断するを取得する
     * @hibernate.property
     *    column="SCALE"
     *    not-null="true"
     *    length="10"
     * @return 変換時に保持する少数精度。負数を指定すると整数の切捨て判断する
    **/
    @jp.rough_diamond.commons.service.annotation.NotNull(property="Unit.scale")
    public Integer getScale() {
        return scale;
    }

    /**
     * 変換時に保持する少数精度。負数を指定すると整数の切捨て判断するを設定する
     * @param scale  変換時に保持する少数精度。負数を指定すると整数の切捨て判断する
    **/
    public void setScale(Integer scale) {
        this.scale = scale;
    }


    /**
     * 楽観的ロッキングキー
    **/ 
    private Long version;
    public final static String VERSION = "version";

    /**
     * 楽観的ロッキングキーを取得する
     * @hibernate.version
     *    column="VERSION"
     * @return 楽観的ロッキングキー
    **/
    public Long getVersion() {
        return version;
    }

    /**
     * 楽観的ロッキングキーを設定する
     * @param version  楽観的ロッキングキー
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
