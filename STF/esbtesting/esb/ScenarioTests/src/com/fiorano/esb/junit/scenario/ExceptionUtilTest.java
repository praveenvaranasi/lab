/**
 * Copyright (c) 1999-2017, Fiorano Software Inc. and affiliates.
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Fiorano Software ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * enclosed with this product or entered into with Fiorano.
 */
package com.fiorano.esb.junit.scenario;

import com.fiorano.stf.test.core.DRTTestCase;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.consts.DRTTestConstants;
import com.fiorano.stf.logger.DRTTestLogger;
import com.fiorano.stf.test.core.TestCaseElement;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.Assert;
import fiorano.tifosi.common.*;

/**
 * Created by IntelliJ IDEA.
 * User: Prasanth
 * Date: Mar 15, 2007
 * Time: 10:55:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class ExceptionUtilTest extends DRTTestCase {

    private boolean m_initialised;
    private String tempFile;
    private String resourceFilePath;

    static boolean isModifiedOnceHA=false;//to check the files overwriting
    static boolean isModifiedOnce=false;
    private FilenameFilter xmlFilter;//file extracter

    public ExceptionUtilTest(String name) {
        super(name);
    }

    public ExceptionUtilTest(TestCaseElement testCaseConfig){
        super(testCaseConfig);
    }

    public static Test suite() {
        return new TestSuite(ExceptionUtilTest.class);
    }

     private void setXmlFilter(String ext)//creating the xml file filter
       {
           xmlFilter=new OnlyExt(ext);
       }


        // the function modifies the xml files and replace any "search" string with "replace" string
        void modifyXmlFiles(String path,String search,String replace) throws IOException{
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
    //changed function

    public void init() throws Exception {
        printInitParams();
    }

    protected void printInitParams() {
        System.out.println("The Parameters Used For The Test Are::");
        tempFile = resourceFilePath + File.separator + "tempFile";
        System.out.println("...........................................................................");
    }

    public void setUp() throws Exception {
        super.setUp();
        if (!m_initialised) {
            resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "ExceptionUtil";

        //new changes            
        ServerStatusController stc;
        stc=ServerStatusController .getInstance();
        boolean isFPSHA=stc.getFPSHA();
setXmlFilter("xml");
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
            m_initialised = true;
        }
    }



    public void testSerializeEnterpriseException() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSerializeEnterpriseException", "ExceptionUtilTest"));
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
            EnterpriseExceptionUtil.toStream(dos,tifEx);
            dos.flush();
            dos.close();
            DataInputStream dis = new DataInputStream(new FileInputStream(tempFile));
            TifosiException ex = (TifosiException)EnterpriseExceptionUtil.fromStream(dis);
            Assert.assertNotNull(ex);
            System.out.println("trace of chained tifosi:");
            System.out.println(ex.getStackTraceAsString());
            dis.close();
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSerializeEnterpriseException", "ExceptionUtilTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSerializeEnterpriseException", "ExceptionUtilTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

     public void testSerializeEnterpriseRuntimeException() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSerializeEnterpriseRuntimeException", "ExceptionUtilTest"));
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
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSerializeEnterpriseRuntimeException", "ExceptionUtilTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSerializeEnterpriseRuntimeException", "ExceptionUtilTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

     public void testSerializeEnterpriseIllegalArgumentException() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSerializeEnterpriseIllegalArgumentException", "ExceptionUtilTest"));
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
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSerializeEnterpriseIllegalArgumentException", "ExceptionUtilTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSerializeEnterpriseIllegalArgumentException", "ExceptionUtilTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testSerializePeerException() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSerializePeerException", "ExceptionUtilTest"));
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
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSerializePeerException", "ExceptionUtilTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSerializePeerException", "ExceptionUtilTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public static ArrayList getOrder(){

	ArrayList methodsOrder = new ArrayList();

	methodsOrder.add("testSerializeEnterpriseException");
	methodsOrder.add("testSerializeEnterpriseRuntimeException");
	methodsOrder.add("testSerializeEnterpriseIllegalArgumentException");
	methodsOrder.add("testSerializePeerException");

	return methodsOrder;
    }
}
