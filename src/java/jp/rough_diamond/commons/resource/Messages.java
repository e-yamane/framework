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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * メッセージ集合
 * @author $Author: Yamane_Eiji@bp.ogis-ri.co.jp $
 * @date $Date: 2006-02-08 10:33:45 +0900 (豌ｴ, 08 2 2006) $
 */
public class Messages implements Serializable{
	private static final long serialVersionUID = 1L;
	private Map<String, List<Message>> messageMap = new LinkedHashMap<String, List<Message>>();

	public void add(Messages msgs) {
		Set<String> properties = msgs.getProperties();
		for(String property : properties) {
			List<Message> msgList = msgs.get(property);
			for(Message msg : msgList) {
				add(property, msg);
			}
		}
	}

	public void add(String property, Message msg) {
		List<Message> list = messageMap.get(property);
		if(list == null) {
			list = new ArrayList<Message>();
			messageMap.put(property, list);
		}
		list.add(msg);
	}

	public Set<String> getProperties() {
		return messageMap.keySet();
	}

	public List<Message> get(String property) {
		return messageMap.get(property);
	}

	public boolean hasError() {
		return getProperties().size() != 0;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(String property : getProperties()) {
			for(Message msg : get(property)) {
				builder.append(property);
				builder.append(":");
				builder.append(msg);
				builder.append('\n');
			}
		}
		return builder.toString();
	}
}
