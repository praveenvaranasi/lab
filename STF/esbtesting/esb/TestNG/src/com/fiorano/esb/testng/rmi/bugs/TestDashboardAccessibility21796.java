package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IServiceProviderManager;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.remote.RMIClient;
import com.fiorano.stf.remote.RTLClient;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import junit.framework.Assert;
import org.testng.annotations.Test;

import javax.management.Attribute;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: aravind
 * Date: 8/13/12
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 */

public class TestDashboardAccessibility21796 extends AbstractTestNG {
    private JMXClient jmxClient;
    protected char fsc = File.separatorChar;
    private String space=" ";
    private String pathUpToResource;
    private String pathToJettyLog=System.getProperty("FIORANO_HOME")+fsc+"runtimedata"+fsc+"EnterpriseServers"+fsc+"profile1"+fsc+"FES"+fsc+"run"+fsc+"logs"+fsc+"jetty.log";
    private static MBeanServerConnection mbsc = null;
    private IServiceProviderManager iServiceProviderManager;
    TestEnvironmentConfig testEnvConfig= ESBTestHarness.getTestEnvConfig();
    String home =testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);


    @Test(groups = "TestDashboardAccessibility21796", alwaysRun = true)
    public void startSetup() throws IOException {
        pathUpToResource = home+fsc+"esb"+fsc+"TestNG"+fsc+"resources";
        if (rmiClient == null) {
            rmiClient = RMIClient.getInstance();
        }
        jmxClient=JMXClient.getInstance();
        mbsc=JMXClient.getMBeanServerConnection();
        iServiceProviderManager=rmiClient.getServiceProviderManager();
    }
    @Test(groups = "TestDashboardAccessibility21796", dependsOnMethods = "startSetup")
    public void checkDashboardAccessibility() throws STFException {
        ObjectName objectName=null;
        try {
            objectName=new ObjectName("Fiorano.Esb.Jetty:ServiceType=Jetty,Name=Jetty,type=config");
        } catch (MalformedObjectNameException e) {
            System.out.println("Problem with object creation");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        int portNumber =-1;

        // ****test case 1****
        try {
            portNumber =(Integer)mbsc.getAttribute(objectName, "PortNumber");

            /*
              Use setAttribute() in MBeanServerConnection for setting the attribute values . and getAttribute() for getting the values.
              The above 2 methods are only for getting and setting the attribute values . In-order to identify which method is for attribute and
              which method is for -operation : in the code you can find annotations above    each jmx method that specifies whether it is
              @jmx.managed-attribute or @jmx.managed-operation ( you can take a look at methods in JettyServiceConfig.java) . Inorder to call @jmx.managed-operation , use invoke method under MBeanServerConnection.
              Also while setting and getting the attributes make sure that you do not specify "set" and "get" in the methodName parameter since while
              calling java will automatically remove "get" and "set " . Eg : if the jmx method name is getSSLPortNumber() , then while calling getAttribute() ,
              use only "SSLPortNumber" and not "getSSLPortNumber" like in getAttribute(objectName , "SSLPortNumber") .

             */
        }
        catch (Exception e)
        {
           e.printStackTrace();
        }
        if(portNumber ==1980)
            System.out.println("Test case 1 Passed");
        else
        {
            System.out.println("Test failed: portnum:;"+portNumber);
            Assert.fail("FES web server listening port should be non ssl port, that is 1980");
        }


        // ****test case 2****
        //set the values
        try {
            mbsc.setAttribute(objectName,new Attribute("SSLEnabled",new Boolean(true)));
            //mbsc.setAttribute(objectName,new Attribute("KeystoreLocation",new String("/home/aravind/Documents/doc/server.jks")));
            System.out.println(pathUpToResource +fsc+"certificates_TestDashboardAccessibility21796"+fsc+"server.jks");
            mbsc.setAttribute(objectName,new Attribute("KeystoreLocation",new String(pathUpToResource +fsc+"certificates_TestDashboardAccessibility21796"+fsc+"server.jks")));
            mbsc.setAttribute(objectName,new Attribute("KeystorePassword",new String("fiorano")));
            mbsc.setAttribute(objectName,new Attribute("KeyPassword",new String("fiorano")));
            //mbsc.setAttribute(objectName,new Attribute("TrustStoreLocation",new String("/home/aravind/Documents/doc/client.jks")));
            mbsc.setAttribute(objectName,new Attribute("TrustStoreLocation",new String(pathUpToResource +fsc+"certificates_TestDashboardAccessibility21796"+ fsc+"client.jks")));
            mbsc.setAttribute(objectName,new Attribute("TruststorePassword",new String("fiorano")));
        } catch (Exception e) {
            System.out.println("Problem while setting attributes");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        saveProfile_ClearLog_RestartServer();

        int current_port,current_ssl_port,port[]=new int[2];
        port=readJettyLog();
        current_port=port[0];
        current_ssl_port=port[1];
        if(current_ssl_port==1984&&current_port==-1)
            System.out.println("Test case 2 passed");
        else if(current_port==1980&&current_ssl_port==-1)
            Assert.fail("FES web server listening port should be only ssl port, that is 1984. It shouldn't listen on default 1980 port");
        else if(current_port==1980&&current_ssl_port==1984)
            Assert.fail("FES web server listening port should be ssl port, that is 1984. It shouldn't listen on default 1980 port");
        else
            Assert.fail("FES web server listening port should be ssl port, that is 1984");


        // **test case 3****


        try {
            mbsc.setAttribute(objectName,new Attribute("DefaultConnectorEnabled",new Boolean(true)));
        }
        catch (Exception e)
        {
            System.out.println("Error in setting DefaultConnectorEnabled");
            e.printStackTrace();
        }

        saveProfile_ClearLog_RestartServer();
        port=readJettyLog();
        current_port=port[0];
        current_ssl_port=port[1];
        if(current_ssl_port==1984&&current_port==1980)
            System.out.println("Test case 3 passed");
        else
            Assert.fail("FES web server listening port should be ssl port, that is 1984 and also default port number 1980");



        //**** revert back the FES arguments***
        try {
            mbsc.setAttribute(objectName,new Attribute("DefaultConnectorEnabled",new Boolean(false)));
            mbsc.setAttribute(objectName,new Attribute("SSLEnabled",new Boolean(false)));
        }
        catch (Exception e)
        {
            System.out.println("Error in setting DefaultConnectorEnabled");
            e.printStackTrace();
        }

        saveProfile_ClearLog_RestartServer();

    }


    void saveProfile_ClearLog_RestartServer()
    {
        // *****saving the config for fes profile ******
        ObjectName objectName1=null;
        try {
            objectName1=new ObjectName("Container.UserComponents:Name=ConfigurationProvider,ServiceType=ConfigProvider");
        } catch (MalformedObjectNameException e) {
            System.out.println("Problem with initilaizing obj");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            mbsc.invoke(objectName1,"save",null,null);
        } catch (Exception e) {
            System.out.println("Problem with invoking save");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // clearing jetty logs inorder to read it
        System.out.println("pathToJettyLog:" + pathToJettyLog);
        File file=new File(pathToJettyLog);
        if(file.exists())
        file.delete();

        try {
            rtlClient.restartEnterpriseServer();
            System.out.println("restarting FES Server");
            Thread.sleep(100000);
            //initializing
            rmiClient = RMIClient.getInstance();
            rtlClient = RTLClient.getInstance();
            jmxClient = JMXClient.getInstance();
        } catch (Exception e) {
            System.out.println("problem in restarting FES Server");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    //
    //readJettyLog() ready the Jetty log to get the port in which FES server is listening
    //returns integer array having two elements currentPort and Cutternt SSL PORT
    //theis function is return according to the format of the Jetty log if there is any change in the Jetty log the should be altered
    //it is assumed that
    //ordinary port willl in the format           Started SelectChannelConnector@0.0.0.0:1980
    //ssl port will in the format                 Started SslSocketConnector@0.0.0.0:1984
    //

    int [] readJettyLog()
    {
        File file=null;

        BufferedReader br=null;
        String line, sslPort =null, port =null;
        int current_port=-1,pow=1,current_ssl_port=-1;

        try{
            file=new File(pathToJettyLog);
            br=new BufferedReader(new FileReader(file));
            while((line=br.readLine())!=null){
              /*  if(line.contains("Started SslSocketConnector"))
                {
                    sslPort=line;
                    current_ssl_port=0;
                }*/
                if(line.contains("SSL-HTTP"))
                {
                    sslPort=line;
                    current_ssl_port=0;
                }
                if(line.contains("{HTTP/1.1}"))
                {
                    port=line;
                    current_port=0;
                }

            }
            if(sslPort!=null)
            for(int i=sslPort.length()-2;i!=0;--i)
            {
                int num=sslPort.charAt(i)-'0';
                if(num<10&&num>-1)
                {
                    current_ssl_port+=num*pow;
                    pow*=10;
                }
                else
                    break;
            }

            pow=1;
            if(port!=null)
            for(int i=port.length()-1;i!=0;--i)
            {
                int num=port.charAt(i)-'0';
                if(num<10&&num>-1)
                {
                    current_port+=num*pow;
                    pow*=10;
                }
                else
                    break;
            }

            br.close();
        }
        catch (Exception e)
        {
            System.out.println("Format of these variables have been changed. path to jetty logs("+pathToJettyLog+") and type of jetty logs(\"Started SslSocketConnector@0.0.0.0:1984\") . Modify the code accordingly\n"+current_port+"\t"+current_ssl_port+e);
            e.printStackTrace();
        }
        finally {

            return new int[]{current_port,current_ssl_port};
        }
    }

}