package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import org.testng.annotations.Test;

import java.io.*;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: janardhan
 * Date: Dec 13, 2010
 * Time: 3:30:06 PM
 * To change this template use File | Settings | File Templates.
 */


public class TestFioranomonitoringService extends AbstractTestNG {

    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private TestEnvironmentConfig testEnvConfig;

    @Test(groups = "FioranomonitoringService", alwaysRun = true)
    public void startSetup() {

    }

    @Test(groups = "FioranomonitoringService", description = "bug 20019 Unable to start Event Process Monitoring Service, bug no 20094 Not able to launch Fiorano Monitor tool as a service on windows and bug no 20040 How to run Fiorano Monitoring Utility as service in mac", dependsOnMethods = "startSetup", alwaysRun = true)
    public void testFioranomonitoringService() {


        Logger.LogMethodOrder(Logger.getOrderMessage("testFioranomonitoringService", "TestFioranomonitoringService"));


        testEnvConfig = ESBTestHarness.getTestEnvConfig();

        String fioranohome = System.getProperty("FIORANO_HOME");
        System.out.println("fioranohome is " + fioranohome);
        String javahome = testEnvConfig.getProperty(TestEnvironmentConstants.JAVA_HOME);
        //String javahome = System.getProperty("java1.5");
        System.out.println("javahome is " + javahome);
        String monitorbin = fioranohome + System.getProperty("file.separator") + "esb" + System.getProperty("file.separator") + "tools" + System.getProperty("file.separator") + "monitor" + System.getProperty("file.separator") + "bin" + System.getProperty("file.separator");
        System.out.println("mnonitorbin is " + monitorbin);
        System.out.println("OS name  is: " + System.getProperty("os.name"));
        try {
            String osname = System.getProperty("os.name");
            if (osname.startsWith("Windows")) {
                /*Process p2=null;
                String[] cmdarray={"FioranoMonitorService.bat","install"};
                p2=Runtime.getRuntime().exec(cmdarray, null, new File(monitorbin));
                 BufferedReader br5 = new BufferedReader(
                        new InputStreamReader(p2.getInputStream()));
                String line5 = null;

                while ((line5 = br5.readLine()) != null) {


                    System.out.println(line5);

                }

                br5.close();*/

                Process proc = null;

                File f = new File(monitorbin);
                //Process p = Runtime.getRuntime().exec(new String[]{"FioranoMonitorService.bat","install"},null,f);
                proc = Runtime.getRuntime().exec("cmd", null, f);


                if (proc != null) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
                    out.println("FioranoMonitorService.bat install");
                    //out.println("pwd");
                    out.println("exit");
                    in.close();
                    out.close();
                }

                String soaver = testEnvConfig.getProperty(TestEnvironmentConstants.SOA_VERSION);
                System.out.println("SOAVersion is:: " + soaver);
                String[] strarray = null;
                strarray = soaver.split("\\\\");
                soaver = strarray[strarray.length - 1];
                System.out.println("soaver is:: " + soaver);
                String s = "sc query " + "\"FioranoMonitoringUtility ::Fiorano SOA " + soaver + "\"";
                System.out.println("----------------------------------");
                System.out.println(s);
                System.out.println("----------------------------------");
                System.out.println("executimg command:: " + "sc query " + "\"FioranoMonitoringUtility ::Fiorano SOA " + soaver + "\"");
                Process p3 = null;
                p3 = Runtime.getRuntime().exec("sc query " + "\"FioranoMonitoringUtility ::Fiorano SOA " + soaver + "\"");
                BufferedReader br8 = new BufferedReader(
                        new InputStreamReader(p3.getInputStream()));
                String line8 = null;

                while ((line8 = br8.readLine()) != null) {

                    if (line8.contains("FAILED")) {
                        throw new Exception("Failed to run FioranoMonitoringService");

                    }
                    System.out.println(line8);

                }

                br8.close();


                BufferedReader br6;

                File f3 = new File(monitorbin + "service" + System.getProperty("file.separator") + "FioranoMonitoring.log");

                br6 = new BufferedReader(new FileReader(f3));
                String line6;
                while ((line6 = br6.readLine()) != null) {

                    if (line6.startsWith("ERROR") || line6.startsWith("FATAL")) {
                        throw new Exception("Error in running FioranoMonitoringService");

                    }


                }
                br6.close();

                /*Process p5 = null;
                p5 = Runtime.getRuntime().exec(monitorbin + "FioranoMonitorService.bat uninstall");
                BufferedReader br7 = new BufferedReader(
                        new InputStreamReader(p5.getInputStream()));
                String line7 = null;

                while ((line7 = br7.readLine()) != null) {


                    System.out.println(line7);

                }

                br7.close();*/

                Process proc2 = null;

                File fi = new File(monitorbin);
                //Process p = Runtime.getRuntime().exec(new String[]{"FioranoMonitorService.bat","install"},null,f);
                proc2 = Runtime.getRuntime().exec("cmd", null, fi);


                if (proc2 != null) {
                    BufferedReader br10 = new BufferedReader(new InputStreamReader(proc2.getInputStream()));
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc2.getOutputStream())), true);
                    out.println("FioranoMonitorService.bat uninstall");
                    //out.println("pwd");
                    out.println("exit");
                    br10.close();
                    out.close();
                }

            }
            if (osname.startsWith("Linux")) {
                File f1 = new File(monitorbin + "FioranoMonitorService.sh");
                File f2 = new File(monitorbin + "FioranoMonitorService1.sh");
                BufferedReader br;
                BufferedWriter bw;

                br = new BufferedReader(new FileReader(f1));

                bw = new BufferedWriter(new FileWriter(f2));


                String line = "";
                StringBuffer newtext = new StringBuffer("");
                while ((line = br.readLine()) != null) {

                    if (line.startsWith("FIORANO_HOME=")) {
                        line = "FIORANO_HOME=" + fioranohome;
                    }
                    if (line.startsWith("JAVA_HOME=")) {
                        line = "JAVA_HOME=" + javahome;
                    }

                    bw.write(line + "\n");


                }
                br.close();
                bw.flush();
                bw.close();


                Runtime.getRuntime().exec("sudo cp " + monitorbin + "FioranoMonitorService1.sh " + "/etc/init.d/");
                Runtime.getRuntime().exec("sudo chkconfig --add FioranoMonitorService1.sh");
                Runtime.getRuntime().exec("sudo chmod +x /etc/init.d/FioranoMonitorService1.sh");
                Runtime.getRuntime().exec("sudo chmod +x " + monitorbin + "FioranoMonitorService1.sh");
                Thread.sleep(10000);
                //Runtime.getRuntime().exec(monitorbin + "FioranoMonitorService1.sh start");
                Process p = null;
                p = Runtime.getRuntime().exec(monitorbin + "FioranoMonitorService1.sh start");
                Thread.sleep(10000);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(p.getInputStream()));
                String line1 = null;

                while ((line1 = in.readLine()) != null) {


                    System.out.println(line1);

                }

                in.close();
                BufferedReader br3;

                File f3 = new File(monitorbin + "service/FioranoMonitoring.log");

                br3 = new BufferedReader(new FileReader(f3));
                String line4;
                while ((line4 = br3.readLine()) != null) {

                    if (line4.startsWith("ERROR") || line4.startsWith("FATAL")) {
                        throw new Exception("Error in running FioranoMonitoringService");

                    }


                }
                br3.close();

                Process p1 = null;
                p1 = Runtime.getRuntime().exec(monitorbin + "FioranoMonitorService1.sh stop");

                BufferedReader br4 = new BufferedReader(
                        new InputStreamReader(p1.getInputStream()));
                String line5 = null;

                while ((line5 = br4.readLine()) != null) {


                    System.out.println(line5);

                }
                Runtime.getRuntime().exec("sudo rm /etc/init.d/FioranoMonitorService1.sh");
                br4.close();
            }

            if (osname.startsWith("Mac")) {
                File f1 = new File(monitorbin + "FioranoMonitorService.sh");
                File f2 = new File(monitorbin + "FioranoMonitorService1.sh");
                BufferedReader br;
                BufferedWriter bw;

                br = new BufferedReader(new FileReader(f1));

                bw = new BufferedWriter(new FileWriter(f2));


                String line = "";
                // StringBuffer newtext = new StringBuffer("");
                while ((line = br.readLine()) != null) {

                    if (line.startsWith("FIORANO_HOME=")) {
                        line = "FIORANO_HOME=" + fioranohome;
                    }
                    if (line.startsWith("JAVA_HOME=")) {
                        line = "JAVA_HOME=" + javahome;
                    }

                    bw.write(line + "\n");


                }
                br.close();
                bw.flush();
                bw.close();


                File f3 = new File(monitorbin + "com.fiorano.monitorservice.plist");
                File f4 = new File(monitorbin + "com.fiorano.monitorservice1.plist");
                BufferedReader br1;
                BufferedWriter bw1;

                br1 = new BufferedReader(new FileReader(f3));

                bw1 = new BufferedWriter(new FileWriter(f4));


                String line1 = "";
                StringBuffer newtext1 = new StringBuffer("");
                while ((line1 = br1.readLine()) != null) {

                    if (line1.startsWith("<array>")) {
                        // line = "FIORANO_HOME=" + fioranohome;
                        bw1.write(line1 + "\n");
                        line1 = br1.readLine();
                        line1 = "<string>" + monitorbin + "FioranoMonitorService1.sh</string>";
                    }


                    bw1.write(line1 + "\n");


                }
                br1.close();
                bw1.flush();
                bw1.close();


                //System.out.println("sudo chmod +x " + monitorbin + "FioranoMonitorService1.sh");
                Runtime.getRuntime().exec("sudo chmod +x " + monitorbin + "FioranoMonitorService1.sh");
                //System.out.println("sudo cp " + monitorbin + "com.fiorano.monitorservice1.plist " + "/System/Library/LaunchAgents");
                Runtime.getRuntime().exec("sudo cp " + monitorbin + "com.fiorano.monitorservice1.plist " + "/System/Library/LaunchAgents");
                //System.out.println("sudo chmod 644 /System/Library/LaunchAgents/com.fiorano.monitorservice1.plist");
                Runtime.getRuntime().exec("sudo chmod 644 /System/Library/LaunchAgents/com.fiorano.monitorservice1.plist");
                //System.out.println("sudo Launchctl load /System/Library/LaunchAgents/com.fiorano.monitorservice1.plist");
                Runtime.getRuntime().exec("sudo Launchctl load /System/Library/LaunchAgents/com.fiorano.monitorservice1.plist");

                Thread.sleep(40000);


                Process p2 = null;
                p2 = Runtime.getRuntime().exec(monitorbin + "FioranoMonitorService1.sh status");

                BufferedReader in2 = new BufferedReader(
                        new InputStreamReader(p2.getInputStream()));
                String line8 = null;

                while ((line8 = in2.readLine()) != null) {

                    if (line8.contains("is running")) {
                        System.out.println(line8);
                    } else {
                        System.out.println(line8);
                        throw new Exception("Error in running FioranoMonitoringService");
                    }

                }

                in2.close();


                /* BufferedReader br3;

                Process p = null;
                p = Runtime.getRuntime().exec(monitorbin + "FioranoMonitorService1.sh start");
                Thread.sleep(10000);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(p.getInputStream()));
                String line4 = null;

                while ((line4 = in.readLine()) != null) {


                    System.out.println(line4);

                }

                in.close();*/
                BufferedReader br5;

                File f5 = new File(monitorbin + "service/FioranoMonitoring.log");

                br5 = new BufferedReader(new FileReader(f5));
                String line7;
                while ((line7 = br5.readLine()) != null) {

                    if (line7.startsWith("ERROR") || line7.startsWith("FATAL")) {
                        throw new Exception("Error in running FioranoMonitoringService");

                    }


                }
                br5.close();

                Process p1 = null;
                p1 = Runtime.getRuntime().exec("sudo " + monitorbin + "FioranoMonitorService1.sh stop");

                BufferedReader br4 = new BufferedReader(
                        new InputStreamReader(p1.getInputStream()));
                String line5 = null;

                while ((line5 = br4.readLine()) != null) {


                    System.out.println(line5);

                }
                //  Runtime.getRuntime().exec("sudo rm /etc/init.d/FioranoMonitorService1.sh");
                br4.close();
                Runtime.getRuntime().exec("sudo Launchctl unload /System/Library/LaunchAgents/com.fiorano.monitorservice1.plist");
                Runtime.getRuntime().exec("sudo rm /System/Library/LaunchAgents/com.fiorano.monitorservice1.plist");
            }

        }

        catch (IOException
                e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            //Assert.fail("Failed to run FioranoMonitoringService", e);
        }
        catch (Exception
                e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testFioranomonitoringService", "TestFioranomonitoringService"), e);
            //Assert.fail("Failed to run FioranoMonitoringService", e);
            return;
        }


        //clear map
        eventStore.clear();


    }

}