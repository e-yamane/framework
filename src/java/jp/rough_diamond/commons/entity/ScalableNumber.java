package jp.rough_diamond.commons.entity;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 小数点位置指定数値のHibernateマッピングクラス
**/
public class ScalableNumber extends jp.rough_diamond.commons.entity.base.BaseScalableNumber {
    private static final long serialVersionUID = 1L;

    public ScalableNumber() {
    	this(0L, 0);
    }
    
    public ScalableNumber(long value, int scale) {
    	setValue(value);
    	setScale(scale);
    }

    public ScalableNumber(String val) {
    	this(new BigDecimal(val));
    }
    
    public ScalableNumber(BigDecimal bd) {
    	setDecimal(bd);
    }
    
	@Override
	public double doubleValue() {
		return decimal().doubleValue();
	}

	@Override
	public float floatValue() {
		return decimal().floatValue();
	}

	@Override
	public int intValue() {
		return decimal().intValue();
	}

	@Override
	public long longValue() {
		return decimal().longValue();
	}
	
	public void setDecimal(BigDecimal bd) {
    	if(bd == null) {
    		setValue(null);
    		setScale(null);
    	} else {
	    	setValue(bd.unscaledValue().longValue());
	    	setScale(bd.scale());
    	}
	}
	
	public BigDecimal decimal() {
		Long value = getValue();
		Integer scale = getScale();
		if(value == null || scale == null) {
			return new BigDecimal("0");
		} else {
			return new BigDecimal(new BigInteger(value.toString()), scale);
		}
	}
}
