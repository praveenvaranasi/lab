package com.fiorano.esb.testng.rmi.bugs;
import com.fiorano.esb.server.api.*;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.security.Principal;

import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.remote.JMXClient;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.management.ObjectName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: phani
 * Date: 2/26/13
 * Time: 2:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestDestinationsNotGettingDeleted22424 extends AbstractTestNG{
    private IEventProcessManager eventProcessManager;
    private SampleEventProcessListener epListener = null;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "TEST_BUG_22424";
    String s;
    private float appVersion = 1.0f;
    private ObjectName objName5 ;
    private JMXClient esb = null;
    private List<PortInstanceMetaData> Ports = new List<PortInstanceMetaData>() {
        public int size() {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public boolean isEmpty() {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public boolean contains(Object o) {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public Iterator<PortInstanceMetaData> iterator() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public Object[] toArray() {
            return new Object[0];  //To change body of implemented methods use File | Settings | File Templates.
        }

        public <T> T[] toArray(T[] a) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public boolean add(PortInstanceMetaData portInstanceMetaData) {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public boolean remove(Object o) {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public boolean containsAll(Collection<?> c) {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public boolean addAll(Collection<? extends PortInstanceMetaData> c) {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public boolean addAll(int index, Collection<? extends PortInstanceMetaData> c) {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public boolean removeAll(Collection<?> c) {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public boolean retainAll(Collection<?> c) {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public void clear() {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public PortInstanceMetaData get(int index) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public PortInstanceMetaData set(int index, PortInstanceMetaData element) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public void add(int index, PortInstanceMetaData element) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public PortInstanceMetaData remove(int index) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public int indexOf(Object o) {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public int lastIndexOf(Object o) {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public ListIterator<PortInstanceMetaData> listIterator() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public ListIterator<PortInstanceMetaData> listIterator(int index) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public List<PortInstanceMetaData> subList(int fromIndex, int toIndex) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    };
    @Test(groups = "Bugs", alwaysRun = true)
    public void initSetup(){
        try{
            eventProcessManager = rmiClient.getEventProcessManager();
            esb = JMXClient.getInstance();
            objName5 = new ObjectName("Fiorano.Esb.Webconsole.Controller:ServiceType=WebconsoleController,Name=WebconsoleController");
        }catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("initSetup", "TestDestinationsNotGettingDeleted22424"), e);
        }
    }
    @Test(groups = "Bugs", description = "Destinations not getting deleted after stopping the Eventprocess in specific scenario.", dependsOnMethods = "initSetup", alwaysRun = true)

    public void TestDestinationsNotGettingDeleted22424Launch() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestDestinationsNotGettingDeleted22424Launch", "TestDestinationsNotGettingDeleted22424"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("TestDestinationsNotGettingDeleted22424", "TestDestinationsNotGettingDeleted22424"));


        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDestinationsNotGettingDeleted22424Launch", "TestDestinationsNotGettingDeleted22424"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }
        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDestinationsNotGettingDeleted22424Launch", "TestDestinationsNotGettingDeleted22424"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDestinationsNotGettingDeleted22424Launch", "TestDestinationsNotGettingDeleted22424"), e);
            Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("Test_Bug_22424.zip");

        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDestinationsNotGettingDeleted22424Launch", "TestDestinationsNotGettingDeleted22424"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDestinationsNotGettingDeleted22424Launch", "TestDestinationsNotGettingDeleted22424"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }
        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDestinationsNotGettingDeleted22424Launch", "TestDestinationsNotGettingDeleted22424"), e);
            Assert.fail("Failed to do CRC!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDestinationsNotGettingDeleted22424Launch", "TestDestinationsNotGettingDeleted22424"), e);
            Assert.fail("Failed to do CRC!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDestinationsNotGettingDeleted22424Launch", "TestDestinationsNotGettingDeleted22424"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDestinationsNotGettingDeleted22424Launch", "TestDestinationsNotGettingDeleted22424"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
        }
        //clear map
        //eventStore.clear();
        /*try {
            eventProcessManager.startAllServices(appName, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "2");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_Bug_22424Launch", "Test_Bug_22424"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_Bug_22424Launch", "Test_Bug_22424"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }*/


    }
    @Test(groups = "Bugs", description = "Destinations not getting deleted after stopping the Eventprocess in specific scenario.", dependsOnMethods = "TestDestinationsNotGettingDeleted22424Launch", alwaysRun = true)

    public void Test_Bug_22424DeleteServiceInstance(){
        try{
            eventProcessManager.deleteServiceInstance(appName,appVersion,"WSStub1");
        }catch(Exception e){
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_Bug_22424Launch", "TestDestinationsNotGettingDeleted22424"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        try {
            deployEventProcess("Test_Bug_22424_postdelete.zip");

        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDestinationsNotGettingDeleted22424Launch", "TestDestinationsNotGettingDeleted22424"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDestinationsNotGettingDeleted22424Launch", "TestDestinationsNotGettingDeleted22424"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }
    }
    @Test(groups = "Bugs", description = "Destinations not getting deleted after stopping the Eventprocess in specific scenario.", dependsOnMethods = "Test_Bug_22424DeleteServiceInstance", alwaysRun = true)
    public void stopApplication() throws ServiceException, RemoteException {
        System.out.println("Stop");
        Logger.LogMethodOrder(Logger.getOrderMessage("stopApplication", "TestDestinationsNotGettingDeleted22424"));
        try {


            eventProcessManager.stopEventProcess(appName, appVersion);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                // Assert.fail("Thread Exception ", e);
                Logger.Log(Level.SEVERE, Logger.getErrMessage("stopApplication", "TestDestinationsNotGettingDeleted22424"), e);
            }
            //eventProcessManager.deleteEventProcess(appName1, appVersion, true, false);
            //eventProcessManager.deleteEventProcess(appName2, appVersion, true, false);
        }
        catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDestinationsNotGettingDeleted22424Launch", "TestDestinationsNotGettingDeleted22424"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDestinationsNotGettingDeleted22424Launch", "TestDestinationsNotGettingDeleted22424"), e);
            Assert.fail("Failed to do start application!", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("stopApplication", "TestDestinationsNotGettingDeleted22424"));
        Logger.Log(Level.INFO, Logger.getFinishMessage("stopApplication", "TestDestinationsNotGettingDeleted22424"));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
    }

    @Test(groups = "Bugs", description = "Destinations not getting deleted after stopping the Eventprocess in specific scenario.", dependsOnMethods = "stopApplication", alwaysRun = true)
    public void Get_Ports_List_22424() {
        try {
            HashMap destinations= getQueues("fps");
            destinations.putAll(getTopics("fps"));
            for(Iterator itr= destinations.keySet().iterator();itr.hasNext();){
                String destName = (String) itr.next();
                if(destName.contains(appName))
                    Assert.fail("Destination \""+destName+"\" not getting deleted");
            }

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            eventProcessManager.deleteEventProcess(appName,appVersion, true, true);
            Thread.sleep(2000);
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    @Test(enabled = false)
    public HashMap getQueues(String serverName) throws Exception {
            Object[] params = {serverName};
            String[] signatures = {String.class.getName()};
            return (HashMap)esb.invoke(objName5,"getQueues",params,signatures);
    }
    @Test(enabled = false)
    public HashMap getTopics(String serverName) throws Exception {
            Object[] params = {serverName};
            String[] signatures = {String.class.getName()};
            return (HashMap)esb.invoke(objName5,"getTopics",params,signatures);
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
