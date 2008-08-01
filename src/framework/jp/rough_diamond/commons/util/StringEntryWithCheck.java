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
/*
 * $Id$
 * $Header$
 */
package jp.rough_diamond.commons.util;


public class StringEntryWithCheck extends ObjectWithCheck {
	private static final long serialVersionUID = 1L;

	//�f�V���A���C�Y�p
    public StringEntryWithCheck() { }

    public StringEntryWithCheck(
            StringEntry[] entries, StringEntry[] subEntries) {
        super(entries, subEntries, "getKey", "getName");
    }

    public StringEntryWithCheck(
            StringEntry[] entries, String selectedKey) {
        super(entries, selectedKey, "getKey", "getName");
    }
}
