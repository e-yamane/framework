package jp.rough_diamond.commons.entity.base;



import  java.io.Serializable;


/**
 * 量のHibernateマッピングベースクラス
**/

public abstract class BaseAmount  implements Serializable {
    public BaseAmount() {
    }


    /**
     * 量(整数)
    **/ 
    private Long value;
    public final static String VALUE = "value";

    /**
     * 量(整数)を取得する
     * @hibernate.property
     *    column="VALUE"
     *    not-null="true"
     * @return 量(整数)
    **/
    @jp.rough_diamond.commons.service.annotation.NotNull(property="Amount.value")
    public Long getValue() {
        return value;
    }

    /**
     * 量(整数)を設定する
     * @param value  量(整数)
    **/
    public void setValue(Long value) {
        this.value = value;
    }


    /**
     * 小数点位置。正の数なら左へ、負の数なら右へ移動させる
    **/ 
    private Integer scale;
    public final static String SCALE = "scale";

    /**
     * 小数点位置。正の数なら左へ、負の数なら右へ移動させるを取得する
     * @hibernate.property
     *    column="SCALE"
     *    not-null="true"
     * @return 小数点位置。正の数なら左へ、負の数なら右へ移動させる
    **/
    @jp.rough_diamond.commons.service.annotation.NotNull(property="Amount.scale")
    public Integer getScale() {
        return scale;
    }

    /**
     * 小数点位置。正の数なら左へ、負の数なら右へ移動させるを設定する
     * @param scale  小数点位置。正の数なら左へ、負の数なら右へ移動させる
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