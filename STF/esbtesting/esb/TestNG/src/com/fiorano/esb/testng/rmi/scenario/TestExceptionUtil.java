package com.fiorano.esb.testng.rmi.scenario;

import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.framework.ServerStatusController;
import fiorano.tifosi.common.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/19/11
 * Time: 6:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestExceptionUtil extends AbstractTestNG{
    private boolean m_initialised;
    private String tempFile;
    private String resourceFilePath;

    static boolean isModifiedOnceHA=false;//to check the files overwriting
    static boolean isModifiedOnce=false;
    private FilenameFilter xmlFilter;//file extracter

    //creating the xml file filter
    private void setXmlFilter(String ext){
           xmlFilter=new OnlyExt(ext);
       }

    // the function modifies the xml files and replace any "search" string with "replace" string
    void modifyXmlFiles(String path,String search,String replace) throws IOException {
        File fl=new File(path);
        File[]filearr=fl.listFiles(xmlFilter);
        FileReader fr=null;
        FileWriter fw=null;
        boolean change=false;
        BufferedReader br;
        String s;
        String result="";


        int i=0;
        while(i<filearr.length-1)
        {
            try{
                fr=new FileReader(filearr[i]) ;
            } catch(FileNotFoundException e){
                e.printStackTrace();
            }
            try{
                fw=new FileWriter("temp.xml");
            } catch(IOException e){
                e.printStackTrace();
            }
            br=null;
            br=new BufferedReader(fr);


         while((s=br.readLine())!=null)
         {

             int j=s.indexOf(search);
             if(j!=-1)
             {
                 change=true;
                 int k=search.length()+j;
                 //System.out.println("The Index start is "+j+" and index last is "+ k);
                 result=s.substring(0,j);
                 result=result+replace;
                 result=result+ s.substring(k);
                 s=result;

              }
                //System.out.print(s);

                fw.write(s);
               fw.write('\n');
        }
            fr.close();
            fw.close();

            if(change)
            {
                fr=new FileReader("temp.xml");
                fw=new FileWriter(filearr[i]);
                br=new BufferedReader(fr);
                while((s=br.readLine())!=null)
                {
                    fw.write(s);
                    fw.write('\n');
                }
                fr.close();
                fw.close();
                change=false;
            }

            i=i+1;

        }
    }

    public void init() throws Exception {
        printInitParams();
    }

    protected void printInitParams() {
        System.out.println("The Parameters Used For The Test Are::");
        tempFile = resourceFilePath + File.separator + "tempFile";
        System.out.println("...........................................................................");
    }

    @BeforeClass(groups = "ExceptionUtilTest", alwaysRun = true)
    public void startSetUp(){
        System.out.println("Started the Execution of the TestCase::" + getName());
        if (!m_initialised) {
            initializeProperties("scenario" + fsc + "ExceptionUtil" + fsc + "tests.properties");
            resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "ExceptionUtil";
            ServerStatusController stc;
            stc=ServerStatusController .getInstance();
            boolean isFPSHA=stc.getFPSHA();
            setXmlFilter("xml");
            try {
                if(isFPSHA && !isModifiedOnceHA)
                {
                    isModifiedOnceHA=true;
                    modifyXmlFiles(resourceFilePath,"fps_test","fps_ha");
                }
                else if(!isFPSHA && !isModifiedOnce)
                {
                    isModifiedOnce=true;
                    modifyXmlFiles(resourceFilePath,"fps_ha","fps_test");
                }
                init();
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                Assert.fail(Logger.getErrMessage(getName(), "TestExceptionUtil"));
            }
            m_initialised = true;
        }

    }

    @Test(groups = "ExceptionUtilTest", alwaysRun = true, dependsOnMethods = "startSetUp")
    public void testSerializeEnterpriseException() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testSerializeEnterpriseException", "ExceptionUtilTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            TifosiException tifEx = null;
            try{
                Class.forName("imaginaryclasswhichwontbepresent");
            }
            catch(Exception e){
               tifEx = new TifosiException(TifErrorCodes.ACCESS_DENIED_ERROR,"Test Error",e);
            }
            Assert.assertNotNull(tifEx);
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(tempFile));
            EnterpriseExceptionUtil.toStream(dos, tifEx);
            dos.flush();
            dos.close();
            DataInputStream dis = new DataInputStream(new FileInputStream(tempFile));
            TifosiException ex = (TifosiException)EnterpriseExceptionUtil.fromStream(dis);
            Assert.assertNotNull(ex);
            System.out.println("trace of chained tifosi:");
            System.out.println(ex.getStackTraceAsString());
            dis.close();
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSerializeEnterpriseException", "ExceptionUtilTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSerializeEnterpriseException", "ExceptionUtilTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "ExceptionUtilTest", alwaysRun = true, dependsOnMethods = "testSerializeEnterpriseException")
    public void testSerializeEnterpriseRuntimeException() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testSerializeEnterpriseRuntimeException", "ExceptionUtilTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            EnterpriseRuntimeException ertfEx = null;
            try{
                Class.forName("imaginaryclasswhichwontbepresent");
            }
            catch(Exception e){
               ertfEx = new EnterpriseRuntimeException(TifErrorCodes.ACCESS_DENIED_ERROR,e);
            }
            Assert.assertNotNull(ertfEx);
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(tempFile));
            EnterpriseExceptionUtil.toStream(dos,ertfEx);
            dos.flush();
            dos.close();
            DataInputStream dis = new DataInputStream(new FileInputStream(tempFile));
            EnterpriseRuntimeException ex = (EnterpriseRuntimeException)EnterpriseExceptionUtil.fromStream(dis);
            Assert.assertNotNull(ex);
            System.out.println("trace of chained runtime exception:");
            System.out.println(ex.getStackTraceAsString());
            dis.close();
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSerializeEnterpriseRuntimeException", "ExceptionUtilTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSerializeEnterpriseRuntimeException", "ExceptionUtilTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }
                                                                                         
    @Test(groups = "ExceptionUtilTest", alwaysRun = true, dependsOnMethods = "testSerializeEnterpriseRuntimeException")
    public void testSerializeEnterpriseIllegalArgumentException() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testSerializeEnterpriseIllegalArgumentException", "ExceptionUtilTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            EnterpriseIllegalArgumentException ertfEx = null;
            try{
                Class.forName("imaginaryclasswhichwontbepresent");
            }
            catch(Exception e){
               ertfEx = new EnterpriseIllegalArgumentException(TifErrorCodes.ACCESS_DENIED_ERROR,e);
            }
            Assert.assertNotNull(ertfEx);
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(tempFile));
            EnterpriseExceptionUtil.toStream(dos,ertfEx);
            dos.flush();
            dos.close();
            DataInputStream dis = new DataInputStream(new FileInputStream(tempFile));
            EnterpriseIllegalArgumentException ex = (EnterpriseIllegalArgumentException)EnterpriseExceptionUtil.fromStream(dis);
            Assert.assertNotNull(ex);
            System.out.println("trace of chained runtime exception:");
            System.out.println(ex.getStackTraceAsString());
            dis.close();
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSerializeEnterpriseIllegalArgumentException", "ExceptionUtilTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSerializeEnterpriseIllegalArgumentException", "ExceptionUtilTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }
                                                                                         
    @Test(groups = "ExceptionUtilTest", alwaysRun = true, dependsOnMethods = "testSerializeEnterpriseIllegalArgumentException")
    public void testSerializePeerException() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testSerializePeerException", "ExceptionUtilTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            PeerException ertfEx = null;
            try{
                Class.forName("imaginaryclasswhichwontbepresent");
            }
            catch(Exception e){
               ertfEx = new PeerException(TifErrorCodes.ACCESS_DENIED_ERROR,e);
            }
            Assert.assertNotNull(ertfEx);
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(tempFile));
            EnterpriseExceptionUtil.toStream(dos,ertfEx);
            dos.flush();
            dos.close();
            DataInputStream dis = new DataInputStream(new FileInputStream(tempFile));
            PeerException ex = (PeerException)EnterpriseExceptionUtil.fromStream(dis);
            Assert.assertNotNull(ex);
            System.out.println("trace of chained runtime exception:");
            System.out.println(ex.getStackTraceAsString());
            dis.close();
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSerializePeerException", "ExceptionUtilTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSerializePeerException", "ExceptionUtilTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }
}
