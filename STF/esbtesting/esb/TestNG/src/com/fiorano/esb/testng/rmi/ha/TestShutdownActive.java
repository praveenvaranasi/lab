package com.fiorano.esb.testng.rmi.ha;

import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.MachineElement;
import com.fiorano.stf.test.core.TestEnvironment;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/5/11
 * Time: 6:01 PM
 * 1. Run ha server on machine1 & machine3. (Machine 1 is primary and Machine 3 is secondary). Both are isWrapper true mode.
 * 2. Run Lock machine on machine2.
 * 3. Get Status of Machine1 and Machine3.
 * 4. Check if one if Active and Other is Passive.
 * 5. Shutdown the Active Server whichever it is.
 * 6. Wait for sometime.
 * 7. Check the status of the server which was Passive ealier.
 * 8. It should be standalone. Fail the test case if it is not standalone.
 * 9. Restart the server which was shutdown in the process.
 */
public class TestShutdownActive extends AbstractTestNG{
    ExecutionController executionController;
    ServerStatusController serverStatusController;
    protected int primary_rmi_port;
    protected int secondary_rmi_port;
    protected String profile_Type;
    private int primaryRMIPort;
    private int secondaryRMIPort;
    private int primaryHAPort;
    private int secondaryHAPort;
    protected String MODE;
    private static int shortSleep=60000;
    private static int midSleep=300000;
    private static int longSleep=360000;

    @BeforeClass (groups = "shutDownActive", alwaysRun = true)
    void startSetUp(){
        initializeProperties("HA" + fsc + "test_fes.properties");
       serverStatusController = ServerStatusController.getInstance();
       executionController = ExecutionController.getInstance();
       this.setPrimaryRMIPort(testProperties.getProperty("PRIMARY_RMI_PORT"));
       this.setSecondaryRMIPort(testProperties.getProperty("SECONDARY_RMI_PORT"));
       this.setPrimaryHAPort(testProperties.getProperty("PRIMARY_HA_PORT"));
       this.setSecondaryHAPort(testProperties.getProperty("SECONDARY_HA_PORT"));
       this.setMode(testProperties.getProperty("MODE"));
    }

    @Test(groups = "shutDownActive", dependsOnMethods = "startSetUp", alwaysRun = true)
    public void ShutDownActive() {
    Logger.LogMethodOrder(Logger.getOrderMessage("testShutDownActive", "ShutdownActive:mode=" + testProperties.getProperty("MODE")));
        TestEnvironment testEnvironment = serverStatusController.getTestEnvironment();
        String oldStatusPrimary, oldStatusSecondary, newStatusMachine1, newStatusMachine3;

        try {
            try {
                Thread.sleep(shortSleep);//sleep  man sleep ....zzzzzzzzzzzzz
            } catch (InterruptedException ignore)
            {
            }
            oldStatusPrimary = serverStatusController.getPrimaryFESStatus();
            oldStatusSecondary = serverStatusController.getSecondaryFESStatus();

            System.out.println("Status of primary server before shutting down the active server = "+oldStatusPrimary);
            System.out.println("Status of secondary server before shutting down the active server = "+oldStatusSecondary);

            if (oldStatusPrimary == null || oldStatusSecondary == null)
            {
                Assert.fail("TestCase ShutdownPassive OF class " + TestShutdownPassive.class.toString() + " in mode = " + testProperties.getProperty("MODE") + " failed !!! Reason: Starting Environment not Correct. One of Server is not responding.");
            }


            boolean priIsActive = (oldStatusPrimary.equalsIgnoreCase("active"));
            boolean priIsPassive= (oldStatusPrimary.equalsIgnoreCase("passive"));

            boolean secIsActive = (oldStatusSecondary.equalsIgnoreCase("active"));
            boolean secIsPassive= (oldStatusSecondary.equalsIgnoreCase("passive"));


            if (!((priIsActive && secIsPassive)||(priIsPassive && secIsActive)))
            {
                Assert.fail("TestCase ShutdownPassive OF class "+ TestShutdownPassive.class.toString() + " in mode = "+testProperties.getProperty("MODE")+" failed !!! Reason: Starting Environment not Correct. One Server should in Active State and other should be in Passive State.");
            }
            String activeMachineAddress , passiveMachineAddress, activeProfileName, passiveProfileName;
            int activeRMIPort, passiveRMIPort;


            if (priIsPassive) {
                activeMachineAddress = serverStatusController.getSecondaryFESAddress();
                passiveMachineAddress = serverStatusController.getPrimaryFESAddress();
                activeProfileName = "haprofile1/secondary";
                passiveProfileName ="haprofile1/primary";
                activeRMIPort = secondaryRMIPort;
                passiveRMIPort = primaryRMIPort;
            } else{
                activeMachineAddress = serverStatusController.getPrimaryFESAddress();
                passiveMachineAddress = serverStatusController.getSecondaryFESAddress();
                activeProfileName = "haprofile1/primary";
                passiveProfileName ="haprofile1/secondary";
                activeRMIPort = primaryRMIPort;
                passiveRMIPort = secondaryRMIPort;
            }

            executionController.shutdownServerOnRemote(activeMachineAddress, this.MODE, activeProfileName, false, true);
            try {
                Thread.sleep(longSleep);
            }
            catch (InterruptedException ignore) {
            }

            newStatusMachine3 = serverStatusController.getServerStatus(passiveMachineAddress, passiveRMIPort);
            System.out.println("Machine 3 status after shutting down Active Server at machine 1 = " + newStatusMachine3);

            if (!newStatusMachine3.equalsIgnoreCase("standalone")) {
                Assert.fail("TestCase testShutDownActive of class " + TestShutdownActive.class.toString() + " in mode = " + testProperties.getProperty("MODE") + " failed !!! Reason: The Passive server didn't go to Standalone state after shutting down the Active Server");

            }
            executionController.startServerOnRemote(activeMachineAddress, this.MODE, activeProfileName, false, true);
            try {
                Thread.sleep(longSleep);
            }
            catch (InterruptedException ignore) {
            }
            newStatusMachine1 = serverStatusController.getServerStatus(activeMachineAddress, activeRMIPort);
            System.out.println("Machine 1 status after restarting it back= " + newStatusMachine1);
            if (!newStatusMachine1.equalsIgnoreCase("passive")) {
                Assert.fail("TestCase testShutDownActive of class " + TestShutdownActive.class.toString() + " in mode = " + testProperties.getProperty("MODE") + " failed !!! Reason: The server which was shut down was started again but it didn't switch to passive");
            }
    } catch (STFException e) {
        Logger.Log(Level.SEVERE, "test case: testShutDownActive OF class " + TestShutdownActive.class.toString() + " in mode = " + testProperties.getProperty("MODE") + " failed !!! ", e);
        e.printStackTrace();
        Assert.fail("TestCase testShutDownActive OF class " + TestShutdownActive.class.toString() + " in mode = " + testProperties.getProperty("MODE") + " failed !!!" + e.getMessage());
        }
    Logger.Log(Level.INFO, Logger.getFinishMessage("testShutDownActive", "ShutdownActive:mode=" + testProperties.getProperty("MODE")));
    System.out.println("-------------------");

    }



    /*definition of auxiliary functions*/
    private void setSecondaryHAPort(String property) {
         this.secondaryHAPort=Integer.parseInt(property);
    }

    private void setPrimaryHAPort(String property) {
       this.primaryHAPort=Integer.parseInt(property);
    }

    private void setSecondaryRMIPort(String property) {
        this.secondaryRMIPort=Integer.parseInt(property);
    }

    private void setPrimaryRMIPort(String property) {
        this.primaryRMIPort=Integer.parseInt(property);
    }

    private void setMode(String property) {
        this.MODE = property;
    }
}