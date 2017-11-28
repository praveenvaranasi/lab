package com.fiorano.esb.junit.ha;

import com.fiorano.stf.test.core.DRTTestCase;
import com.fiorano.stf.test.core.TestCaseElement;
import com.fiorano.stf.test.core.TestEnvironment;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.ServerStatusController;

import java.util.ArrayList;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created by IntelliJ IDEA.
 * User: nagesh
 * Date: Jan 9, 2009
 * Time: 4:39:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class DHLHATest extends DRTTestCase {
    private TestEnvironment testEnvironment;

    private final int FES_PRIMARY_RMI_PORT = 2047;
    private final int FES_SECONDARY_RMI_PORT = 2048;
    private final int FPS_PRIMARY_RMI_PORT = 2067;
    private final int FPS_SECONDARY_RMI_PORT = 2068;

    private final int FES_PRIMARY_PORT = 1747;
    private final int FES_SECONDARY_PORT = 1748;
    private final int FPS_PRIMARY_PORT = 1767;
    private final int FPS_SECONDARY_PORT = 1768;

    public DHLHATest(TestCaseElement testCaseConfig, ServerStatusController controller) {
        super(testCaseConfig, controller);
    }

    public DHLHATest(String testCaseName) {
        super(testCaseName);
    }

    public DHLHATest(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
    }


    public void testFESshutdown() throws Exception {
        System.out.println("Started the Execution of the TestCase::" + getName());

       /* try {
            int count = 10;
            System.out.println("Testing for Primary FES shutdown...");
            ExecutionController remoteProxy = ExecutionController.getInstance();
            remoteProxy.shutdownServerOnRemote(serverStatusController.getPrimaryFESAddress(), TestEnvironmentConstants.FES, "fes_primary", true);

            while (getPrimaryFESstatus() != null && count > 0) {
                count--;
                sleep(5000);
            }
            Assert.assertNull(getPrimaryFESstatus());
            System.out.println("FES_Primary : DEAD ");
            count = 10;

            while (getSecondaryFESstatus().equalsIgnoreCase("PASSIVE") || getSecondaryFESstatus().equalsIgnoreCase("") && count > 0) {
                count--;
                sleep(5000);
            }

            Assert.assertEquals(getSecondaryFESstatus(), "STANDALONE");
            System.out.println("FES_Secondary : STANDALONE");

            *//*remoteProxy.startServerOnRemote(serverStatusController.getPrimaryFESAddress(), TestEnvironmentConstants.FES, "HA_EnterpriseServer/FES_Primary");
            count = 10;*//*
            System.out.println("Testing for Secondary FES shutdown...");
            remoteProxy.shutdownServerOnRemote(serverStatusController.getSecondaryFESAddress(), TestEnvironmentConstants.FES, "fes_secondary", false);

            while (getSecondaryFESstatus() != null && count > 0) {
                count--;
                sleep(5000);
            }
            Assert.assertNull(getSecondaryFESstatus());
            System.out.println("FES_Secondary : DEAD ");
            count = 10;

            while (getPrimaryFESstatus().equalsIgnoreCase("PASSIVE") || getPrimaryFESstatus().equalsIgnoreCase("") && count > 0) {
                count--;
                sleep(5000);
            }
            Assert.assertEquals(getPrimaryFESstatus(), "STANDALONE");
            System.out.println("FES_Primary : STANDALONE");

            *//*System.out.println("Bringing the servers to normal state....");
            remoteProxy.startServerOnRemote(serverStatusController.getSecondaryFESAddress(), TestEnvironmentConstants.FES, "HA_EnterpriseServer/FES_Secondary");
*//*
            Assert.assertEquals(getPrimaryFPSstatus(), "ACTIVE");
            System.out.println("FPS_Primary : ACTIVE");
            Assert.assertEquals(getSecondaryFPSstatus(), "PASSIVE");
            System.out.println("FPS_Secondary : PASSIVE");
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase " + getName() + ": FAILED >> " + ex.getMessage(), false);
        }*/
    }


    public void testFPSshutdown() throws Exception {
        System.out.println("Started the Execution of the TestCase::" + getName());

        try {
            int count = 10;
            System.out.println("Testing for Primary FPS shutdown...");
            ExecutionController remoteProxy = ExecutionController.getInstance();
           /* remoteProxy.shutdownServerOnRemote(serverStatusController.getPrimaryFPSAddress(), TestEnvironmentConstants.FPS, "fps_ha");*/

            while (getPrimaryFPSstatus() != null && count > 0) {
                count--;
                sleep(5000);
            }
            Assert.assertNull(getPrimaryFPSstatus());
            System.out.println("FPS_Primary : DEAD ");
            count = 10;

            while (getSecondaryFPSstatus().equalsIgnoreCase("PASSIVE") || getSecondaryFPSstatus().equalsIgnoreCase("") && count > 0) {
                count--;
                sleep(5000);
            }
            Assert.assertEquals(getSecondaryFPSstatus(), "STANDALONE");
            System.out.println("FPS_Secondary : STANDALONE");

            /*remoteProxy.startServerOnRemote(serverStatusController.getPrimaryFPSAddress(), TestEnvironmentConstants.FPS, "FPS_HA/FPS_Primary");
            count = 10;*/
            System.out.println("Testing for Secondary FPS shutdown...");
/*
            remoteProxy.shutdownServerOnRemote(serverStatusController.getSecondaryFPSAddress(), TestEnvironmentConstants.FPS, "fps_ha");
*/

            while (getSecondaryFPSstatus() != null && count > 0) {
                count--;
                sleep(5000);
            }
            Assert.assertNull(getSecondaryFPSstatus());
            System.out.println("FPS_Secondary : DEAD ");
            count = 10;

            while (getPrimaryFPSstatus().equalsIgnoreCase("PASSIVE") || getPrimaryFPSstatus().equalsIgnoreCase("") && count > 0) {
                count--;
                sleep(5000);
            }
            Assert.assertEquals(getPrimaryFPSstatus(), "STANDALONE");
            System.out.println("FPS_Primary : STANDALONE");

           /* System.out.println("Bringing the servers to normal state....");
            remoteProxy.startServerOnRemote(serverStatusController.getSecondaryFPSAddress(), TestEnvironmentConstants.FPS, "FPS_HA/FPS_Secondary");
*/
            Assert.assertEquals(getPrimaryFESstatus(), "ACTIVE");
            System.out.println("FES_Primary : ACTIVE");
            Assert.assertEquals(getSecondaryFESstatus(), "PASSIVE");
            System.out.println("FES_Secondary : PASSIVE");
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase " + getName() + ": FAILED >> " + ex.getMessage(), false);
        }
    }

    public void testNetworkdown() throws Exception {
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            ExecutionController remoteProxy = ExecutionController.getInstance();
            String primaryFESAddress = serverStatusController.getPrimaryFESAddress();
            String secondaryFESAddress = serverStatusController.getSecondaryFESAddress();

            String primaryFPSAddress = serverStatusController.getPrimaryFPSAddress();
            String secondaryFPSAddress = serverStatusController.getSecondaryFPSAddress();

            String fesLockfileAddress = serverStatusController.getFesLockFileMachineAddress();
            String fpsLockfileAddress = serverStatusController.getFpsLockFileMachineAddress();

            System.out.println("Blocking network between two nodes");
            // Primary Server
            remoteProxy.modifyFilters(primaryFESAddress, secondaryFESAddress, FES_PRIMARY_PORT, "A");
            remoteProxy.modifyFilters(primaryFPSAddress, secondaryFPSAddress, FPS_PRIMARY_PORT, "A");
            // Secondary Server
            remoteProxy.modifyFilters(secondaryFESAddress, primaryFESAddress, FES_SECONDARY_PORT, "A");
            remoteProxy.modifyFilters(secondaryFPSAddress, primaryFPSAddress, FPS_SECONDARY_PORT, "A");
            sleep(60000);

            System.out.println("Status of servers after network between nodes A & B is out...");
            System.out.println("FES_Primary : " + getPrimaryFESstatus() +"FES_Secondary : " + getSecondaryFESstatus());
            System.out.println("FPS_Primary : " + getPrimaryFPSstatus() +"FPS_Secondary : " + getSecondaryFPSstatus());

            System.out.println("Testing network to Gateway server down..");
            //Quorum Server
            remoteProxy.modifyGateway(primaryFESAddress, fesLockfileAddress, "A");
            remoteProxy.modifyGateway(primaryFPSAddress, fpsLockfileAddress, "A");
            sleep(50000);

            System.out.println("Status of servers after network between nodes A, B & Gateway server is out...");
            System.out.println("FES_Primary : " + getPrimaryFESstatus() +"FES_Secondary : " + getSecondaryFESstatus());
            System.out.println("FPS_Primary : " + getPrimaryFPSstatus() +"FPS_Secondary : " + getSecondaryFPSstatus());

            System.out.println("Re-enabling network between nodes");
            // Primary Server
            remoteProxy.modifyFilters(primaryFESAddress, secondaryFESAddress, FES_PRIMARY_PORT, "D");
            remoteProxy.modifyFilters(primaryFPSAddress, secondaryFPSAddress, FPS_PRIMARY_PORT, "D");
            // Secondary Server
            remoteProxy.modifyFilters(secondaryFESAddress, primaryFESAddress, FES_SECONDARY_PORT, "D");
            remoteProxy.modifyFilters(secondaryFPSAddress, primaryFPSAddress, FPS_SECONDARY_PORT, "D");
            //Quorum Server
            remoteProxy.modifyGateway(primaryFESAddress, fesLockfileAddress, "D");
            remoteProxy.modifyGateway(primaryFPSAddress, fpsLockfileAddress, "D");
            sleep(30000);

            Assert.assertEquals(getPrimaryFESstatus(), "ACTIVE");
            System.out.println("FES_Primary : ACTIVE");
            Assert.assertEquals(getSecondaryFESstatus(), "PASSIVE");
            System.out.println("FES_Secondary : PASSIVE");
            Assert.assertEquals(getPrimaryFPSstatus(), "ACTIVE");
            System.out.println("FPS_Primary : ACTIVE");
            Assert.assertEquals(getSecondaryFPSstatus(), "PASSIVE");
            System.out.println("FPS_Secondary : PASSIVE");

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase " + getName() + ": FAILED >> " + ex.getMessage(), false);
        }
    }

    private String getPrimaryFESstatus() throws Exception {
        return serverStatusController.getServerStatus(serverStatusController.getPrimaryFESAddress(), FES_PRIMARY_RMI_PORT);
    }

    private String getSecondaryFESstatus() throws Exception {
        return serverStatusController.getServerStatus(serverStatusController.getSecondaryFESAddress(), FES_SECONDARY_RMI_PORT);
    }

    private String getPrimaryFPSstatus() throws Exception {
        return serverStatusController.getServerStatus(serverStatusController.getPrimaryFPSAddress(), FPS_PRIMARY_RMI_PORT);
    }

    private String getSecondaryFPSstatus() throws Exception {
        return serverStatusController.getServerStatus(serverStatusController.getSecondaryFPSAddress(), FPS_SECONDARY_RMI_PORT);
    }


    private void sleep(long interval) {
        try {
            Thread.sleep(interval);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static Test suite() {
        return new TestSuite(DHLHATest.class);
    }

    public static ArrayList getOrder() {

        ArrayList methodsOrder = new ArrayList();

        methodsOrder.add("testFESshutdown");
        methodsOrder.add("testFPSshutdown");
        methodsOrder.add("testNetworkdown");

        return methodsOrder;
    }


}
