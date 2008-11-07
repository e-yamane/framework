package jp.rough_diamond.commons.testdata;

import jp.rough_diamond.commons.testing.DBInitializer;
import jp.rough_diamond.framework.service.ServiceLocator;

public class UnitLoader extends DBInitializer {
	/**
	 * �C���X�^���X
	 */
	public final static DBInitializer INSTANCE = ServiceLocator.getService(UnitLoader.class);

	/**
	 * ��������
	 * @throws Exception
	 */
    public static void init() throws Exception {
        Resetter.reset();
        INSTANCE.load();
    }

    /**
     * ���[�h����t�@�C�����Q
     */
    final static String[] NAMES = new String[]{
        "jp/rough_diamond/commons/testdata/UNIT.xls",
    };

    public static String[] getNames() {
    	return NAMES;
    }
    @Override
	protected String[] getResourceNames() {
		return getNames();
	}
}
