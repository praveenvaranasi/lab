package com.fiorano.esb.testng.rmi.log;

import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.CustomFormatter;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import com.fiorano.stf.test.core.TestNGTestCase;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

/**
 * Created by IntelliJ IDEA.
 * User: lokesh2
 * Date: Feb 4, 2011
 * Time: 12:41:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class Logger {

    private static java.util.logging.Logger test_logger;
    private static java.util.logging.Logger OrderLogger;

    public static void initializeLogger() {
        if (test_logger != null && OrderLogger != null)
            return;
        try {
            TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();

            String TestingHome = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
            File th = new File(TestingHome);
            String logsDir = th.getAbsolutePath() + File.separator + TestEnvironmentConstants.REPORTS_DIR
                    + File.separator + TestNGTestCase.testNG_ReportsDir + File.separator + "logs";
            new File(logsDir).mkdir();
            // This logger is used to log the exception messages and other log messages like "method successfully executed".
            test_logger = java.util.logging.Logger.getLogger("com.fiorano.esb.Test.rmi.execution");
            test_logger.setUseParentHandlers(false);
            test_logger.setLevel(Level.parse(AbstractTestNG.getProperty("Log_Level").trim()));
            FileHandler fh;
            fh = new FileHandler(logsDir + File.separator + "test-output.log", true);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            test_logger.addHandler(fh);

            //This logger is used only to log the order in which methods were executed in the Test Bench.
            OrderLogger = java.util.logging.Logger.getLogger("com.fiorano.esb.Test.rmi.order");
            OrderLogger.setUseParentHandlers(false);
            FileHandler fh_one;
            fh_one = new FileHandler(logsDir + File.separator + "test-order.log", true);
            CustomFormatter custom_formatter = new CustomFormatter();
            fh_one.setFormatter(custom_formatter);
            OrderLogger.addHandler(fh_one);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void LogMethodOrder(String message) {
        OrderLogger.log(Level.INFO, message);
    }

    public static void Log(Level level, String message) {
        test_logger.log(level, message);
    }

    public static void Log(Level level, String message, Throwable e) {
        test_logger.log(level, message, e);
    }

    public static String getErrMessage(String methodName, String className) {
        return "Exception Occurred during executing test method " + methodName + " in class " + className;
    }

    public static String getExecuteMessage(String methodName, String className) {
        return "Executing test method " + methodName + " in class " + className;
    }

    public static String getFinishMessage(String methodName, String className) {
        return className + " :::: " + methodName + "()" + " :::: " + "Succesfully executed";
    }

    public static String getOrderMessage(String methodName, String className) {
        return className + " :::: " + methodName + "()";
    }
}
