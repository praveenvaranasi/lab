package com.fiorano.esb.junit.rmi.serialize;

import junit.framework.Assert;

import java.io.File;

import com.fiorano.stf.test.core.TestCaseElement;
import com.fiorano.stf.test.core.RMITestCase;

/**
 * Created by IntelliJ IDEA.
 * User: arun
 * Date: Sep 9, 2009
 * Time: 2:56:54 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class provides a Test to scan a jar file for serializable classes, and then verify if all of those contain a variable SerialUID and further, it is unique.
 * the class FieldSpy contains methods Test and CheckforSerial and private instance Hashmap. Test parses the jar and passes on the serializable classes to method checkforserial.
 * Checkforserial ensures that they class passed to it contains the SerialUID variable and hashes the value in it along with the classname. In case of collision, failure is thrown
 *
 * @author arun
 */
public class SerialVersionUIDTest extends RMITestCase {

    private String resourcePath, path;

    public SerialVersionUIDTest(TestCaseElement testCaseConfig) throws Exception {
        super(testCaseConfig);
        setUp();

    }

    public void setUp() throws Exception {
        super.setUp();
        path = testCaseProperties.getProperty("path");
        resourcePath = testCaseConfig.getTestBaseDir() + File.separator + "rmi" + File.separator + "Serialization";

    }

    public void testAPIspy() {
        String[] args = new String[1];
        args[0] = resourcePath + File.separator + path + testCaseProperties.getProperty("apiFile");
        FieldSpy jamesbond = new FieldSpy();
        try {
             boolean bol = jamesbond.Test(args);
            StringBuffer sb = new StringBuffer();
            for(Object o : jamesbond.getClassNamesWOSVUID())
            {
                sb.append(" Class \""+ (String)o+"\" does not have SerialVersionUID"+"\n");
            }
            assertTrue(sb.toString(),bol);
//            assertTrue(jamesbond.Test(args));
        } catch (Exception e) {
            Assert.assertTrue("Test failed for " + args[0], false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void testImplspy() {
        String[] args = new String[1];
        args[0] = resourcePath + File.separator + path + testCaseProperties.getProperty("implFile");
        FieldSpy jamesbond = new FieldSpy();
        try {
            boolean bol = jamesbond.Test(args);
            StringBuffer sb = new StringBuffer();
            for(Object o : jamesbond.getClassNamesWOSVUID())
            {
                sb.append(" Class \""+ (String)o+"\" does not have SerialVersionUID"+"\n");
            }
            assertTrue(sb.toString(),bol);
        } catch (Exception e) {
            Assert.assertTrue("Test failed for " + args[0], false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

}

