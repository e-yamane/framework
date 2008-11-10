package jp.rough_diamond.commons.entity;

import java.math.BigDecimal;
import java.math.BigInteger;

import jp.rough_diamond.commons.service.UnitConversionService;

/**
 * 量のHibernateマッピングクラス
**/
public class Amount extends jp.rough_diamond.commons.entity.base.BaseAmount {
    private static final long serialVersionUID = 1L;
    public Amount() { }
    public Amount(BigDecimal quantity, Unit unit) {
    	setQuantity(quantity);
    	setUnit(unit);
    }
    
    public void setQuantity(BigDecimal quantity) {
    	if(quantity == null) {
    		setValue(null);
    		setScale(null);
    	} else {
	    	setValue(quantity.unscaledValue().longValue());
	    	setScale(quantity.scale());
    	}
    }
    
    public BigDecimal getQuantity() {
    	Long value = getValue();
    	Integer scale = getScale();
    	if(value == null || scale == null) {
    		return null;
    	} else {
    		return new BigDecimal(new BigInteger(getValue().toString()), getScale());
    	}
    }
    
    /**
     * UnitConversionServiceへのラッパーメソッド
     * @param destUnit
     * @return
     * @throws UnitConversionService.NotConversionException
     */
    public Amount convertUnit(Unit destUnit) throws UnitConversionService.NotConversionException{
    	return UnitConversionService.getService().convertUnit(this, destUnit);
    }

    /**
     * UnitConversionServiceへのラッパーメソッド
     * @param destUnit
     * @param roundingMode
     * @return
     * @throws UnitConversionService.NotConversionException
     */
    public Amount convertUnit(Unit destUnit, int roundingMode) throws UnitConversionService.NotConversionException{
    	return UnitConversionService.getService().convertUnit(this, destUnit, roundingMode);
    }
}
