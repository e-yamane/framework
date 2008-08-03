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
package jp.rough_diamond.framework.web.struts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import jp.rough_diamond.framework.web.struts.multipart.MultipartProgressRequestWrapper;

public class UpdateProgressorForm extends BaseForm {
	private static final long serialVersionUID = 1L;

	public boolean isFinish() {
		return (getWrapper() == null);
	}

	public long getReadSize() throws Exception {
		return getWrapper().getReadSize();
	}

	public long getContentLength() {
		return getWrapper().getContentLength();
	}

	public String getPercent(int ratio) throws Exception {
		try {
			BigDecimal bd = getPercent2(ratio);
			if(ratio == 0) {
				return "" + bd.intValue();
			}
			StringBuilder builder = new StringBuilder("#.");
			for(int i = 0 ; i < ratio ; i++) {
				builder.append("0");
			}
			DecimalFormat df = new DecimalFormat(builder.toString());
			return df.format(bd.doubleValue());
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	BigDecimal getPercent2(int scale) throws Exception {
		BigDecimal bd = new BigDecimal(getReadSize() * 100L);
		return bd.divide(new BigDecimal(getContentLength()), scale, RoundingMode.HALF_UP);
	}

	private MultipartProgressRequestWrapper getWrapper() {
		return MultipartProgressRequestWrapper.getRequest(getWatchId());
	}

	private String watchId;
//	private Long	startTime;
//	private Long	currentTime;

	public String getWatchId() {
		return watchId;
	}

	public void setWatchId(String watchId) {
		this.watchId = watchId;
	}

//	public Long getStartTime() {
//		return startTime;
//	}
//
//	public void setStartTime(Long startTime) {
//		this.startTime = startTime;
//	}
}
