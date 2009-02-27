package jp.rough_diamond.commons.util.codec;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.StringEncoder;

import junit.framework.TestCase;

public class SimpleStringEncoderTest extends TestCase {
	public void testIt() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("abc", "xyz");
		map.put("a", "qaz");
		StringEncoder encoder = new SimpleStringEncoder(map);
		//�����ƃR�s�[���Ă邱�Ƃ��m�F
		map.clear();
		assertEquals("����Ă��܂��B", "��qazxyz", encoder.encode("��aabc"));
	}
}