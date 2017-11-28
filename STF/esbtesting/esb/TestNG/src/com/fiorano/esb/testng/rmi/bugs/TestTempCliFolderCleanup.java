package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.remote.RMIClient;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import com.fiorano.util.io.FileUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 9/19/11
 * Time: 5:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestTempCliFolderCleanup extends AbstractTestNG{
    char fsc = File.separatorChar;
    String pathUptoCli;
    String exportPath ;


    @BeforeClass(groups = "TempCliFolderCleanup", alwaysRun=true, description = "bug number 21037")
    public void startSetUp(){
            initializeProperties();
            TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
            String FioranoHome = testEnvConfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);
            File fh = new File(FioranoHome);
            pathUptoCli = fh.getAbsolutePath() + fsc + "esb" + fsc + "tools" + fsc + "cli";
            exportPath = pathUptoCli+ fsc+ "STFTestDir";
    }

    @Test(groups = "TempCliFolderCleanup", alwaysRun=true, description = "bug number 21037")
    public void runCliTool() throws InterruptedException {
//        System.out.println(pathUptoCli + fsc + "build.xml exportAppsWithLibs");
        Logger.LogMethodOrder(Logger.getOrderMessage("runCliTool", "TestTempCliFolderCleanup"));
        File exportDir = new File(exportPath);
        exportDir.mkdirs();
        try {
            Process p=Runtime.getRuntime().exec("ant exportAppsWithLibs -DCONNECTION_USER_NAME=admin -DCONNECTION_PASSWORD=passwd -DAPPLICATION_EXPORT_DIR_LIB=" + exportPath, null, exportDir.getParentFile());
            System.out.println("running ant in cli. Please wait.");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line1 = null;                         //to print the output of hidden console on terminal

            while ((line1 = in.readLine()) != null) {
                System.out.println(line1);
            }
            p.waitFor();
            Logger.Log(Level.INFO, "running ant in cli.");

        } catch (IOException e) {
            Assert.fail("could not run ant in cli");
            Logger.Log(Level.SEVERE, Logger.getErrMessage("runCliTool", "TestTempCliFolderCleanup"));
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test(groups = "TempCliFolderCleanup", alwaysRun=true, description = "bug number 21037", dependsOnMethods = "runCliTool")
    public void checkTempFolder() {
        Logger.LogMethodOrder(Logger.getOrderMessage("checkTempFolders", "TestTempCliFolderCleanup"));
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(pathUptoCli + fsc + "eStudio.properties");
        } catch (FileNotFoundException e) {
            Assert.fail("file build.properties was not found in cli");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        testProperties = new Properties();
        try {
            if(fis!=null){
                testProperties.load(fis);
            } else
                Assert.fail("Fail to load " + pathUptoCli + fsc + "eStudio.properties");
        } catch (IOException e) {
            Assert.fail("file build.properties was not found in cli");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        FileFilter fileFilter = new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        };

        File test = null;
        File[] children = null;
        if(exportPath != null){
         test =  new File(exportPath);
        }
        if(test !=null){
        children = test.listFiles(fileFilter);
        }
        try {
            if(children.length==0) {
                System.out.println("no temp directories in export folder were remained");
                Logger.Log(Level.INFO, "no temp directories in export folder were remained");
            }
            else{
                Assert.fail("Could not clear temp folders in export directory");
            Logger.Log(Level.SEVERE, Logger.getErrMessage("checkTempFolder", "TestTempCliFolderCleanup"));
            }
        } catch (NullPointerException e) {
                System.out.println("no temp directories in export folder were remained");
                Logger.Log(Level.INFO, "no temp directories in export folder were remained");
        }

        test =  new File(pathUptoCli + fsc + "temp");
            if(!test.exists()){
                System.out.println("Temp folder was not found in cli. Test successful.");
            Logger.Log(Level.INFO,Logger.getFinishMessage("checkTempFolder", "TestTempCliFolderCleanup"));
            }
            else  {
                Assert.fail("temp folder was not successfully deleted. the test is not passed");
            Logger.Log(Level.SEVERE, Logger.getErrMessage("checkTempFolder", "TestTempCliFolderCleanup"));
            }

        File deleteDir = new File(exportPath);
        if(deleteDir.exists())
            FileUtil.deleteDir(deleteDir);
    }

}
