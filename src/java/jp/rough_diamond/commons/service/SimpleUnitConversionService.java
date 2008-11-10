package jp.rough_diamond.commons.service;

import java.math.BigDecimal;
import java.util.Date;

import jp.rough_diamond.commons.entity.Amount;
import jp.rough_diamond.commons.entity.Unit;

/**
 * 単位変換サービスのシンプル実装
 * @author e-yamane
 */
public class SimpleUnitConversionService extends UnitConversionService {
	/**
	 * 生成子
	 * 丸め誤差計算モードを省略された場合は四捨五入（ROUND_HALF_UP）を行います
	 */
	public SimpleUnitConversionService() {
		this("ROUND_HALF_UP");
	}
	
	/**
	 * 生成子
	 * 指定された文字列に対応する丸め誤差モードをデフォルトとします。
	 * @param mode
	 * @see java.math.BigDecimal
	 */
	public SimpleUnitConversionService(String mode) {
		this.defaultRoundingMode = getRoundingMode(mode);
	}
	
	static int getRoundingMode(String mode) {
		try {
			return BigDecimal.class.getField(mode).getInt(null);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	private final int defaultRoundingMode;
	
	@Override
	public Amount convertUnit(Amount srcAmount, Unit destUnit, Date d) {
		return convertUnit(srcAmount, destUnit, defaultRoundingMode, d);
	}

	@Override
	public Amount convertUnit(Amount srcAmount, Unit destUnit, int roundingMode, Date d) throws NotConversionException {
		if(srcAmount.getUnit() == null && destUnit != null) {
			throw new NotConversionException();
		}
		if(srcAmount.getUnit() == null && destUnit == null) {
			return new Amount(srcAmount.getQuantity(), null);
		}
		if(srcAmount.getUnit() != null && destUnit == null) {
			return new Amount(srcAmount.getQuantity(), null);
		}
		if(srcAmount.getUnit().equals(destUnit)) {
			return new Amount(srcAmount.getQuantity(), destUnit);
		}
		if(!srcAmount.getUnit().getBase().equals(destUnit.getBase())) {
			throw new NotConversionException();
		}
		//[元の数] × [元の単位の係数] / [変換後の単位の係数] 
		BigDecimal bd = srcAmount.getQuantity().multiply(
				srcAmount.getUnit().getRate()).divide(
						destUnit.getRate(), destUnit.getScale(), roundingMode);
		return new Amount(bd, destUnit);
	}
}
