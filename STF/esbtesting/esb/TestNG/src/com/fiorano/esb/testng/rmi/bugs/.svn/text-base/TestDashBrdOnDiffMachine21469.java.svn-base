package com.fiorano.esb.testng.rmi.bugs;

//import com.gargoylesoftware.htmlunit.html.HtmlPage;
//import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by IntelliJ IDEA.
 * User: santosh
 * Date: 3/26/12
 * Time: 11:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestDashBrdOnDiffMachine21469 {

    private String urlText;
    private BufferedReader in;
    private String FESaddrss;
    private ArrayList urlDetails;
   // private HtmlPage page;
   // private HtmlTableRow row;

    @Test(groups = "bugs",alwaysRun =true)
    public void initNewSetup(){
        try {
            this.urlDetails = getActiveFESUrl();
        } catch (STFException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        this.FESaddrss = (String) urlDetails.get(0);
        this.urlText = "http://"+FESaddrss+":1980/ESBDashboard/loginpage.htm?check=true";
    }
    @Test(groups ="bugs",dependsOnMethods = "initNewSetup",alwaysRun = true)
    public void TestNewNone(){
        BufferedReader in = null;
                try {
                    URL url = new URL(urlText);
                    in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println(inputLine);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
    }

     @Test(enabled = false)
    public static ArrayList getActiveFESUrl() throws STFException {
        ArrayList url = new ArrayList(2);//string followed by int.
        try {
            String s = ServerStatusController.getInstance().getURLOnFESActive();
            String rtlPort = s.substring(s.lastIndexOf(":") + 1);
            url.add(s.substring(s.lastIndexOf("//") + 2, s.lastIndexOf(":")));
            if (rtlPort.equals("1947")) {
                url.add(2047);
            } else
                url.add(2048);
        } catch (STFException e) {
            e.printStackTrace();
        }
        return url;
    }
}
