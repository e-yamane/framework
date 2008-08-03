package jp.rough_diamond.tools.property_checker;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * プロパティファイルの妥当性検証ツール
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
     * チェック開始
     * @return trueの場合は完全一致している
     */
    public boolean doIt() {
        return PropertyChecker.doIt(baseProperty, targetProperties);
    }

    /**
     * チェック開始
     * @return trueの場合は完全一致している
     */
    @SuppressWarnings("unchecked")
    public static boolean doIt(Properties baseProperty, Properties targetProperties) {
        System.out.println("プロパティのチェックを開始します。");
        Enumeration<String> keys = (Enumeration<String>) baseProperty.propertyNames();
        boolean ret = true;
        while(keys.hasMoreElements()) {
            String key = keys.nextElement();
            if(targetProperties.getProperty(key) == null) {
                System.out.println(key + "が存在していません");
                ret = false;
            }
            targetProperties.remove(key);
        }
        keys = (Enumeration<String>)targetProperties.propertyNames();
        while(keys.hasMoreElements()) {
            ret = false;
            String key = keys.nextElement();
            System.out.println(key + "は検証用プロパティに存在していません。");
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
