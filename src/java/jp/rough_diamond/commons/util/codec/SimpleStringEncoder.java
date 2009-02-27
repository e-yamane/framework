package jp.rough_diamond.commons.util.codec;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * ê∂ê¨éqÇ≈ìnÇ≥ÇÍÇΩMapÇ…ìoò^Ç≥ÇÍÇΩï∂éöïœä∑ÇÇ®Ç±Ç»Ç§StringEncoder
 * MapÇ…ë∂ç›ÇµÇ»Ç¢èÍçáÇÕìnÇ≥ÇÍÇΩï∂éöÇï‘ãpÇ∑ÇÈ
 * @author e-yamane
 *
 */
public class SimpleStringEncoder implements StringEncoder {
	private final Map<String, String> encodeMap;
    private final int maxKeyLength;

	@SuppressWarnings("unchecked")
	public SimpleStringEncoder(Map<String, String> encodeMap) {
		Map<String, String> tmp = new HashMap<String, String>(encodeMap);
		this.encodeMap = Collections.unmodifiableMap(tmp);
        Iterator<String> iterator = encodeMap.keySet().iterator();
        int maxLength = 0;
        while(iterator.hasNext()) {
            String key = (String)iterator.next();
            maxLength = Math.max(maxLength, key.length());
        }
        this.maxKeyLength = maxLength;
	}

	@Override
	public String encode(String target) throws EncoderException {
        int index = 0;
        int length = target.length();
        StringBuilder buf = new StringBuilder();
        while(index < length) {
            boolean isTranslate = false;
            int firstCounter = (index + maxKeyLength > length) ? length - index : maxKeyLength;
            for(int i = firstCounter ; i > 0  ; i--) {
                String key = target.substring(index, index + i);
                if(encodeMap.containsKey(key)) {
                    buf.append(encodeMap.get(key));
                    index = index + i;
                    isTranslate = true;
                    break;
                }
            }
            if(!isTranslate) {
                buf.append(target.substring(index, index + 1));
                index++;
            }
        }
        return buf.toString();
	}

	@Override
	public Object encode(Object target) throws EncoderException {
		return encode(target.toString());
	}
}
