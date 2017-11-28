package com.fiorano.esb.testng.rmi.ha;

import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.TestEnvironment;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/5/11
 * Time: 6:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestDHLHA extends AbstractTestNG{

    private final int FES_PRIMARY_RMI_PORT = 2047;
    private final int FES_SECONDARY_RMI_PORT = 2048;
    private final int FPS_PRIMARY_RMI_PORT = 2067;
    private final int FPS_SECONDARY_RMI_PORT = 2068;

    private final int FES_PRIMARY_PORT = 1747;
    private final int FES_SECONDARY_PORT = 1748;
    private final int FPS_PRIMARY_PORT = 1767;
    private final int FPS_SECONDARY_PORT = 1768;
    private ServerStatusController serverStatusController;

    @Test(groups = "DHLAHATest", alwaysRun = true)
    void startSetUp(){
       serverStatusController = ServerStatusController.getInstance();
    }

    @Test(groups = "DHLAHATest", dependsOnMethods = "startSetUp", alwaysRun = true)
    void testFPSShutdown() throws Exception {
        System.out.println("Started the Execution of the TestCase::" + getName());

        try {
            int count = 10;
            System.out.println("Testing for Primary FPS shutdown...");
            ExecutionController remoteProxy = ExecutionController.getInstance();
            remoteProxy.shutdownServerOnRemote(serverStatusController.getPrimaryFPSAddress(), TestEnvironmentConstants.FPS, "haprofile1/primary", false, true);

            while (getPrimaryFPSStatus() != null && count > 0) {
                count--;
                sleep(5000);
            }
            Assert.assertNull(getPrimaryFPSStatus());
            System.out.println("FPS_Primary : DEAD ");
            count = 10;

            while (getSecondaryFPSStatus().equalsIgnoreCase("PASSIVE") || getSecondaryFPSStatus().equalsIgnoreCase("") && count > 0) {
                count--;
                sleep(5000);
            }
            Assert.assertEquals(getSecondaryFPSStatus(), "STANDALONE");
            System.out.println("FPS_Secondary : STANDALONE");

            remoteProxy.startServerOnRemote(serverStatusController.getPrimaryFPSAddress(), TestEnvironmentConstants.FPS, "haprofile1/primary", false, true);
            count = 10;
            while (getPrimaryFESStatus() != null && count > 0) {
                count--;
                sleep(5000);
            }
            System.out.println("Testing for Secondary FPS shutdown...");

            remoteProxy.shutdownServerOnRemote(serverStatusController.getPrimaryFPSAddress(), TestEnvironmentConstants.FPS, "haprofile1/secondary", false, true);
            count=10;
            while (getSecondaryFPSStatus() != null && count > 0) {
                count--;
                sleep(5000);
            }
            Assert.assertNull(getSecondaryFPSStatus());
            System.out.println("FPS_Secondary : DEAD ");
            count = 10;

            while (getPrimaryFPSStatus().equalsIgnoreCase("PASSIVE") || getPrimaryFPSStatus().equalsIgnoreCase("") && count > 0) {
                count--;
                sleep(5000);
            }
            Assert.assertEquals(getPrimaryFPSStatus(), "STANDALONE");
            System.out.println("FPS_Primary : STANDALONE");

            System.out.println("Bringing the servers to normal state....");
            remoteProxy.startServerOnRemote(serverStatusController.getPrimaryFPSAddress(), TestEnvironmentConstants.FPS, "haprofile1/secondary", false, true);
            Assert.assertEquals(getPrimaryFESStatus(), "ACTIVE");
            System.out.println("FES_Primary : ACTIVE");
            Assert.assertEquals(getSecondaryFESStatus(), "PASSIVE");
            System.out.println("FES_Secondary : PASSIVE");
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase " + getName() + ": FAILED >> " + ex.getMessage());
        }
    }

    @Test(groups = "DHLHATest", alwaysRun = true, dependsOnMethods = "testFPSShutdown")
    void testNetworkDown() throws Exception {
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
            remoteProxy.modifyFireWallRule(primaryFESAddress, secondaryFESAddress, FES_SECONDARY_PORT, FES_PRIMARY_PORT, "A");
            remoteProxy.modifyFireWallRule(primaryFPSAddress, secondaryFPSAddress, FPS_SECONDARY_PORT, FPS_PRIMARY_PORT, "A");
            // Secondary Server
            remoteProxy.modifyFireWallRule(secondaryFESAddress, primaryFESAddress, FES_PRIMARY_PORT, FES_SECONDARY_PORT, "A");
            remoteProxy.modifyFireWallRule(secondaryFPSAddress, primaryFPSAddress, FPS_SECONDARY_PORT, FPS_SECONDARY_PORT, "A");
            sleep(60000);

            System.out.println("Status of servers after network between nodes A & B is out...");
            System.out.println("FES_Primary : " + getPrimaryFESStatus() +" FES_Secondary : " + getSecondaryFESStatus());
            System.out.println("FPS_Primary : " + getPrimaryFPSStatus() +" FPS_Secondary : " + getSecondaryFPSStatus());

            System.out.println("Testing network to Gateway server down..");
            //Quorum Server
            remoteProxy.modifyGateway(primaryFESAddress, fesLockfileAddress, "A");
            remoteProxy.modifyGateway(primaryFPSAddress, fpsLockfileAddress, "A");
            sleep(50000);

            System.out.println("Status of servers after network between nodes A, B & Gateway server is out...");
            System.out.println("FES_Primary : " + getPrimaryFESStatus() +" FES_Secondary : " + getSecondaryFESStatus());
            System.out.println("FPS_Primary : " + getPrimaryFPSStatus() +" FPS_Secondary : " + getSecondaryFPSStatus());

            System.out.println("Re-enabling network between nodes");
            // Primary Server
            remoteProxy.modifyFireWallRule(primaryFESAddress, secondaryFESAddress, FES_SECONDARY_PORT, FES_PRIMARY_PORT, "D");
            remoteProxy.modifyFireWallRule(primaryFPSAddress, secondaryFPSAddress, FPS_SECONDARY_PORT, FPS_PRIMARY_PORT, "D");
            // Secondary Server
            remoteProxy.modifyFireWallRule(secondaryFESAddress, primaryFESAddress, FES_PRIMARY_PORT, FES_SECONDARY_PORT, "D");
            remoteProxy.modifyFireWallRule(secondaryFPSAddress, primaryFPSAddress, FPS_PRIMARY_PORT, FPS_SECONDARY_PORT, "D");
            //Quorum Server
            remoteProxy.modifyGateway(primaryFESAddress, fesLockfileAddress, "D");
            remoteProxy.modifyGateway(primaryFPSAddress, fpsLockfileAddress, "D");
            sleep(30000);

            Assert.assertEquals(getPrimaryFESStatus(), "ACTIVE");
            System.out.println("FES_Primary : ACTIVE");
            Assert.assertEquals(getSecondaryFESStatus(), "PASSIVE");
            System.out.println("FES_Secondary : PASSIVE");
            Assert.assertEquals(getPrimaryFPSStatus(), "ACTIVE");
            System.out.println("FPS_Primary : ACTIVE");
            Assert.assertEquals(getSecondaryFPSStatus(), "PASSIVE");
            System.out.println("FPS_Secondary : PASSIVE");

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase " + getName() + ": FAILED >> " + ex.getMessage());
        }
    }

    @Test(groups = "DHLHATest", alwaysRun = true, dependsOnMethods = "testNetworkDown")
    protected void tearDown() throws Exception{
        ExecutionController executionController = ExecutionController.getInstance();
        executionController.flushFilters(serverStatusController.getPrimaryFESAddress());
        executionController.flushFilters(serverStatusController.getSecondaryFESAddress());
        executionController.flushFilters(serverStatusController.getPrimaryFPSAddress());
        executionController.flushFilters(serverStatusController.getSecondaryFPSAddress());
        executionController.flushFilters(serverStatusController.getFesLockFileMachineAddress());
        executionController.flushFilters(serverStatusController.getFpsLockFileMachineAddress());
    }





    private String getPrimaryFESStatus() throws Exception {
        return serverStatusController.getServerStatus(serverStatusController.getPrimaryFESAddress(), FES_PRIMARY_RMI_PORT);
    }

    private String getSecondaryFESStatus() throws Exception {
        return serverStatusController.getServerStatus(serverStatusController.getSecondaryFESAddress(), FES_SECONDARY_RMI_PORT);
    }

    private String getPrimaryFPSStatus() throws Exception {
        return serverStatusController.getServerStatus(serverStatusController.getPrimaryFPSAddress(), FPS_PRIMARY_RMI_PORT);
    }

    private String getSecondaryFPSStatus() throws Exception {
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

}
