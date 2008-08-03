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
package jp.rough_diamond.commons.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class Child {
    public static enum CascadeType {
        /**
         * 登録時：
         * （子から探して）親がいないとエラー
         * 更新時：
         * （子から探して）親がいないとエラー
         * （親から探して）子がいればエラー
         * 削除時：
         * （親から探して）親がいればエラー
         */
        RESTRICT,
        /**
         * 登録時：
         * （子から探して）親がいないとエラー
         * 更新時：
         * （子から探して）親がいないとエラー）
         * （親から探して）子がいれば値を合わせる
         * 削除時：
         * （親から探して）子が入ればnullをセット
         */
        UPDATE,
        ;
    }
    
    private String      entityName;
    private String[]    keys;
    private Parent      parent;
    private CascadeType cascade;

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String[] getKeys() {
        return Arrays.copyOf(keys, keys.length);
    }
    
    public void setKeys(String keys) {
        List<String> list = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(keys, ", ");
        while(tokenizer.hasMoreTokens()) {
            list.add(tokenizer.nextToken());
        }
        setKeys(list.toArray(new String[list.size()]));
    }
    
    public void setKeys(String[] keys) {
        this.keys = Arrays.copyOf(keys, keys.length);
    }

    public void setCascade(String type) {
        this.cascade = CascadeType.valueOf(type.toUpperCase());
    }

    public CascadeType getCascadeType() {
        return this.cascade;
    }
    
    public Parent getParent() {
        return parent;
    }
    
    void setParent(Parent parent) {
        this.parent = parent;
    }
}
