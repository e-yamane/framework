package jp.rough_diamond.commons.service;

import java.math.BigDecimal;
import java.util.Date;

import jp.rough_diamond.commons.entity.Amount;
import jp.rough_diamond.commons.entity.Unit;

/**
 * �P�ʕϊ��T�[�r�X�̃V���v������
 * @author e-yamane
 */
public class SimpleUnitConversionService extends UnitConversionService {
	/**
	 * �����q
	 * �ۂߌ덷�v�Z���[�h���ȗ����ꂽ�ꍇ�͎l�̌ܓ��iROUND_HALF_UP�j���s���܂�
	 */
	public SimpleUnitConversionService() {
		this("ROUND_HALF_UP");
	}
	
	/**
	 * �����q
	 * �w�肳�ꂽ������ɑΉ�����ۂߌ덷���[�h���f�t�H���g�Ƃ��܂��B
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
		//[���̐�] �~ [���̒P�ʂ̌W��] / [�ϊ���̒P�ʂ̌W��] 
		BigDecimal bd = srcAmount.getQuantity().multiply(
				srcAmount.getUnit().getRate()).divide(
						destUnit.getRate(), destUnit.getScale(), roundingMode);
		return new Amount(bd, destUnit);
	}
}
