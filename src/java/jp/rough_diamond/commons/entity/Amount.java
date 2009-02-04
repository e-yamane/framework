package jp.rough_diamond.commons.entity;

import java.math.BigDecimal;
import java.util.Date;

import jp.rough_diamond.commons.service.UnitConversionService;

/**
 * 量のHibernateマッピングクラス
**/
public class Amount extends jp.rough_diamond.commons.entity.base.BaseAmount {
    private static final long serialVersionUID = 1L;
    public Amount() { }
    public Amount(BigDecimal bd, Unit unit) {
    	this(new ScalableNumber(bd), unit);
    }
    
    public Amount(ScalableNumber sn, Unit unit) {
    	setQuantity(sn);
    	setUnit(unit);
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
     * @param d
     * @return
     * @throws UnitConversionService.NotConversionException
     */
    public Amount convertUnit(Unit destUnit, Date d) throws UnitConversionService.NotConversionException{
    	return UnitConversionService.getService().convertUnit(this, destUnit, d);
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

    /**
     * UnitConversionServiceへのラッパーメソッド
     * @param destUnit
     * @param roundingMode
     * @param d
     * @return
     * @throws UnitConversionService.NotConversionException
     */
    public Amount convertUnit(Unit destUnit, int roundingMode, Date d) throws UnitConversionService.NotConversionException{
    	return UnitConversionService.getService().convertUnit(this, destUnit, roundingMode, d);
    }
    
	@Override
	public double doubleValue() {
		return (getQuantity() == null) ? 0.0D : getQuantity().doubleValue();
	}

	@Override
	public float floatValue() {
		return (getQuantity() == null) ? 0.0F : getQuantity().floatValue();
	}
	@Override
	public int intValue() {
		return (getQuantity() == null) ? 0 : getQuantity().intValue();
	}
	@Override
	public long longValue() {
		return (getQuantity() == null) ? 0L : getQuantity().longValue();
	}
	/**
	 * getQuantity().decimal()のショートカットメソッド
	 * getQuantity()==nullの場合はBigDecimal.ZEROを返却
	 * @return
	 */
	public BigDecimal decimal() {
		return (getQuantity() == null) ? BigDecimal.ZERO : getQuantity().decimal();
	}
}
