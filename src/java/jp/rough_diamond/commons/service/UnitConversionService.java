package jp.rough_diamond.commons.service;

import jp.rough_diamond.commons.entity.Amount;
import jp.rough_diamond.commons.entity.Unit;
import jp.rough_diamond.framework.service.Service;
import jp.rough_diamond.framework.service.ServiceLocator;

/**
 * �P�ʕϊ��T�[�r�X
 * @author e-yamane
 */
abstract public class UnitConversionService implements Service {
	public final static String DEFAULT_SERVICE_CLASS_NAME = 
				"jp.rough_diamond.commons.service.SimpleUnitConversionService";
	final static UnitConversionService INSTANCE;
	
	static {
		INSTANCE = ServiceLocator.getService(UnitConversionService.class, DEFAULT_SERVICE_CLASS_NAME);
	}
	
	public static UnitConversionService getService() {
		return INSTANCE;
	}

	/**
	 * �P�ʂ�ϊ�����
	 * @param srcAmount					���ƂȂ��
	 * @param destUnit					�ϊ�����P��
	 * @param roundingMode				����؂�Ȃ��ꍇ�̊ۂ߃��[�h
	 * @return							�ϊ������P�ʂ̗�
	 * @throws NotConversionException 	�ϊ����s
	 */
	abstract public Amount convertUnit(Amount srcAmount, Unit destUnit, int roundingMode) throws NotConversionException;

	/**
	 * �P�ʂ�ϊ�����
	 * �ۂ߃��[�h�͎����T�[�r�X�̃f�t�H���g�l�Ƃ���
	 * @param srcAmount	���ƂȂ��
	 * @param destUnit	�ϊ�����P��
	 * @return			�ϊ������P�ʂ̗�
	 * @throws NotConversionException �ϊ����s
	 */
	abstract public Amount convertUnit(Amount srcAmount, Unit destUnit) throws NotConversionException;

	/**
	 * �P�ʕϊ��G���[
	 * @author e-yamane
	 *
	 */
	public static class NotConversionException extends IllegalArgumentException {
		private static final long serialVersionUID = 1L;

		public NotConversionException() {
			super();
		}

		public NotConversionException(String message, Throwable cause) {
			super(message, cause);
		}

		public NotConversionException(String s) {
			super(s);
		}

		public NotConversionException(Throwable cause) {
			super(cause);
		}
	}
}
