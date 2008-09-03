package jp.rough_diamond.framework.web.struts;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;

public class BaseFormTest extends TestCase {
	public void testSerialize() throws Exception {
		BaseFormExt form = new BaseFormExt();
		form.hoge = "xyz";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(form);
		oos.close();
		byte[] array = baos.toByteArray();
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(array));
		form = null;
		System.gc();
		form = (BaseFormExt)ois.readObject();
		assertNotNull("•œŒ³‚Å‚«‚Ä‚¢‚Ü‚¹‚ñB", form.errs);
		assertEquals("•œŒ³‚Å‚«‚Ä‚¢‚Ü‚¹‚ñB", "xyz", form.hoge);
	}
	
	public static class BaseFormExt extends BaseForm {
		private static final long serialVersionUID = 1L;
		String hoge;
	}
}
