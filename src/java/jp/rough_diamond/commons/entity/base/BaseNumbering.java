package jp.rough_diamond.commons.entity.base;




import  java.io.Serializable;


/**
 * ƒiƒ“ƒoƒŠƒ“ƒOƒe[ƒuƒ‹‚ÌHibernateƒ}ƒbƒsƒ“ƒOƒx[ƒXƒNƒ‰ƒX
 * @hibernate.class
 *    table="NUMBERING"
 *    realClass="jp.rough_diamond.commons.entity.Numbering"
**/

public abstract class BaseNumbering  implements Serializable {
    public BaseNumbering() {
    }

    /**
     * ‚h‚c
    **/ 
    private String id;

    public final static String ID = "id";
    /**
     * ‚h‚c‚ğæ“¾‚·‚é
     * @hibernate.id
     *    generator-class="assigned"
     *    column="ID"
     *    not-null="true"
     *    length="128"
     * @return ‚h‚c
    **/

    public String getId() {
        return id;
    }

    /**
     * ‚h‚c‚ğİ’è‚·‚é
     * @param id  ‚h‚c
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
     * Œ»İŠ„‚è“–‚Ä‚Ä‚¢‚é”Ô†
    **/ 
    private Long nextNumber;
    public final static String NEXT_NUMBER = "nextNumber";

    /**
     * Œ»İŠ„‚è“–‚Ä‚Ä‚¢‚é”Ô†‚ğæ“¾‚·‚é
     * @hibernate.property
     *    column="NEXT_NUMBER"
     *    not-null="true"
     *    length="20"
     * @return Œ»İŠ„‚è“–‚Ä‚Ä‚¢‚é”Ô†
    **/
    @jp.rough_diamond.commons.service.annotation.NotNull(property="Numbering.nextNumber")
    public Long getNextNumber() {
        return nextNumber;
    }

    /**
     * Œ»İŠ„‚è“–‚Ä‚Ä‚¢‚é”Ô†‚ğİ’è‚·‚é
     * @param nextNumber  Œ»İŠ„‚è“–‚Ä‚Ä‚¢‚é”Ô†
    **/
    public void setNextNumber(Long nextNumber) {
        this.nextNumber = nextNumber;
    }



    
    private static final long serialVersionUID = 1L;
}
