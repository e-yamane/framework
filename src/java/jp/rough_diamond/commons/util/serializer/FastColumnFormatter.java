/*
 * 作成日: 2005/03/27
 *
 * この生成されたコメントの挿入されるテンプレートを変更するため
 * ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */
package jp.rough_diamond.commons.util.serializer;

/**
 * @author e-yamane
 * 常にエスケープを行なわず「"」で囲まない。（カラム内に「"」や改行が含まれている場合には使用しないこと）
 */
public class FastColumnFormatter implements ColumnFormatter {
	public String getColumn(String column) {
		return column;
	}
}
