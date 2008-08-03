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
package jp.rough_diamond.framework.transaction;

import java.sql.SQLException;

public class VersionUnmuchException extends SQLException {
	public final static long serialVersionUID = -1L;
	
	public VersionUnmuchException(String reason, String SQLState, int vendorCode) {
		super(reason, SQLState, vendorCode);
	}

	public VersionUnmuchException(String reason, String SQLState) {
		super(reason, SQLState);
	}

	public VersionUnmuchException(String reason) {
		super(reason);
	}

	public VersionUnmuchException() {
		super();
	}

}
