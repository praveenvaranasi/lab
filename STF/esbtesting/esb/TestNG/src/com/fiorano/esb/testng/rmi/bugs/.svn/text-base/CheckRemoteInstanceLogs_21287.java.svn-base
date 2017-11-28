package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IDebugger;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.*;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: anurag
 * Date: 4/12/12
 * Time: 2:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class CheckRemoteInstanceLogs_21287 extends AbstractTestNG{
    private IEventProcessManager eventProcessManager;
       private IDebugger debugger;
       private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
       private String appName = "BUG_21287";
       private TestEnvironmentConfig testEnvConfig;
       private SampleEventProcessListener epListener = null;
       private byte[] sentFile;
       String Location = System.getProperty("FIORANO_HOME");
       private float appVersion = 1.0f;
         @Test(groups = "Bugs", alwaysRun = true)
      public void initSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        this.debugger=rmiClient.getDebugger();
        this.testEnvConfig = ESBTestHarness.getTestEnvConfig();

        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group Bugs. as eventProcessManager is not set.");
        }
    }
      @Test(groups = "Bugs", description = "exported eventprocess logs should contain remote instance logs also. - bug 21287 ", dependsOnMethods = "initSetup", alwaysRun = true)
    public void Test_Bug21287Launch() {
         Logger.LogMethodOrder(Logger.getOrderMessage("Test_Bug21287Launch", "Test_Bug21287"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("Test_Bug21287Launch", "Test_Bug21287"));


        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
           // e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_Bug21287Launch", "Test_Bug21287"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }


     try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_Bug21287Launch", "Test_Bug21287"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_Bug21287Launch", "Test_Bug21287"), e);
            Assert.fail("Failed to do ep listener!", e);
        }

        try {
             deployEventProcess("Bug_21287_Remote.zip");
             deployEventProcess("Bug_21287.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_Bug21287Launch", "Test_Bug21287"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_Bug21287Launch", "Test_Bug21287"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_Bug21287Launch", "Test_Bug21287"), e);
            Assert.fail("Failed to do CRC!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_Bug21287Launch", "Test_Bug21287"), e);
            Assert.fail("Failed to do CRC!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_Bug21287Launch", "Test_Bug21287"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_Bug21287Launch", "Test_Bug21287"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

          // Exporting application logs

            byte Result[];
            long index=0;
          try{
               Result = eventProcessManager.exportApplicationLogs(appName, (float) 1.0,index);
               File file1 = new File(Location+fsc+"Bug_21287"+".zip");
               file1.createNewFile();
               BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file1));

               while(Result != null) {
                    output.write(Result);
                    index=file1.length();
                    Result = eventProcessManager.exportApplicationLogs(appName,(float)1.0,index);
               }

              //Reading contents of zipped file and searching whether remote logs exists

              FileInputStream fs = new FileInputStream(Location+fsc+"Bug_21287"+".zip");
              ZipInputStream zis = new ZipInputStream(fs);
              ZipEntry zE;
                 int i=0;
              while((zE=zis.getNextEntry())!=null){
                  System.out.println(zE.getName());
                  if(zE.getName().equals("BUG_21287_REMOTE.chat21.OUT"))
                  {
                      System.out.println("Logs for remote instance present__Testcase pass");
                      i = 1;
                      break;
                  }

                  zis.closeEntry();
                }

                if( i != 1)
                {System.out.println("Logs for remote instance present__Testcase failure");}
                zis.close();

                //Deleting the zipp log files after test case gets over
                Boolean deleted = file1.delete();
                 if(deleted == true)
                    System.out.println(" Zip log File deleted");
                output.close();
               } catch(Exception e)
                    {e.printStackTrace();}

     }

@Test(groups = "Bugs", dependsOnMethods = "Test_Bug21287Launch", alwaysRun = true)
       public void stopAndDeleteApplication() {
            Logger.LogMethodOrder(Logger.getOrderMessage("stopAndDeleteApplication", "Test_Bug21287"));
            try {
                eventProcessManager.stopEventProcess(appName,appVersion);
                eventProcessManager.stopEventProcess("BUG_21287_REMOTE",appVersion);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
                eventProcessManager.deleteEventProcess(appName, appVersion, true, false);
                eventProcessManager.deleteEventProcess("BUG_21287_REMOTE", appVersion, true, false);
            } catch (RemoteException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "Test_Bug21287"), e);
                Assert.fail("Failed to do stop/delete EP as part of cleanup", e);
            } catch (ServiceException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "Test_Bug21287"), e);
                Assert.fail("Failed to do stop/delete EP as part of cleanup", e);
            }
            Logger.Log(Level.SEVERE, Logger.getFinishMessage("stopAndDeleteApplication", "Test_Bug21287"));
    }
         @Test(enabled = false)
    private void deployEventProcess(String zipName) throws IOException, ServiceException {
           BufferedInputStream bis = null;
           TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
           String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
           File TestingHome = new File(home);
           bis = new BufferedInputStream(new FileInputStream(TestingHome.getAbsolutePath() + File.separator + "esb" + File.separator + "TestNG" +
                   File.separator + "resources" + File.separator + zipName));

           boolean completed = false;
           byte result[];
           while (!completed) {
               byte[] temp = new byte[1024];
               int readcount = 0;
               readcount = bis.read(temp);

               if (readcount < 0) {
                   completed = true;
                   readcount = 0;
               }
               result = new byte[readcount];
               System.arraycopy(temp, 0, result, 0, readcount);

               // it will deploy the event process if the correct byte array is send; else gives an error
               this.eventProcessManager.deployEventProcess(result, completed);
           }
       }


}

