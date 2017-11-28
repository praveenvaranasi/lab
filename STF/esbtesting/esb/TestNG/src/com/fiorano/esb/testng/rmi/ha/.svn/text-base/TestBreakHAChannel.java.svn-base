package com.fiorano.esb.testng.rmi.ha;

import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.MachineElement;
import com.fiorano.stf.test.core.TestEnvironment;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/4/11
 * Time: 12:42 PM
 * test
     *    1. Run ha server on machine1 & machine3. (Machine 1 is primary and Machine 3 is secondary)
     *    2. Get Status of Machine1 and Machine3.
     *    3. Block the link between these machine1 and machine3.
     *    4. Thread.sleep for some time.
     *    5. Get Status of Machine1 and Machine3.
     *    6. Active server should go to Standalone and Passive should remain in Passive mode.
     *    7. Re-establish Connection.
     *    8. Standalone server should go to Active mode and Passive server should remain in Passive mode.
     *    9. Flush Iptables in the teardown method.
 */
public class TestBreakHAChannel extends AbstractTestNG{
    ExecutionController executionController;
    ServerStatusController serverStatusController;
    private int primaryRMIPort;
    private int secondaryRMIPort;
    private int primaryHAPort;
    private int secondaryHAPort;

    private static int shortSleep=60000;
    private static int midSleep=300000;
    private static int longSleep=360000;

    @Test(groups = "BreakHAChannel", description = "this test is a part of HA tests",  alwaysRun = true)
    void startSetUp(){
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetUp", "BreakHAChannel"));
        initializeProperties("HA"+fsc+"test_fes.properties");
        serverStatusController = ServerStatusController.getInstance();
        executionController = ExecutionController.getInstance();
        this.setPrimaryRMIPort(testProperties.getProperty("PRIMARY_RMI_PORT"));
        this.setSecondaryRMIPort(testProperties.getProperty("SECONDARY_RMI_PORT"));
        this.setPrimaryHAPort(testProperties.getProperty("PRIMARY_HA_PORT"));
        this.setSecondaryHAPort(testProperties.getProperty("SECONDARY_HA_PORT"));
    }

    @Test(groups = "BreakHAChannel", description = "this test is a part of HA tests", dependsOnMethods = "startSetUp", alwaysRun = true)
    public void testBreakConnection(){
        Logger.LogMethodOrder(Logger.getOrderMessage("testBreakConnection", "TestBreakHAChannel:mode=" + testProperties.getProperty("MODE")));
        TestEnvironment testEnvironment = serverStatusController.getTestEnvironment();
        String oldStatusMachine1, oldStatusMachine3, newStatusMachine1, newStatusMachine3;
        
        try {
            
            try {
                Thread.sleep(shortSleep);//sleep  man sleep ....zzzzzzzzzzzzz
            } catch (InterruptedException ignore)
            {
            }

            oldStatusMachine1 = serverStatusController.getServerStatus(serverStatusController.getPrimaryFESAddress(),primaryRMIPort);
            oldStatusMachine3 = serverStatusController.getServerStatus(serverStatusController.getSecondaryFESAddress(),secondaryRMIPort);
            System.out.println("Machine 1 status before breaking the connection = "+oldStatusMachine1);
            System.out.println("Machine 3 status before breaking the connection = "+oldStatusMachine3);


            if (oldStatusMachine1 == null || oldStatusMachine3 == null) {
                Assert.fail("TestCase testShutDownActive OF class " + TestShutdownActive.class.toString() + " in mode = " + testProperties.getProperty("MODE") + " failed !!! Reason: Starting Environment not Correct. One of Server is not responding.");
            }


            boolean m1IsActive = (oldStatusMachine1.equalsIgnoreCase("active"));
            boolean m1IsPassive = (oldStatusMachine1.equalsIgnoreCase("passive"));

            boolean m3IsActive = (oldStatusMachine3.equalsIgnoreCase("active"));
            boolean m3IsPassive = (oldStatusMachine3.equalsIgnoreCase("passive"));


            if (!((m1IsActive && m3IsPassive) || (m1IsPassive && m3IsActive))) {
                Assert.fail("TestCase testShutDownActive OF class " + TestShutdownActive.class.toString() + " in mode = " + testProperties.getProperty("MODE") + " failed !!! Reason: Starting Environment not Correct. One Server should in Active State and other should be in Passive State.");
            }




            executionController.modifyFireWallRule(serverStatusController.getPrimaryFESAddress(),serverStatusController.getSecondaryFESAddress(), secondaryHAPort, primaryHAPort, "A");
            executionController.modifyFireWallRule(serverStatusController.getSecondaryFESAddress(), serverStatusController.getPrimaryFESAddress(), primaryHAPort, secondaryHAPort, "A");

            try {
                Thread.sleep(longSleep);//sleep  man sleep ....zzzzzzzzzzzzz
            } catch (InterruptedException ignore)
            {
            }

            newStatusMachine1 = serverStatusController.getServerStatus(serverStatusController.getPrimaryFESAddress(),primaryRMIPort);
            newStatusMachine3 = serverStatusController.getServerStatus(serverStatusController.getSecondaryFESAddress(),secondaryRMIPort);
            System.out.println("Machine 1 status after breaking the connection = "+newStatusMachine1);
            System.out.println("Machine 3 status after breaking the connection = "+newStatusMachine3);

            if (oldStatusMachine1.equalsIgnoreCase("active"))
            {
                if (newStatusMachine1.equalsIgnoreCase("standalone"))
                {
                    ///Case Passed
                    //System.out.println("Test Case 1 Passed");
                }
                else
                {
                   Assert.fail("TestCase testBreakConnection OF class "+ TestBreakHAChannel.class.toString() + " in mode = "+testProperties.getProperty("MODE")+" failed !!! Reason: The Active server didn't switch to Standalone after breaking the connection. New Server status for the Active Server is :"+newStatusMachine1 );
                }
            }

            if (oldStatusMachine3.equalsIgnoreCase("passive"))
            {
                if (newStatusMachine3.equalsIgnoreCase("passive"))
                {
                    ///Case Passed
                   // Assert.("TestCase TestBreakHAChannel OF class "+TestBreakHAChannel.class.toString() + " in mode = "+testProperties.getProperty("MODE")+" passed !!! ");
                    //System.out.println("Test Case 1 Passed");
                }
                else
                {
                   /// Case Fail
                     Assert.fail("TestCase testBreakConnection OF class "+ TestBreakHAChannel.class.toString() + " in mode = "+testProperties.getProperty("MODE")+" failed !!! Reason: The Passive server didn't remain passive after breaking the connection. New Server status for the Passive Server is "+newStatusMachine3 );
                }
            }



            if (oldStatusMachine3.equalsIgnoreCase("active"))
            {
                if (newStatusMachine3.equalsIgnoreCase("standalone"))
                {
                    ///Case Passed
                    //System.out.println("Test Case 1 Passed");
                }
                else
                {
                   Assert.fail("TestCase testBreakConnection OF class "+ TestBreakHAChannel.class.toString() + " in mode = "+testProperties.getProperty("MODE")+" failed !!! Reason: The Active server didn't switch to Standalone after breaking the connection. New Server status for the Active Server is :"+newStatusMachine3 );
                }
            }

            if (oldStatusMachine1.equalsIgnoreCase("passive"))
            {
                if (newStatusMachine1.equalsIgnoreCase("passive"))
                {
                    ///Case Passed
                   // Assert.("TestCase TestBreakHAChannel OF class "+TestBreakHAChannel.class.toString() + " in mode = "+testProperties.getProperty("MODE")+" passed !!! ");
                    //System.out.println("Test Case 1 Passed");
                }
                else
                {
                   /// Case Fail
                     Assert.fail("TestCase testBreakConnection OF class "+ TestBreakHAChannel.class.toString() + " in mode = "+testProperties.getProperty("MODE")+" failed !!! Reason: The Passive server didn't remain passive after breaking the connection. New Server status for the Passive Server is "+newStatusMachine1 );
                }
            }

        } catch (STFException e) {
             Logger.Log(Level.SEVERE,"test case: testBreakConnection OF class "+ TestBreakHAChannel.class.toString() + " in mode = "+testProperties.getProperty("MODE")+" failed !!! ",e);
            e.printStackTrace();
            Assert.fail("TestCase testBreakConnection OF class "+ TestBreakHAChannel.class.toString() + " in mode = "+testProperties.getProperty("MODE")+" failed !!!"  + e.getMessage());
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testBreakConnection", "TestBreakHAChannel:mode="+testProperties.getProperty("MODE")));
        System.out.println("-------------------");

    }


    @Test(groups = "BreakHAChannel", description = "this test is a part of HA tests", alwaysRun = true, dependsOnMethods = "testBreakConnection")
    public void testPutConnection(){
        Logger.LogMethodOrder(Logger.getOrderMessage("testPutConnection", "TestBreakHAChannel:mode="+testProperties.getProperty("MODE")));

        TestEnvironment testEnvironment = serverStatusController.getTestEnvironment();

        String statusM1Old, statusM3Old, statusM1New, statusM3New;
        try {
            statusM1Old = serverStatusController.getServerStatus(serverStatusController.getPrimaryFESAddress(),primaryRMIPort);
            statusM3Old = serverStatusController.getServerStatus(serverStatusController.getSecondaryFESAddress(),secondaryRMIPort);
            System.out.println("Machine 1 status before re-establishing the broken connection  = "+statusM1Old);
            System.out.println("Machine 3 status before re-establishing the broken connection  = "+statusM3Old);
            executionController.modifyFireWallRule(serverStatusController.getPrimaryFESAddress(), serverStatusController.getSecondaryFESAddress(), secondaryHAPort, primaryHAPort, "D");
            executionController.modifyFireWallRule(serverStatusController.getSecondaryFESAddress(), serverStatusController.getPrimaryFESAddress(), primaryHAPort, secondaryHAPort, "D");

            try {
                Thread.sleep(longSleep);//sleep  man sleep ....zzzzzzzzzzzzz
            } catch (InterruptedException ignore)
            {
            }

            statusM1New = serverStatusController.getServerStatus(serverStatusController.getPrimaryFESAddress(),primaryRMIPort);
            statusM3New = serverStatusController.getServerStatus(serverStatusController.getSecondaryFESAddress(),secondaryRMIPort);
            System.out.println("Machine 1 status after re-establishing the broken connection = "+statusM1New);
            System.out.println("Machine 3 status after re-establishing the broken connection = "+statusM3New);

            if (statusM1Old.equalsIgnoreCase("standalone"))
            {
                if (statusM1New.equalsIgnoreCase("active"))
                {
                    ///Case Passed
                    //System.out.println("Test Case 2 Passed");
                }
                else
                {
                   Assert.fail("TestCase testPutConnection OF class "+ TestBreakHAChannel.class.toString() + " in mode = "+testProperties.getProperty("MODE")+" failed !!! Reason: The Standalone server didn't switch to Active after re-establishing the connection. New Server status for the Standalone Server is :"+statusM1New );
                }
            }

            if (statusM3Old.equalsIgnoreCase("passive"))
            {
                if (statusM3New.equalsIgnoreCase("passive"))
                {
                    ///Case Passed
                   // Assert.("TestCase TestBreakHAChannel OF class "+TestBreakHAChannel.class.toString() + " in mode = "+testCaseProperties.getProperty("MODE")+" passed !!! ");
                    //System.out.println("Test Case 2 Passed");
                }
                else
                {
                   /// Case Fail
                     Assert.fail("TestCase testPutConnection OF class "+ TestBreakHAChannel.class.toString() + " in mode = "+testProperties.getProperty("MODE")+" failed !!! Reason: The Passive server didn't remain passive after re-establishing the connection. New Server status for the Passive Server is "+statusM3New );
                }
            }



            if (statusM3Old.equalsIgnoreCase("standalone"))
            {
                if (statusM3New.equalsIgnoreCase("active"))
                {
                    ///Case Passed
                    //System.out.println("Test Case 2 Passed");
                }
                else
                {
                   Assert.fail("TestCase testPutConnection OF class "+ TestBreakHAChannel.class.toString() + " in mode = "+testProperties.getProperty("MODE")+" failed !!! Reason: The Standalone server didn't switch to Active after re-establishing the connection. New Server status for the Standalone Server is :"+statusM1New );
                }
            }

            if (statusM1Old.equalsIgnoreCase("passive"))
            {
                if (statusM1New.equalsIgnoreCase("passive"))
                {
                    ///Case Passed
                   // Assert.("TestCase TestBreakHAChannel OF class "+TestBreakHAChannel.class.toString() + " in mode = "+testCaseProperties.getProperty("MODE")+" passed !!! ");
                    //System.out.println("Test Case 2 Passed");
                }
                else
                {
                   /// Case Fail
                     Assert.fail("TestCase testPutConnection OF class "+ TestBreakHAChannel.class.toString() + " in mode = "+testProperties.getProperty("MODE")+" failed !!! Reason: The Passive server didn't remain passive after re-establishing the connection. New Server status for the Passive Server is "+statusM3New );
                }
            }

        } catch (STFException e) {
             Logger.Log(Level.SEVERE,"test case: testPutConnection OF class "+ TestBreakHAChannel.class.toString() + " in mode = "+testProperties.getProperty("MODE")+" failed !!! ",e);
            e.printStackTrace();
            Assert.fail("TestCase testPutConnection OF class "+ TestBreakHAChannel.class.toString() + " in mode = "+testProperties.getProperty("MODE")+" failed !!!"  + e.getMessage());
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testPutConnection", "TestBreakHAChannel:mode="+testProperties.getProperty("MODE")));
        System.out.println("-------------------");

    }

    @Test(groups = "BreakHAChannel", description = "this test is a part of HA tests", alwaysRun = true, dependsOnMethods = "testPutConnection")
    protected void tearDown() throws Exception{
                executionController.flushFilters(serverStatusController.getPrimaryFESAddress());
                executionController.flushFilters(serverStatusController.getSecondaryFESAddress());
            //nothing else to do.
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
}
