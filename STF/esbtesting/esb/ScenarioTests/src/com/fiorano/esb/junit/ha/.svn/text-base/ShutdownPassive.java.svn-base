package com.fiorano.esb.junit.ha;

import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.DRTTestCase;
import com.fiorano.stf.test.core.MachineElement;
import com.fiorano.stf.consts.DRTTestConstants;
import com.fiorano.stf.logger.DRTTestLogger;
import com.fiorano.stf.test.core.TestCaseElement;
import com.fiorano.stf.test.core.TestEnvironment;
import junit.framework.Assert;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: prateek
 * Date: Nov 2, 2010
 * Time: 3:30:00 PM
 * To change this template use File | Settings | File Templates.
 *
     *    1. Run ha server on machine1 & machine3. (Machine 1 is primary and Machine 3 is secondary). Both are isWrapper true mode.
     *    2. Run Lock machine on machine2.
     *    3. Get Status of Machine1 and Machine3.
     *    4. Check if one if Active and Other is Passive.
     *    5. Shutdown the Active Server whichever it is.
     *    6. Wait for sometime.
     *    7. Check the status of the server which was Passive ealier.
     *    8. It should be standalone. Fail the test case if it is not standalone.
     *    9. Restart the server which was shutdown in the process.
 */
public class ShutdownPassive extends DRTTestCase {
ExecutionController executionController;
private MachineElement machineElement3;
private MachineElement machineElement1;
private MachineElement machineElement2;
private int primaryRMIPort;
private int secondaryRMIPort;
private int primaryHAPort;
private int secondaryHAPort;
protected String MODE;
    private static int shortSleep=60000;
    private static int midSleep=300000;
    private static int longSleep=360000;

    public ShutdownPassive(String testCaseName) {
        super(testCaseName);
        serverStatusController =ServerStatusController.getInstance();
        executionController = ExecutionController.getInstance();

    }

    public ShutdownPassive(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
        serverStatusController = ServerStatusController.getInstance();
        executionController = ExecutionController.getInstance();
    }

    public ShutdownPassive(TestCaseElement testCaseConfig, ServerStatusController serverStatusController) {
        super(testCaseConfig, serverStatusController);
        serverStatusController =ServerStatusController.getInstance();
        executionController = ExecutionController.getInstance();
    }

    @Override
    protected void setUp() throws Exception {
       super.setUp();
       this.setPrimaryRMIPort(testCaseProperties.getProperty("PRIMARY_RMI_PORT"));
       this.setSecondaryRMIPort(testCaseProperties.getProperty("SECONDARY_RMI_PORT"));
       this.setPrimaryHAPort(testCaseProperties.getProperty("PRIMARY_HA_PORT"));
       this.setSecondaryHAPort(testCaseProperties.getProperty("SECONDARY_HA_PORT"));
       this.setMode(testCaseProperties.getProperty("MODE"));
    }
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
         this.MODE=property;
    }

    public static ArrayList getOrder() {

        ArrayList methodsOrder = new ArrayList();

        methodsOrder.add("testShutDownPassive");
        //methodsOrder.add("testPutConnection");
        return methodsOrder;
    }

    /**

     */
    public void testShutDownPassive(){
        DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testShutDownPassive", "ShutdownPassive:mode="+testCaseProperties.getProperty("MODE")));
        TestEnvironment testEnvironment = serverStatusController.getTestEnvironment();
        machineElement1 = testEnvironment.getMachine("machine1");
        machineElement3 = testEnvironment.getMachine("machine3");
        String oldStatusMachine1, oldStatusMachine3, newStatusMachine1, newStatusMachine3;
        
        try {
            try {
                Thread.sleep(shortSleep);//sleep  man sleep ....zzzzzzzzzzzzz
            } catch (InterruptedException ignore)
            {
            }
            oldStatusMachine1 = serverStatusController.getServerStatus(machineElement1.getAddress(),primaryRMIPort);
            oldStatusMachine3 = serverStatusController.getServerStatus(machineElement3.getAddress(),secondaryRMIPort);

			System.out.println("Machine 1 status before shutting down the passive server = "+oldStatusMachine1);
            System.out.println("Machine 3 status before shutting down the passive server = "+oldStatusMachine3);
            
			if (oldStatusMachine1 == null || oldStatusMachine3 == null)
            {
                Assert.fail("TestCase ShutdownPassive OF class "+ ShutdownPassive.class.toString() + " in mode = "+testCaseProperties.getProperty("MODE")+" failed !!! Reason: Starting Environment not Correct. One of Server is not responding.");
            }


            boolean m1IsActive = (oldStatusMachine1.equalsIgnoreCase("active"));
            boolean m1IsPassive= (oldStatusMachine1.equalsIgnoreCase("passive"));

            boolean m3IsActive = (oldStatusMachine3.equalsIgnoreCase("active"));
            boolean m3IsPassive= (oldStatusMachine3.equalsIgnoreCase("passive"));


            if (!((m1IsActive && m3IsPassive)||(m1IsPassive && m3IsActive)))
            {
                Assert.fail("TestCase ShutdownPassive OF class "+ ShutdownPassive.class.toString() + " in mode = "+testCaseProperties.getProperty("MODE")+" failed !!! Reason: Starting Environment not Correct. One Server should in Active State and other should be in Passive State.");
            }



            if (m1IsPassive)
            {
                executionController.shutdownServerOnRemote(machineElement1.getAddress(), this.MODE,"haprofile1/primary",true,true);
                try
                {
                    Thread.sleep(longSleep);
                }
                catch (InterruptedException ignore){}
                
                newStatusMachine3 = serverStatusController.getServerStatus(machineElement3.getAddress(),secondaryRMIPort);
                System.out.println("Machine 3 status after shutting down Passive Server at machine 1 = "+newStatusMachine3);

                if (!newStatusMachine3.equalsIgnoreCase("standalone"))
                {
                      Assert.fail("TestCase ShutdownPassive of class "+ ShutdownPassive.class.toString() + " in mode = "+testCaseProperties.getProperty("MODE")+" failed !!! Reason: The Active server didn't go to Standalone state after shutting down the Passive Server");

                }
                executionController.startServerOnRemote(machineElement1.getAddress(), this.MODE, "haprofile1/primary",true,true);
                try
                {
                    Thread.sleep(longSleep);
                }
                catch (InterruptedException ignore){}
                newStatusMachine1 = serverStatusController.getServerStatus(machineElement1.getAddress(),primaryRMIPort);
                System.out.println("Machine 1 status after restarting it back= "+newStatusMachine1);
                if (!newStatusMachine1.equalsIgnoreCase("passive"))
                {
                      Assert.fail("TestCase ShutdownPassive of class "+ ShutdownPassive.class.toString() + " in mode = "+testCaseProperties.getProperty("MODE")+" failed !!! Reason: The server which was shut down was started again but it didn't switch to passive");
                }
            }
            else
            {
                executionController.shutdownServerOnRemote(machineElement3.getAddress(), this.MODE,"haprofile1/secondary",true,true);
                try
                {
                    Thread.sleep(longSleep);
                }
                catch (InterruptedException ignore){}
                newStatusMachine1 = serverStatusController.getServerStatus(machineElement1.getAddress(),primaryRMIPort);
                System.out.println("Machine 1 status after shutting down Passive server at machine 3 = "+newStatusMachine1);

                if (!newStatusMachine1.equalsIgnoreCase("standalone"))
                {
                      Assert.fail("TestCase ShutdownPassive of class "+ ShutdownPassive.class.toString() + " in mode = "+testCaseProperties.getProperty("MODE")+" failed !!! Reason: The Active server didn't go to Standalone state after shutting down the Passive Server");
                }
                executionController.startServerOnRemote(machineElement3.getAddress(), this.MODE,"haprofile1/secondary",true,true);
                try
                {
                    Thread.sleep(longSleep);
                }
                catch (InterruptedException ignore){}
                newStatusMachine3 = serverStatusController.getServerStatus(machineElement3.getAddress(),secondaryRMIPort);
                System.out.println("Machine 3 status after restarting it back= "+newStatusMachine3);

                if (!newStatusMachine3.equalsIgnoreCase("passive"))
                {
                      Assert.fail("TestCase ShutdownPassive of class "+ ShutdownPassive.class.toString() + " in mode = "+testCaseProperties.getProperty("MODE")+" failed !!! Reason: The server which was shut down was started again but it didn't switch to passive");
                }

            }

        } catch (STFException e) {
             DRTTestLogger.log(Level.SEVERE,"test case: ShutdownPassive OF class "+ ShutdownPassive.class.toString() + " in mode = "+testCaseProperties.getProperty("MODE")+" failed !!! ",e);
            e.printStackTrace();
            Assert.fail("TestCase ShutdownPassive OF class "+ ShutdownPassive.class.toString() + " in mode = "+testCaseProperties.getProperty("MODE")+" failed !!!"  + e.getMessage());
        }
        DRTTestLogger.log(DRTTestConstants.getFinishMessage("testShutDownPassive", "ShutdownPassive:mode="+testCaseProperties.getProperty("MODE")));
        System.out.println("-------------------");

    }


    protected void tearDown() throws java.lang.Exception{
             super.tearDown();


    }

}