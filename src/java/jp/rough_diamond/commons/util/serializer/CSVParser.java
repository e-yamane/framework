package jp.rough_diamond.commons.util.serializer;

import  java.io.*;

import org.apache.commons.logging.*;

/**
 * $Id: CSVParser.java 445 2006-03-07 09:38:39Z Yamane_Eiji@bp.ogis-ri.co.jp $<br />
 * CSV�t�H�[�}�b�g�̕������񎟌��z��֕ϊ�����p�[�U�[�B
 * @author $Author: Yamane_Eiji@bp.ogis-ri.co.jp $
**/
public final class CSVParser extends SVParser {
    public static Log log = LogFactory.getLog(CSVParser.class); 

    /**
     * �����q
     * @param   text    �p�[�Y�Ώە�����
     * @exception       IOException �ǂݍ��ݎ��s�i������܂���j
    **/
    public CSVParser(String text) throws IOException {
        this(new StringReader(text));
    }

    /**
     * �����q
     * @param   reader  ��������̓��[�_�[
     * @exception       IOException �ǂݍ��ݎ��s
    **/
    public CSVParser(Reader reader) throws IOException {
        super(reader, ",", "\"", "\n", " �@\t");
    }
}