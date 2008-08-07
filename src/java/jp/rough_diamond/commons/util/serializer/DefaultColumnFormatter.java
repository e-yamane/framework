/*
 * �쐬��: 2005/03/27
 *
 * ���̐������ꂽ�R�����g�̑}�������e���v���[�g��ύX���邽��
 * �E�B���h�E > �ݒ� > Java > �R�[�h���� > �R�[�h�ƃR�����g
 */
package jp.rough_diamond.commons.util.serializer;

import org.apache.commons.lang.StringUtils;

/**
 * @author e-yamane
 * ��Ɂu"�v�ŃJ�������͂ރt�H�[�}�b�^�[
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
