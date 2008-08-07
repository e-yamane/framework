package jp.rough_diamond.commons.util.serializer;

import  java.io.*;

import org.apache.commons.logging.*;

/**
 * $Id: CSVParser.java 445 2006-03-07 09:38:39Z Yamane_Eiji@bp.ogis-ri.co.jp $<br />
 * CSVフォーマットの文字列を二次元配列へ変換するパーザー。
 * @author $Author: Yamane_Eiji@bp.ogis-ri.co.jp $
**/
public final class CSVParser extends SVParser {
    public static Log log = LogFactory.getLog(CSVParser.class); 

    /**
     * 生成子
     * @param   text    パーズ対象文字列
     * @exception       IOException 読み込み失敗（あがりません）
    **/
    public CSVParser(String text) throws IOException {
        this(new StringReader(text));
    }

    /**
     * 生成子
     * @param   reader  文字列入力リーダー
     * @exception       IOException 読み込み失敗
    **/
    public CSVParser(Reader reader) throws IOException {
        super(reader, ",", "\"", "\n", " 　\t");
    }
}