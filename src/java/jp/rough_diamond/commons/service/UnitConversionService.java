package jp.rough_diamond.commons.service;

import java.util.Date;

import jp.rough_diamond.commons.entity.Amount;
import jp.rough_diamond.commons.entity.Unit;
import jp.rough_diamond.framework.service.Service;
import jp.rough_diamond.framework.service.ServiceLocator;

/**
 * 単位変換サービス
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
	 * 単位を変換する
	 * @param srcAmount					元となる量
	 * @param destUnit					変換する単位
	 * @param roundingMode				割り切れない場合の丸めモード
	 * @param d							変換レート適用日時
	 * @return							変換した単位の量
	 * @throws NotConversionException 	変換失敗
	 */
	abstract public Amount convertUnit(Amount srcAmount, Unit destUnit, int roundingMode, Date d) throws NotConversionException;

	/**
	 * 単位を変換する
	 * 変換レートは現時点とする
	 * @param srcAmount					元となる量
	 * @param destUnit					変換する単位
	 * @param roundingMode				割り切れない場合の丸めモード
	 * @return							変換した単位の量
	 * @throws NotConversionException 	変換失敗
	 */
	public Amount convertUnit(Amount srcAmount, Unit destUnit, int roundingMode) throws NotConversionException {
		return convertUnit(srcAmount, destUnit, roundingMode, new Date());
	}

	/**
	 * 単位を変換する
	 * 丸めモードは実装サービスのデフォルト値とする
	 * 変換レートは現時点とする
	 * @param srcAmount	元となる量
	 * @param destUnit	変換する単位
	 * @param d			変換レート適用日時
	 * @return			変換した単位の量
	 * @throws NotConversionException 変換失敗
	 */
	abstract public Amount convertUnit(Amount srcAmount, Unit destUnit, Date d) throws NotConversionException;
	
	/**
	 * 単位を変換する
	 * 丸めモードは実装サービスのデフォルト値とする
	 * @param srcAmount	元となる量
	 * @param destUnit	変換する単位
	 * @return			変換した単位の量
	 * @throws NotConversionException 変換失敗
	 */
	public Amount convertUnit(Amount srcAmount, Unit destUnit) throws NotConversionException {
		return convertUnit(srcAmount, destUnit, new Date());
	}

	/**
	 * 単位変換エラー
	 * @author e-yamane
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
