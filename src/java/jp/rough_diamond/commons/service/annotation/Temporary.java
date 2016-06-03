/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */

package jp.rough_diamond.commons.service.annotation;

/**
 * 一時データとして利用するエンティティに付与するマーカーインタフェース
 * トランザクションマネージャはトランザクション反映時に永続化しないようにしなければならない
 */
//TODO アノテーションじゃないけどマーカーだからこのパッケージいいかどうかはわかんない
public interface Temporary {

}
