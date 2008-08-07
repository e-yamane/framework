/*
 * 作成日: 2005/03/27
 *
 * この生成されたコメントの挿入されるテンプレートを変更するため
 * ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */
package jp.rough_diamond.commons.util.serializer;

import org.apache.commons.lang.StringUtils;

/**
 * @author e-yamane
 * 常に「"」でカラムを囲むフォーマッター
 */
public class DefaultColumnFormatter implements ColumnFormatter {
	public String getColumn(String column) {
        if(!StringUtils.containsNone(column, "\n\",")) {
            if(column.indexOf('\"') != -1) {
                column = CSVContainer.getSupplementQuote(column);
            }
            return "\"" +  column + "\"";
        } else {
            return column;
        }
	}
}
