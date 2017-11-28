package com.fiorano.esb.junit.ha;

import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.DRTTestCase;
import com.fiorano.stf.test.core.TestCaseElement;
import com.fiorano.stf.test.core.TestEnvironment;
import com.fiorano.stf.test.core.MachineElement;
import com.fiorano.stf.consts.DRTTestConstants;
import com.fiorano.stf.logger.DRTTestLogger;
import junit.framework.Assert;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * User: prateek
 * Date: Nov 2, 2010
 * Time: 3:30:00 PM
 * 1. Run ha server on machine1 & machine3. (Machine 1 is primary and Machine 3 is secondary)
 * 2. Run Lock machine on machine2.
 * 3. Get Status of Machine1 and Machine3.
 * 4. Block the link between these machine1 and machine2.
 * 5. Block the link between these machine3 and machine2.
 * 6. Wait for sometime.
 * 7. Block the link between machine1 and machine3.
 * 8. Wait for sometime.
 * 9. Get Status of Machine1 and Machine3.
 * 10. Active server should go to Waiting and Passive should remain in Passive mode.
 * 11. Re-establish Connection between machine1 and machine2.
 * 12. Re-establish Connection between machine3 and machine2.
 * 13. Wait for sometime.
 * 14. Re-establish Connection between machine1 and machine3.
 * 15. Wait for sometime.
 * 16. Get Status of Machine1 and Machine3.
 * 17. Waiting server should go to Active and Passive should remain in Passive mode.
 * 18. Flush Iptables in the teardown method.
 */
public class NetworkDown extends DRTTestCase {
    ExecutionController executionController;
    private MachineElement machineElement3;
    private MachineElement machineElement1;
    private MachineElement machineElement2;
    private int primaryRMIPort;
    private int secondaryRMIPort;
    private int primaryHAPort;
    private int secondaryHAPort;
    private int dummyPort=1212;

    private static int shortSleep=60000;
    private static int midSleep=300000;
    private static int longSleep=360000;


    public NetworkDown(String testCaseName) {
        super(testCaseName);
        serverStatusController = ServerStatusController.getInstance();
        executionController = ExecutionController.getInstance();

    }

    public NetworkDown(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
        serverStatusController = ServerStatusController.getInstance();
        executionController = ExecutionController.getInstance();
    }

    public NetworkDown(TestCaseElement testCaseConfig, ServerStatusController serverStatusController) {
        super(testCaseConfig, serverStatusController);
        serverStatusController = ServerStatusController.getInstance();
        executionController = ExecutionController.getInstance();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.setPrimaryRMIPort(testCaseProperties.getProperty("PRIMARY_RMI_PORT"));
        this.setSecondaryRMIPort(testCaseProperties.getProperty("SECONDARY_RMI_PORT"));
        this.setPrimaryHAPort(testCaseProperties.getProperty("PRIMARY_HA_PORT"));
        this.setSecondaryHAPort(testCaseProperties.getProperty("SECONDARY_HA_PORT"));
    }

    private void setSecondaryHAPort(String property) {
        this.secondaryHAPort = Integer.parseInt(property);
    }

    private void setPrimaryHAPort(String property) {
        this.primaryHAPort = Integer.parseInt(property);
    }

    private void setSecondaryRMIPort(String property) {
        this.secondaryRMIPort = Integer.parseInt(property);
    }

    private void setPrimaryRMIPort(String property) {
        this.primaryRMIPort = Integer.parseInt(property);
    }

    public static ArrayList getOrder() {

        ArrayList methodsOrder = new ArrayList();

        methodsOrder.add("testBreakConnection");
        methodsOrder.add("testPutConnection");
        return methodsOrder;
    }

    /**
     * 1. Run ha server on machine1 & machine3. (Machine 1 is primary and Machine 3 is secondary)
     * 2. Run Lock machine on machine2.
     * 3. Get Status of Machine1 and Machine3.
     * 4. Block the link between these machine1 and machine2.
     * 5. Block the link between these machine3 and machine2.
     * 6. Wait for sometime.
     * 7. Block the link between machine1 and machine3.
     * 8. Wait for sometime.
     * 9. Get Status of Machine1 and Machine3.
     * 10. Active server should go to Waiting and Passive should remain in Passive mode.
     * 11. Re-establish Connection between machine1 and machine2.
     * 12. Re-establish Connection between machine3 and machine2.
     * 13. Wait for sometime.
     * 14. Re-establish Connection between machine1 and machine3.
     * 15. Wait for sometime.
     * 16. Get Status of Machine1 and Machine3.
     * 17. Waiting server should go to Active and Passive should remain in Passive mode.
     * 18. Flush Iptables in the teardown method.
     */

    public void testBreakConnection() {
        DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testBreakConnection", "NetworkDown:mode=" + testCaseProperties.getProperty("MODE")));

        TestEnvironment testEnvironment = serverStatusController.getTestEnvironment();
        machineElement1 = testEnvironment.getMachine("machine1");
        machineElement2 = testEnvironment.getMachine("machine2");
        machineElement3 = testEnvironment.getMachine("machine3");
        String oldStatusMachine1, oldStatusMachine3, newStatusMachine1, newStatusMachine3;

        try {
            try {
                Thread.sleep(shortSleep);//sleep  man sleep ....zzzzzzzzzzzzz
            } catch (InterruptedException ignore)
            {
            }
            oldStatusMachine1 = serverStatusController.getServerStatus(machineElement1.getAddress(), primaryRMIPort);
            oldStatusMachine3 = serverStatusController.getServerStatus(machineElement3.getAddress(), secondaryRMIPort);
            System.out.println("Machine 1 status before breaking the connection = " + oldStatusMachine1);
            System.out.println("Machine 3 status before breaking the connection = " + oldStatusMachine3);

            if (oldStatusMachine1 == null || oldStatusMachine3 == null) {
                Assert.fail("TestCase testShutDownActive OF class " + ShutdownActive.class.toString() + " in mode = " + testCaseProperties.getProperty("MODE") + " failed !!! Reason: Starting Environment not Correct. One of Server is not responding.");
            }

            boolean m1IsActive = (oldStatusMachine1.equalsIgnoreCase("active"));
            boolean m1IsPassive = (oldStatusMachine1.equalsIgnoreCase("passive"));

            boolean m3IsActive = (oldStatusMachine3.equalsIgnoreCase("active"));
            boolean m3IsPassive = (oldStatusMachine3.equalsIgnoreCase("passive"));


            if (!((m1IsActive && m3IsPassive) || (m1IsPassive && m3IsActive))) {
                Assert.fail("TestCase testShutDownActive OF class " + ShutdownActive.class.toString() + " in mode = " + testCaseProperties.getProperty("MODE") + " failed !!! Reason: Starting Environment not Correct. One Server should in Active State and other should be in Passive State.");
            }

            executionController.modifyFireWallRule(machineElement1.getAddress(), machineElement2.getAddress(), 7,dummyPort, "A");

            executionController.modifyFireWallRule(machineElement3.getAddress(), machineElement2.getAddress(), 7,dummyPort ,"A");
            try {
                Thread.sleep(longSleep);//sleep  man sleep ....zzzzzzzzzzzzz
            } catch (InterruptedException ignore) {
            }

            executionController.modifyFireWallRule(machineElement1.getAddress(), machineElement3.getAddress(), secondaryHAPort, primaryHAPort, "A");
            executionController.modifyFireWallRule(machineElement3.getAddress(), machineElement1.getAddress(), primaryHAPort, secondaryHAPort, "A");


            try {
                Thread.sleep(longSleep);//sleep  man sleep ....zzzzzzzzzzzzz
            } catch (InterruptedException ignore) {
            }

            newStatusMachine1 = serverStatusController.getServerStatus(machineElement1.getAddress(), primaryRMIPort);
            newStatusMachine3 = serverStatusController.getServerStatus(machineElement3.getAddress(), secondaryRMIPort);
            System.out.println("Machine 1 status after breaking the connection = " + newStatusMachine1);
            System.out.println("Machine 3 status after breaking the connection = " + newStatusMachine3);

            if (oldStatusMachine1.equalsIgnoreCase("active")) {
                if (newStatusMachine1.equalsIgnoreCase("waiting")) {
                    ///Case Passed
                    //System.out.println("Test Case 1 Passed");
                } else {
                    Assert.fail("TestCase testBreakConnection OF class " + NetworkDown.class.toString() + " in mode = " + testCaseProperties.getProperty("MODE") + " failed !!! Reason: The Active server didn't switch to waiting after breaking the connection. New Server status for the Active Server is :" + newStatusMachine1);
                }
            }

            if (oldStatusMachine3.equalsIgnoreCase("passive")) {
                if (newStatusMachine3.equalsIgnoreCase("passive")) {
                    ///Case Passed
                    //Assert.("TestCase testBreakConnection OF class "+NetworkDown.class.toString() + " in mode = "+testCaseProperties.getProperty("MODE")+" passed !!! ");
                    //System.out.println("Test Case 1 Passed");
                } else {
                    /// Case Fail
                    Assert.fail("TestCase testBreakConnection OF class " + NetworkDown.class.toString() + " in mode = " + testCaseProperties.getProperty("MODE") + " failed !!! Reason: The Passive server didn't remain passive after breaking the connection. New Server status for the Passive Server is " + newStatusMachine3);
                }
            }


            if (oldStatusMachine3.equalsIgnoreCase("active")) {
                if (newStatusMachine3.equalsIgnoreCase("waiting")) {
                    ///Case Passed
                    //System.out.println("Test Case 1 Passed");
                } else {
                    Assert.fail("TestCase testBreakConnection OF class " + NetworkDown.class.toString() + " in mode = " + testCaseProperties.getProperty("MODE") + " failed !!! Reason: The Active server didn't switch to waiting after breaking the connection. New Server status for the Active Server is :" + newStatusMachine3);
                }
            }

            if (oldStatusMachine1.equalsIgnoreCase("passive")) {
                if (newStatusMachine1.equalsIgnoreCase("passive")) {
                    ///Case Passed
                    // Assert.("TestCase testBreakConnection OF class "+NetworkDown.class.toString() + " in mode = "+testCaseProperties.getProperty("MODE")+" passed !!! ");
                    //System.out.println("Test Case 1 Passed");
                } else {
                    /// Case Fail
                    Assert.fail("TestCase testBreakConnection OF class " + NetworkDown.class.toString() + " in mode = " + testCaseProperties.getProperty("MODE") + " failed !!! Reason: The Passive server didn't remain passive after breaking the connection. New Server status for the Passive Server is " + newStatusMachine1);
                }
            }

        } catch (STFException e) {
            DRTTestLogger.log(Level.SEVERE, "test case: testBreakConnection OF class " + NetworkDown.class.toString() + " in mode = " + testCaseProperties.getProperty("MODE") + " failed !!! ", e);
            e.printStackTrace();
            Assert.fail("TestCase testBreakConnection OF class " + NetworkDown.class.toString() + " in mode = " + testCaseProperties.getProperty("MODE") + " failed !!!" + e.getMessage());
        }
        DRTTestLogger.log(DRTTestConstants.getFinishMessage("testBreakConnection", "NetworkDown:mode=" + testCaseProperties.getProperty("MODE")));
        System.out.println("-------------------");

    }


    public void testPutConnection() {
        DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testPutConnection", "NetworkDown:mode=" + testCaseProperties.getProperty("MODE")));

        TestEnvironment testEnvironment = serverStatusController.getTestEnvironment();
        machineElement1 = testEnvironment.getMachine("machine1");
        machineElement2 = testEnvironment.getMachine("machine2");
        machineElement3 = testEnvironment.getMachine("machine3");
        String statusM1Old, statusM3Old, statusM1New, statusM3New;
        try {
            statusM1Old = serverStatusController.getServerStatus(machineElement1.getAddress(), primaryRMIPort);
            statusM3Old = serverStatusController.getServerStatus(machineElement3.getAddress(), secondaryRMIPort);
            System.out.println("Machine 1 status before re-establishing the broken connection  = " + statusM1Old);
            System.out.println("Machine 3 status before re-establishing the broken connection  = " + statusM3Old);

            executionController.modifyFireWallRule(machineElement1.getAddress(), machineElement2.getAddress(), 7,dummyPort ,"D");

            executionController.modifyFireWallRule(machineElement3.getAddress(), machineElement2.getAddress(), 7,dummyPort ,"D");

            try {
                Thread.sleep(longSleep);//sleep  man sleep ....zzzzzzzzzzzzz
            } catch (InterruptedException ignore) {
            }


            executionController.modifyFireWallRule(machineElement1.getAddress(), machineElement3.getAddress(), secondaryHAPort, primaryHAPort, "D");
            executionController.modifyFireWallRule(machineElement3.getAddress(), machineElement1.getAddress(), primaryHAPort, secondaryHAPort, "D");

            try {
                Thread.sleep(longSleep);//sleep  man sleep ....zzzzzzzzzzzzz
            } catch (InterruptedException ignore) {
            }

            statusM1New = serverStatusController.getServerStatus(machineElement1.getAddress(), primaryRMIPort);
            statusM3New = serverStatusController.getServerStatus(machineElement3.getAddress(), secondaryRMIPort);
            System.out.println("Machine 1 status after re-establishing the broken connection = " + statusM1New);
            System.out.println("Machine 3 status after re-establishing the broken connection = " + statusM3New);

            if (statusM1Old.equalsIgnoreCase("waiting")) {
                if (statusM1New.equalsIgnoreCase("active")) {
                    ///Case Passed
                    //System.out.println("Test Case 2 Passed");
                } else {
                    Assert.fail("TestCase testPutConnection OF class " + NetworkDown.class.toString() + " in mode = " + testCaseProperties.getProperty("MODE") + " failed !!! Reason: The Wating server didn't switch to Active after breaking the connection. New Server status for the Waiting Server is :" + statusM1New);
                }
            }

            if (statusM3Old.equalsIgnoreCase("passive")) {
                if (statusM3New.equalsIgnoreCase("passive")) {
                    ///Case Passed
                    // Assert.("TestCase testPutConnection OF class "+NetworkDown.class.toString() + " in mode = "+testCaseProperties.getProperty("MODE")+" passed !!! ");
                    //System.out.println("Test Case 2 Passed");
                } else {
                    /// Case Fail
                    Assert.fail("TestCase testPutConnection OF class " + NetworkDown.class.toString() + " in mode = " + testCaseProperties.getProperty("MODE") + " failed !!! Reason: The Passive server didn't remain passive after breaking the connection. New Server status for the Passive Server is " + statusM3New);
                }
            }


            if (statusM3Old.equalsIgnoreCase("waiting")) {
                if (statusM3New.equalsIgnoreCase("active")) {
                    ///Case Passed
                    //System.out.println("Test Case 2 Passed");
                } else {
                    Assert.fail("TestCase testPutConnection OF class " + NetworkDown.class.toString() + " in mode = " + testCaseProperties.getProperty("MODE") + " failed !!! Reason: The Waiting server didn't switch to Active after breaking the connection. New Server status for the Waiting Server is :" + statusM3New);
                }
            }

            if (statusM1Old.equalsIgnoreCase("passive")) {
                if (statusM1New.equalsIgnoreCase("passive")) {
                    ///Case Passed
                    // Assert.("TestCase testPutConnection OF class "+NetworkDown.class.toString() + " in mode = "+testCaseProperties.getProperty("MODE")+" passed !!! ");
                    //System.out.println("Test Case 2 Passed");
                } else {
                    /// Case Fail
                    Assert.fail("TestCase testPutConnection OF class " + NetworkDown.class.toString() + " in mode = " + testCaseProperties.getProperty("MODE") + " failed !!! Reason: The Passive server didn't remain passive after breaking the connection. New Server status for the Passive Server is " + statusM1New);
                }
            }

        } catch (STFException e) {
            DRTTestLogger.log(Level.SEVERE, "test case: testPutConnection OF class " + NetworkDown.class.toString() + " in mode = " + testCaseProperties.getProperty("MODE") + " failed !!! ", e);
            e.printStackTrace();
            Assert.fail("TestCase testPutConnection OF class " + NetworkDown.class.toString() + " in mode = " + testCaseProperties.getProperty("MODE") + " failed !!!" + e.getMessage());
        }
        DRTTestLogger.log(DRTTestConstants.getFinishMessage("testPutConnection", "NetworkDown:mode=" + testCaseProperties.getProperty("MODE")));
        System.out.println("-------------------");

    }


    protected void tearDown() throws java.lang.Exception {
        super.tearDown();
        if (machineElement1 != null)
            executionController.flushFilters(machineElement1.getAddress());
        if (machineElement3 != null)
            executionController.flushFilters(machineElement1.getAddress());

    }

}
