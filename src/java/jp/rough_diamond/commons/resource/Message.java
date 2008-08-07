/*
 * ====================================================================
 *
 *  Copyright 2007 Eiji Yamane(yamane@super-gs.jp)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 */
/**
 *
 */
package jp.rough_diamond.commons.resource;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * メッセージ
 * @author $Author: Yamane_Eiji@bp.ogis-ri.co.jp $
 * @date $Date: 2006-03-10 14:59:54 +0900 (驥?, 10 3 2006) $
 */
public class Message  implements Serializable{
	private static final long serialVersionUID = 1L;
	public final String 	key;
	public final String[]	values;

	public Message(String key) {
		this.key = key;
		this.values = new String[0];
	}

	public Message(String key, String... values) {
		this.key = key;
		if(values == null) {
			this.values = new String[0];
		} else {
			this.values = new String[values.length];
			for(int i = 0 ; i < this.values.length ; i++) {
				this.values[i] = values[i];
			}
		}
	}

	public String getKey() {
		return key;
	}

    public String toString() {
        return MessageFormat.format(
                ResourceManager.getResource().getString(key), (Object[])values);
    }
}
