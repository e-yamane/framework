/**
 * 
 */
package jp.rough_diamond.commons.testing;

import junit.framework.TestCase;

/**
 * DBUnitを利用してデータをローディングしているテストケース
 * @author e-yamane
 */
public abstract class DataLoadingTestCase extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DBInitializer.clearModifiedClasses();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        DBInitializer.clearModifiedData();
    }
}
