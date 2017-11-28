package com.fiorano.stf.logger;

import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.DRTTestCase;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import com.fiorano.stf.util.CustomFormatter;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by IntelliJ IDEA.
 * User: sagar
 * Date: 26 Oct, 2010
 * Time: 6:37:14 PM
 * To change this template use File | Settings | File Templates.
 */

public class DRTTestLogger {

    private static Logger drtTest_logger;
    private static Logger drtOrderLogger;


    public static void initializeLogger() {
        try {
            TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();

            String TestingHome = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
            File th = new File(TestingHome);
            String location = th.getAbsolutePath() + File.separator + TestEnvironmentConstants.REPORTS_DIR + File.separator + DRTTestCase.DRTTest_Logs;
            // This logger is used to log the exception messages and other log messages like "method successfully executed".
            drtTest_logger = Logger.getLogger("com.fiorano.stf.esb.drt.execution");
            drtTest_logger.setUseParentHandlers(false);
            drtTest_logger.setLevel(Level.INFO);
            FileHandler fh;
            new File(location).mkdirs();
            fh = new FileHandler(location + File.separator + "test-output.log", true);
            fh.setLevel(Level.INFO);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            drtTest_logger.addHandler(fh);

            //This logger is used only to log the order in which methods were executed in the Test Bench.

            drtOrderLogger = Logger.getLogger("com.fiorano.stf.esb.drt.order");
            drtOrderLogger.setUseParentHandlers(false);
            FileHandler fh_one;
            fh_one = new FileHandler(location + File.separator + "test-order.log", true);
            fh_one.setLevel(Level.INFO);
            CustomFormatter custom_formatter = new CustomFormatter();
            fh_one.setFormatter(custom_formatter);
            drtOrderLogger.addHandler(fh_one);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logMethodOrder(String message) {
        drtOrderLogger.log(Level.INFO, message);
    }

    public static void log(String message) {
        drtTest_logger.log(Level.SEVERE, message);
    }

    public static void logInfo(String message) {
        drtTest_logger.log(Level.INFO, message);
    }


    public static void log(Level level, String message, Throwable e) {
        drtTest_logger.log(level, message, e);
    }

}
