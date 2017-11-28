package com.fiorano.stf.test.core;

import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ServerStatusController;
import org.testng.TestNG;
import org.testng.xml.Parser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * User: vishnu
 * This class extends DRTTestCase only for the sake running TestNG cases from STF as STF is using
 * Junit as its basic test class element.
 */
public class TestNGTestCase extends DRTTestCase {

    public String testcasename = null;
    public static String testNG_ReportsDir = null;

    public TestNGTestCase(String testCaseName) {
        super(testCaseName);
    }

    public TestNGTestCase(TestCaseElement testCaseConfig, ServerStatusController serverStatusController) {
        super(testCaseConfig, serverStatusController);
    }

    public TestNGTestCase(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
        testcasename = testCaseConfig.getName();
    }

    protected void setUp() throws Exception {
        testNG_ReportsDir = "TestNG_Reports" + "__" + testcasename;
    }

    /**
     * This method has a prefix "test", so that it can be called by STF.
     * We are not implementing "public static ArrayList getOrder()" which lists the order of running methods. see "public class AddBreakPointsTest extends DRTTestCase "
     * Since this is the only method, by default, this is picked by STF.
     *
     * @throws Exception
     */
    public void testRunSuite() throws Exception {

        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();

        String TestingHome = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        File th = new File(TestingHome);
        TestNG tng = this.loadTestNGSuites(th);
        if (tng == null) {
            System.out.println("Wont be running testNG suite due to rise of some error.!.");
        } else
            tng.run();
    }

    /**
     * parses a testng.xml specification file and loads the testng suites.
     *
     * @param testingHome
     * @return TestNG object
     */
    protected TestNG loadTestNGSuites(File testingHome) {

        List suites = null;
        try {
            //suites = new ArrayList(new Parser(new FileInputStream(testingHome.getAbsolutePath()+"/esb/TestNG/src/testng.xml")).parse());
            //the file passed here is same as the one specified in testspec.xml
            suites = new ArrayList(new Parser(new FileInputStream(((DRTTestCaseElement) (this.testCaseConfig)).getPropertyFilePath())).parse());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        TestNG testng = new TestNG();
        String reportsDirectory = testingHome.getAbsolutePath() + File.separator + TestEnvironmentConstants.REPORTS_DIR
                + File.separator + testNG_ReportsDir;
        new File(reportsDirectory).mkdir();
        testng.setOutputDirectory(reportsDirectory);
        testng.setXmlSuites(suites);
        testng.setDefaultTestName("STF_TestNG_Test_Case");
        return testng;
    }

}
