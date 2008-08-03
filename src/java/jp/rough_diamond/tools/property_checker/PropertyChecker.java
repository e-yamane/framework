package jp.rough_diamond.tools.property_checker;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * �v���p�e�B�t�@�C���̑Ó������؃c�[��
 * @author TISK33645
 */
public class PropertyChecker {
    private Properties baseProperty;
    private Properties targetProperties;
    public PropertyChecker(
            String basePropertyFileName, String targetPropertyFileName) 
                    throws IOException {
        baseProperty = new Properties();
        baseProperty.load(new FileInputStream(basePropertyFileName));
        targetProperties = new Properties();
        targetProperties.load(new FileInputStream(targetPropertyFileName));
    }

    /**
     * �`�F�b�N�J�n
     * @return true�̏ꍇ�͊��S��v���Ă���
     */
    public boolean doIt() {
        return PropertyChecker.doIt(baseProperty, targetProperties);
    }

    /**
     * �`�F�b�N�J�n
     * @return true�̏ꍇ�͊��S��v���Ă���
     */
    @SuppressWarnings("unchecked")
    public static boolean doIt(Properties baseProperty, Properties targetProperties) {
        System.out.println("�v���p�e�B�̃`�F�b�N���J�n���܂��B");
        Enumeration<String> keys = (Enumeration<String>) baseProperty.propertyNames();
        boolean ret = true;
        while(keys.hasMoreElements()) {
            String key = keys.nextElement();
            if(targetProperties.getProperty(key) == null) {
                System.out.println(key + "�����݂��Ă��܂���");
                ret = false;
            }
            targetProperties.remove(key);
        }
        keys = (Enumeration<String>)targetProperties.propertyNames();
        while(keys.hasMoreElements()) {
            ret = false;
            String key = keys.nextElement();
            System.out.println(key + "�͌��ؗp�v���p�e�B�ɑ��݂��Ă��܂���B");
        }
        return ret;
    }

    public static void main(String[] args) {
        try {
            if(new PropertyChecker(args[0], args[1]).doIt()) {
                System.exit(0);
            }
        } catch(Exception e) {
            e.printStackTrace(System.out);
        }
        System.exit(-1);
    }
}
