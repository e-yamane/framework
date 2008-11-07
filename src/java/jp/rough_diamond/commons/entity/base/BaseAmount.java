package jp.rough_diamond.commons.entity.base;



import  java.io.Serializable;
import  java.math.BigDecimal;


/**
 * 量のHibernateマッピングベースクラス
**/

public abstract class BaseAmount  implements Serializable {
    public BaseAmount() {
    }


    /**
     * 量
    **/ 
    private BigDecimal quantity;
    public final static String QUANTITY = "quantity";

    /**
     * 量を取得する
     * @hibernate.property
     *    column="QUANTITY"
     *    not-null="true"
     * @return 量
    **/
    @jp.rough_diamond.commons.service.annotation.NotNull(property="Amount.quantity")
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     * 量を設定する
     * @param quantity  量
    **/
    public void setQuantity(BigDecimal quantity) {
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