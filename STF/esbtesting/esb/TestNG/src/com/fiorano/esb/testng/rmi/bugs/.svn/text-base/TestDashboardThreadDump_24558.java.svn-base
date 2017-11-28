package com.fiorano.esb.testng.rmi.bugs;



import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.remote.JMXClient;
import org.testng.Assert;
import org.testng.annotations.Test;
import javax.management.*;
import java.io.*;


/**
 * Created with IntelliJ IDEA.
 * User: deepika
 * Date: 3/10/14
 * Time: 12:30 PM
 * To change this template use File | Settings | File Templates.
 */
/* Bug 24558 - Option to generate threaddump for servers running as services
 */


public class TestDashboardThreadDump_24558 extends AbstractTestNG{

    private JMXClient jmxclient;
    private StringBuffer fesThreadDump = null;
    private ObjectName objname = null;
    private String serverName= "fes";
    BufferedOutputStream bos = null;
    FileOutputStream fos = null;

    @Test(groups="bugs", alwaysRun = true)
    public void Setup(){

           jmxclient = JMXClient.getInstance();
    }

    @Test(groups="bugs", dependsOnMethods = "Setup")
    public void checkDashboardThreadDump() throws STFException{


       try{
           objname = new ObjectName("Fiorano.Esb.Server:ServiceType=FESServer,Name=FESServer");
       }
       catch(MalformedObjectNameException e){
           Assert.fail("Error in forming the object");
           e.printStackTrace();
       }


       try{
           System.out.println("Getting heap dump details");
           fesThreadDump = new StringBuffer((String)jmxclient.invoke(objname,"getThreadDump",null,null));
       }
       catch(Exception e){
           Assert.fail("Failed to get ThreadDumpDetails",e);
           e.printStackTrace();
        }

       if(fesThreadDump == null)
           Assert.fail("Error in fetching ThreadDump Details of the server : No details fetched");

        // Writing the DumpDetails to a file

        //The file will be stored in TestingHome/esbTesting

       try {
           fos = new FileOutputStream("../../" + File.separator + serverName);
           bos = new BufferedOutputStream(fos);
           bos.write(fesThreadDump.toString().getBytes());
       }
       catch (Exception e) {
           e.printStackTrace();
       }
       finally {
           try {
            if(bos != null)
               bos.close();
            fos.close();
           }
           catch (Exception e) {
           e.printStackTrace();
           }
       }

    }
}
