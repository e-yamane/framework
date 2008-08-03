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
package jp.rough_diamond.commons.resource;

import java.util.List;

public class MessagesUtil {
    public static Messages translate(Messages msgs, String before, String after) {
        Messages ret = new Messages();
        for(String key : msgs.getProperties()) {
            String newKey = key.replaceFirst(before, after);
            List<Message> list = msgs.get(key);
            for(Message msg : list) {
                ret.add(newKey, new Message(msg.key, msg.values));
            }
        }
        return ret;
    }

    //Messagesを任意のプロパティに集約させる
    public static Messages concatProperty(Messages msgs, String property) {
        Messages ret = new Messages();
        for(String key : msgs.getProperties()) {
            for(Message msg : msgs.get(key)) {
                ret.add(property, msg);
            }
        }
        return ret;
    }
}
