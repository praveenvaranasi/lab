package com.fiorano.stf.framework;

import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import com.fiorano.stf.util.ConfigsParser;
import com.fiorano.stf.util.ZipUtil;
import net.sf.antcontrib.antserver.client.ClientTask;
import net.sf.antcontrib.antserver.commands.PropertyContainer;
import net.sf.antcontrib.antserver.commands.RunTargetCommand;
import net.sf.antcontrib.antserver.commands.SendFileCommand;
import org.apache.tools.ant.Project;
import org.w3c.dom.Document;

import java.io.*;
import java.util.Vector;
import java.util.zip.ZipOutputStream;

import java.io.IOException;
/**
 * @author Sandeep M
 * @Date:
 */
public class ExecutionController {
    private static ExecutionController instance = new ExecutionController();


    private ExecutionController() {
    }

    public static ExecutionController getInstance() {
        return instance;
    }

    public void sendProfiles(String profilesZipName) {
        Vector properties = new Vector();
        addProperty(properties, "profilesZipName", profilesZipName);
        callRemoteAntTarget("localhost", "sendProfiles", properties);
    }

    public void sendConfiguration(String configsZipName) {
        Vector properties = new Vector();
        addProperty(properties, "configsZipName", configsZipName);
        callRemoteAntTarget("localhost", "sendConfigs", properties);
    }

    public void deployProfile(DeploymentUnit deploymentUnit, TestEnvironmentConfig config) throws STFException {
        int profileType = deploymentUnit.getProfileType();
        String priFESURL = deploymentUnit.getPrimaryFESUrl();
        String secFESURL = deploymentUnit.getSecFESUrl();
        String lockFilePath = deploymentUnit.getLockFilePath();
        String backupPeerUrl = deploymentUnit.getBackupPeerURL();
        String gatewayMachine = deploymentUnit.getGatewayMachineAddress();
        String configsFilePath = config.get(TestEnvironmentConstants.FIORANO_HOME) + File.separator + deploymentUnit.getServerConfigFilePath();

        Document doc = ConfigsParser.createDOM(configsFilePath);

        switch (profileType) {
            case DeploymentUnit.HA_FES_PRIMARY: {
                String attr1Name[] = new String[]{ConfigsParser.ObjectName, ConfigsParser.Password};
                String attr1Val[] = new String[]{"Fiorano.Esb.Bam.Alert.Transport.Smtp:ServiceType=SMTPTransport," +
                        "Name=SMTPTransport,type=config", ""};
                ConfigsParser.createElementNode(doc, "Container", ConfigsParser.SMTP_TRANSPORT_CONFIG, attr1Name, attr1Val);

                String attr2Name[] = new String[]{ConfigsParser.ObjectName, ConfigsParser.Password};
                String attr2Val[] = new String[]{"Fiorano.Esb.Events:ServiceType=EventEmailNotifier,Name=ESBEventEmailNotifier,type=config", ""};
                ConfigsParser.createElementNode(doc, "Container", ConfigsParser.EventEmailNotifier, attr2Name, attr2Val);

                String attr3Name[] = new String[]{ConfigsParser.ObjectName, ConfigsParser.DefSecurityCredentials};
                String attr3Val[] = new String[]{"Fiorano.Esb.Bam.Alert.Transport.Jms:ServiceType=JMSTransport,Name=JMSTransport,type=config", "CVuEpOIpQJAIVeUQIs7kBQ=="};
                ConfigsParser.createElementNode(doc, "Container", ConfigsParser.JMS_TRANSPORT_CONFIG, attr3Name, attr3Val);


                ConfigsParser.setAttribute(doc, ConfigsParser.HA_KRPCPROVIDER, ConfigsParser.BackupHAIPAddress, secFESURL);
                ConfigsParser.setAttribute(doc, ConfigsParser.DEFAULT_OBJS_CONFIG, ConfigsParser.BackupServerIp, secFESURL);

                ConfigsParser.setAttribute(doc, ConfigsParser.RPL_HA_MANAGER, ConfigsParser.GatewayServerIPAddress, gatewayMachine);
                ConfigsParser.setAttribute(doc, ConfigsParser.RPL_HA_MANAGER, ConfigsParser.LockFile, lockFilePath);

                break;
            }
            case DeploymentUnit.HA_FES_SECONDARY: {
                String attr1Name[] = new String[]{ConfigsParser.ObjectName, ConfigsParser.Password};
                String attr1Val[] = new String[]{"Fiorano.Esb.Bam.Alert.Transport.Smtp:ServiceType=SMTPTransport," +
                        "Name=SMTPTransport,type=config", ""};
                ConfigsParser.createElementNode(doc, "Container", ConfigsParser.SMTP_TRANSPORT_CONFIG, attr1Name, attr1Val);

                String attr2Name[] = new String[]{ConfigsParser.ObjectName, ConfigsParser.Password};
                String attr2Val[] = new String[]{"Fiorano.Esb.Events:ServiceType=EventEmailNotifier,Name=ESBEventEmailNotifier,type=config", ""};
                ConfigsParser.createElementNode(doc, "Container", ConfigsParser.EventEmailNotifier, attr2Name, attr2Val);

                String attr3Name[] = new String[]{ConfigsParser.ObjectName, ConfigsParser.DefSecurityCredentials};
                String attr3Val[] = new String[]{"Fiorano.Esb.Bam.Alert.Transport.Jms:ServiceType=JMSTransport,Name=JMSTransport,type=config", "CVuEpOIpQJAIVeUQIs7kBQ=="};
                ConfigsParser.createElementNode(doc, "Container", ConfigsParser.JMS_TRANSPORT_CONFIG, attr3Name, attr3Val);

                ConfigsParser.setAttribute(doc, ConfigsParser.HA_KRPCPROVIDER, ConfigsParser.BackupHAIPAddress, priFESURL);
                ConfigsParser.setAttribute(doc, ConfigsParser.DEFAULT_OBJS_CONFIG, ConfigsParser.BackupServerIp, priFESURL);

                ConfigsParser.setAttribute(doc, ConfigsParser.RPL_HA_MANAGER, ConfigsParser.GatewayServerIPAddress, gatewayMachine);
                ConfigsParser.setAttribute(doc, ConfigsParser.RPL_HA_MANAGER, ConfigsParser.LockFile, lockFilePath);

                break;
            }
            case DeploymentUnit.HA_FPS_PRIMARY: {
                ConfigsParser.setAttribute(doc, ConfigsParser.HA_KRPCPROVIDER, ConfigsParser.BackupHAIPAddress, backupPeerUrl);
                ConfigsParser.setAttribute(doc, ConfigsParser.DEFAULT_OBJS_CONFIG, ConfigsParser.BackupServerIp, backupPeerUrl);
                ConfigsParser.setAttribute(doc, ConfigsParser.EnterpriseServer, ConfigsParser.PrimaryURL, "http://" + priFESURL + ":1847");
                ConfigsParser.setAttribute(doc, ConfigsParser.EnterpriseServer, ConfigsParser.BackupURLs, "http://" + secFESURL + ":1848");

                ConfigsParser.setAttribute(doc, ConfigsParser.RPL_HA_MANAGER, ConfigsParser.GatewayServerIPAddress, gatewayMachine);
                ConfigsParser.setAttribute(doc, ConfigsParser.RPL_HA_MANAGER, ConfigsParser.LockFile, lockFilePath);

                break;
            }
            case DeploymentUnit.HA_FPS_SECONDARY: {
                ConfigsParser.setAttribute(doc, ConfigsParser.HA_KRPCPROVIDER, ConfigsParser.BackupHAIPAddress, backupPeerUrl);
                ConfigsParser.setAttribute(doc, ConfigsParser.DEFAULT_OBJS_CONFIG, ConfigsParser.BackupServerIp, backupPeerUrl);
                ConfigsParser.setAttribute(doc, ConfigsParser.EnterpriseServer, ConfigsParser.PrimaryURL, "http://" + priFESURL + ":1847");
                ConfigsParser.setAttribute(doc, ConfigsParser.EnterpriseServer, ConfigsParser.BackupURLs, "http://" + secFESURL + ":1848");

                ConfigsParser.setAttribute(doc, ConfigsParser.RPL_HA_MANAGER, ConfigsParser.GatewayServerIPAddress, gatewayMachine);
                ConfigsParser.setAttribute(doc, ConfigsParser.RPL_HA_MANAGER, ConfigsParser.LockFile, lockFilePath);

                break;
            }
            case DeploymentUnit.STAND_ALONE_FES: {
                break;
            }
            case DeploymentUnit.STAND_ALONE_FPS: {
                ConfigsParser.setAttribute(doc, ConfigsParser.EnterpriseServer, ConfigsParser.PrimaryURL, "http://" + priFESURL + ":1847");
                ConfigsParser.setAttribute(doc, ConfigsParser.EnterpriseServer, ConfigsParser.BackupURLs, "http://" + secFESURL + ":1848");
                //resets the jetty ssl & basicAuthentication values back to default if not enabled
                if (!deploymentUnit.getIsJetty()) {
                    ConfigsParser.setAttribute(doc, ConfigsParser.JETTY_SERVICE_CONFIG, ConfigsParser.SSLEnabled, "false");
                } else if (!deploymentUnit.getIsbasicAuth()) {

                    ConfigsParser.setAttribute(doc, ConfigsParser.JETTY_SERVICE_CONFIG, ConfigsParser.BasicAuthSupported, "false");
                    try {
                        String TestingHome = ESBTestHarness.getTestEnvConfig().getProperty(TestEnvironmentConstants.TESTING_HOME);
                        String FioranoHome = ESBTestHarness.getTestEnvConfig().getProperty(TestEnvironmentConstants.FIORANO_HOME);
                        String jettyhome = TestingHome + "/configuration/profilerConfigs/peer_jetty";
                        String webinfHome = FioranoHome + "/esb/server/jetty/fps/webapps/bcwsgateway/WEB-INF";

                        File inputFile = new File(jettyhome + "/web_old.xml");
                        File outFile = new File(webinfHome + "/web.xml");
                        outFile.delete();
                        File outputFile = new File(webinfHome + "/web.xml");

                        FileReader in = new FileReader(inputFile);
                        FileWriter out = new FileWriter(outputFile);
                        int c;

                        while ((c = in.read()) != -1)
                            out.write(c);

                        in.close();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //configure ssl & perr_jetty settings if set to true
                if (deploymentUnit.getIsSSL()) {
                    configureSSL(doc, deploymentUnit);
                } else if (deploymentUnit.getIsJetty() || deploymentUnit.getIsbasicAuth()) {
                    configureJetty(doc, deploymentUnit, deploymentUnit.getIsJetty(), deploymentUnit.getIsbasicAuth());
                }
                break;
            }
        }

        ConfigsParser.writeXmlFile(doc, configsFilePath);


    }


    private void configureSSL(Document doc, DeploymentUnit deploymentUnit) throws STFException {

        ConfigsParser.setAttribute(doc, ConfigsParser.JETTY_SERVICE_CONFIG, ConfigsParser.KeyPassword, " ");
        ConfigsParser.setAttribute(doc, ConfigsParser.JETTY_SERVICE_CONFIG, ConfigsParser.KeystorePassword, " ");
        ConfigsParser.setAttribute(doc, ConfigsParser.JETTY_SERVICE_CONFIG, ConfigsParser.TruststorePassword, " ");
        ConfigsParser.setAttribute(doc, ConfigsParser.EnterpriseServer, ConfigsParser.ConnectionFactory, "primaryTCF");
        ConfigsParser.setAttribute(doc, ConfigsParser.EnterpriseServer, ConfigsParser.ConnectionRetryCount, "-1");
        ConfigsParser.setAttribute(doc, ConfigsParser.EnterpriseServer, ConfigsParser.ConnectionRetryInterval, "10000");
        ConfigsParser.setAttribute(doc, ConfigsParser.EnterpriseServer, ConfigsParser.InitialContextFactory, "fiorano.jms.runtime.naming.FioranoInitialContextFactory");
        //DependencyHandlerName="AuditEventListener" HandlerType="AuditEventListener">
        ConfigsParser.setAttribute(doc, ConfigsParser.EnterpriseServer, ConfigsParser.ProviderClass, "fiorano.tifosi.provider.fioranoMQ.FioranoProvider");
        ConfigsParser.setAttribute(doc, ConfigsParser.EnterpriseServer, ConfigsParser.QueueConnectionFactory, "primaryQCF");
        ConfigsParser.setAttribute(doc, ConfigsParser.EnterpriseServer, ConfigsParser.ServerName, "FES");
        ConfigsParser.setAttribute(doc, ConfigsParser.EnterpriseServer, ConfigsParser.UseServerlessMode, "false");
        ConfigsParser.setAttribute(doc, ConfigsParser.EnterpriseServer, ConfigsParser.UserName, "anonymous");

        String attr1Name[] = new String[]{ConfigsParser.ObjectName, ConfigsParser.SSLEnabled};
        String attr1Val[] = new String[]{"Fiorano.etc:ServiceType=FMQConfigLoader,Name=FMQConfigLoader2,type=config", "true"};
        ConfigsParser.createElementNode(doc, "Container", ConfigsParser.COMMON_CONFIG, attr1Name, attr1Val);

        String attr2Name[] = new String[]{ConfigsParser.AuditListenerClassName, ConfigsParser.DependencyHandlerName, ConfigsParser.HandlerType};
        String attr2Val[] = new String[]{"com.fiorano.peer.events.AuditEventListener", "AuditEventListener", "AuditEventListener"};
        ConfigsParser.createElementNode(doc, "Container", ConfigsParser.AuditEventListener, attr2Name, attr2Val);
        //Protocol="SUN_SSL"
        String attr3Name[] = new String[]{ConfigsParser.Default, ConfigsParser.FMQServer, ConfigsParser.ObjectName, ConfigsParser.Port, ConfigsParser.Protocol, ConfigsParser.UseForPeerToPeerCommunication};
        String attr3Val[] = new String[]{"false", "false", "Fiorano.socketAcceptors.port-2:ServiceType=ConnectionManager,Name=ConnectionManager2,type=config", "2010", "SUN_SSL", "true"};
        ConfigsParser.createElementNode(doc, "Container", ConfigsParser.CONNECTION_MANAGER, attr3Name, attr3Val);

        String attr4Name[] = new String[]{ConfigsParser.ObjectName, ConfigsParser.Path};
        String attr4Val[] = new String[]{"Fiorano.mq.connection-consumer.FileDBManager:ServiceType=FileDBManager,Name=FileDBManager5,type=config", "CONN_CONSUMER"};
        ConfigsParser.createElementNode(doc, "Container", ConfigsParser.DB, attr4Name, attr4Val);

        String attr5Name[] = new String[]{ConfigsParser.ObjectName, ConfigsParser.Path};
        String attr5Val[] = new String[]{"Fiorano.mq.ptp.databases.file.FileDBManager:ServiceType=FileDBManager,Name=FileDBManager6,type=config", "PTP"};
        ConfigsParser.createElementNode(doc, "Container", ConfigsParser.DB, attr5Name, attr5Val);

        String attr6Name[] = new String[]{ConfigsParser.ObjectName, ConfigsParser.Path};
        String attr6Val[] = new String[]{"Fiorano.mq.pubsub.databases.file.FileDBManager:ServiceType=FileDBManager,Name=FileDBManager4,type=config", "PUBSUB"};
        ConfigsParser.createElementNode(doc, "Container", ConfigsParser.DB, attr6Name, attr6Val);

        String attr7Name[] = new String[]{ConfigsParser.ObjectName, ConfigsParser.Path};
        String attr7Val[] = new String[]{"Fiorano.mq.pubsub.databases.file.FileDBManager:ServiceType=FileDBManager,Name=FileDBManager7,type=config", "PSQ"};
        ConfigsParser.createElementNode(doc, "Container", ConfigsParser.DB, attr7Name, attr7Val);

        String attr8Name[] = new String[]{ConfigsParser.ObjectName, ConfigsParser.Path};
        String attr8Val[] = new String[]{"Fiorano.security.AclManager.FileDBManager:ServiceType=FileDBManager,Name=FileDBManager3,type=config", "SDB/REALM.ACL"};
        ConfigsParser.createElementNode(doc, "Container", ConfigsParser.DB, attr8Name, attr8Val);

        String attr9Name[] = new String[]{ConfigsParser.ObjectName, ConfigsParser.Path};
        String attr9Val[] = new String[]{"Fiorano.security.PrincipalManager.FileDBManager:ServiceType=FileDBManager,Name=FileDBManager2,type=config", "SDB/REALM.PRINCIPAL"};
        ConfigsParser.createElementNode(doc, "Container", ConfigsParser.DB, attr9Name, attr9Val);

        String attr10Name[] = new String[]{ConfigsParser.ObjectName, ConfigsParser.Password};
        String attr10Val[] = new String[]{"Fiorano.jmx.engine:ServiceType=JMXEngine,Name=ClientJMXEngine,type=config", "DMSLzKckc/JLgIUhIpdeyw=="};
        ConfigsParser.createElementNode(doc, "Container", ConfigsParser.JMX_ENGINE, attr10Name, attr10Val);

        String attr11Name[] = new String[]{ConfigsParser.ObjectName, ConfigsParser.Password};
        String attr11Val[] = new String[]{"Fiorano.etc:ServiceType=RdbmsDBManager,Name=RdbmsDBManager,type=config", ""};
        ConfigsParser.createElementNode(doc, "Container", ConfigsParser.DB_MANAGER, attr11Name, attr11Val);

        //now copy configs from testing_home/configuration/profilerConfigs/peer_ssl
        copySSLConfigs(deploymentUnit);

    }

    private void copySSLConfigs(DeploymentUnit deploymentUnit) throws STFException {
        try {
            String TestingHome = ESBTestHarness.getTestEnvConfig().getProperty(TestEnvironmentConstants.TESTING_HOME);
            BufferedReader in = new BufferedReader(new FileReader(TestingHome + "/configuration/profilerConfigs/peer_ssl/toInsert.txt"));
            String entry;
            while ((entry = in.readLine()) != null) {
                process(entry, deploymentUnit);
            }
            in.close();
        } catch (Exception e) {
            throw new STFException("exception while copying ssl configs.", e);
        }

    }

    private void configureJetty(Document doc, DeploymentUnit deploymentUnit, boolean jetty, boolean basicAuth) throws STFException {

        String TestingHome = ESBTestHarness.getTestEnvConfig().getProperty(TestEnvironmentConstants.TESTING_HOME);
        String FioranoHome = ESBTestHarness.getTestEnvConfig().getProperty(TestEnvironmentConstants.FIORANO_HOME);
        String jettyhome = TestingHome + "/configuration/profilerConfigs/peer_jetty";
        String webinfHome = FioranoHome + "/esb/server/jetty/fps/webapps/bcwsgateway/WEB-INF";

        if (jetty) {
            ConfigsParser.setAttribute(doc, ConfigsParser.JETTY_SERVICE_CONFIG, ConfigsParser.KeyPassword, "DMSLzKckc/JLgIUhIpdeyw==");
            ConfigsParser.setAttribute(doc, ConfigsParser.JETTY_SERVICE_CONFIG, ConfigsParser.KeystorePassword, "DMSLzKckc/JLgIUhIpdeyw==");
            ConfigsParser.setAttribute(doc, ConfigsParser.JETTY_SERVICE_CONFIG, ConfigsParser.TruststorePassword, "DMSLzKckc/JLgIUhIpdeyw==");
            ConfigsParser.setAttribute(doc, ConfigsParser.JETTY_SERVICE_CONFIG, ConfigsParser.KeystoreLocation, jettyhome + "/kcckeys");
            ConfigsParser.setAttribute(doc, ConfigsParser.JETTY_SERVICE_CONFIG, ConfigsParser.SSLEnabled, "true");
            ConfigsParser.setAttribute(doc, ConfigsParser.JETTY_SERVICE_CONFIG, ConfigsParser.TrustStoreLocation, jettyhome + "/kcckeys");
        }

        if (basicAuth) {
            ConfigsParser.setAttribute(doc, ConfigsParser.JETTY_SERVICE_CONFIG, ConfigsParser.BasicAuthSupported, "true");
            ConfigsParser.setAttribute(doc, ConfigsParser.JETTY_SERVICE_CONFIG, ConfigsParser.RealmProperties, jettyhome + "/realm.properties");

            try {
                File inputFile = new File(jettyhome + "/web_basicauth.xml");
                File outFile = new File(webinfHome + "/web.xml");
                outFile.delete();
                File outputFile = new File(webinfHome + "/web.xml");

                FileReader in = new FileReader(inputFile);
                FileWriter out = new FileWriter(outputFile);
                int c;

                while ((c = in.read()) != -1)
                    out.write(c);

                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        ConfigsParser.setAttribute(doc, ConfigsParser.EnterpriseServer, ConfigsParser.ConnectionFactory, "primaryTCF");
        ConfigsParser.setAttribute(doc, ConfigsParser.EnterpriseServer, ConfigsParser.ConnectionRetryCount, "-1");
        ConfigsParser.setAttribute(doc, ConfigsParser.EnterpriseServer, ConfigsParser.ConnectionRetryInterval, "10000");
        ConfigsParser.setAttribute(doc, ConfigsParser.EnterpriseServer, ConfigsParser.InitialContextFactory, "fiorano.jms.runtime.naming.FioranoInitialContextFactory");
        //DependencyHandlerName="AuditEventListener" HandlerType="AuditEventListener">
        ConfigsParser.setAttribute(doc, ConfigsParser.EnterpriseServer, ConfigsParser.ProviderClass, "fiorano.tifosi.provider.fioranoMQ.FioranoProvider");
        ConfigsParser.setAttribute(doc, ConfigsParser.EnterpriseServer, ConfigsParser.QueueConnectionFactory, "primaryQCF");
        ConfigsParser.setAttribute(doc, ConfigsParser.EnterpriseServer, ConfigsParser.ServerName, "FES");
        ConfigsParser.setAttribute(doc, ConfigsParser.EnterpriseServer, ConfigsParser.UseServerlessMode, "false");
        ConfigsParser.setAttribute(doc, ConfigsParser.EnterpriseServer, ConfigsParser.UserName, "anonymous");


        /*String attr4Name[] = new String[]{ConfigsParser.ObjectName, ConfigsParser.Path};
        String attr4Val[] = new String[]{"Fiorano.mq.connection-consumer.FileDBManager:ServiceType=FileDBManager,Name=FileDBManager5,type=config", "CONN_CONSUMER"};
        ConfigsParser.createElementNode(doc, "Container", ConfigsParser.DB, attr4Name, attr4Val);

        String attr5Name[] = new String[]{ConfigsParser.ObjectName, ConfigsParser.Path};
        String attr5Val[] = new String[]{"Fiorano.mq.ptp.databases.file.FileDBManager:ServiceType=FileDBManager,Name=FileDBManager6,type=config", "PTP"};
        ConfigsParser.createElementNode(doc, "Container", ConfigsParser.DB, attr5Name, attr5Val);

        String attr6Name[] = new String[]{ConfigsParser.ObjectName, ConfigsParser.Path};
        String attr6Val[] = new String[]{"Fiorano.mq.pubsub.databases.file.FileDBManager:ServiceType=FileDBManager,Name=FileDBManager4,type=config", "PUBSUB"};
        ConfigsParser.createElementNode(doc, "Container", ConfigsParser.DB, attr6Name, attr6Val);

        String attr7Name[] = new String[]{ConfigsParser.ObjectName, ConfigsParser.Path};
        String attr7Val[] = new String[]{"Fiorano.mq.pubsub.databases.file.FileDBManager:ServiceType=FileDBManager,Name=FileDBManager7,type=config", "PSQ"};
        ConfigsParser.createElementNode(doc, "Container", ConfigsParser.DB, attr7Name, attr7Val);

        String attr8Name[] = new String[]{ConfigsParser.ObjectName, ConfigsParser.Path};
        String attr8Val[] = new String[]{"Fiorano.security.AclManager.FileDBManager:ServiceType=FileDBManager,Name=FileDBManager3,type=config", "SDB/REALM.ACL"};
        ConfigsParser.createElementNode(doc, "Container", ConfigsParser.DB, attr8Name, attr8Val);

        String attr9Name[] = new String[]{ConfigsParser.ObjectName, ConfigsParser.Path};
        String attr9Val[] = new String[]{"Fiorano.security.PrincipalManager.FileDBManager:ServiceType=FileDBManager,Name=FileDBManager2,type=config", "SDB/REALM.PRINCIPAL"};
        ConfigsParser.createElementNode(doc, "Container", ConfigsParser.DB, attr9Name, attr9Val);*/

        String attr2Name[] = new String[]{ConfigsParser.AuditListenerClassName, ConfigsParser.DependencyHandlerName, ConfigsParser.HandlerType};
        String attr2Val[] = new String[]{"com.fiorano.peer.events.AuditEventListener", "AuditEventListener", "AuditEventListener"};
        ConfigsParser.createElementNode(doc, "Container", ConfigsParser.AuditEventListener, attr2Name, attr2Val);

        String attr10Name[] = new String[]{ConfigsParser.ObjectName, ConfigsParser.Password};
        String attr10Val[] = new String[]{"Fiorano.jmx.engine:ServiceType=JMXEngine,Name=ClientJMXEngine,type=config", "DMSLzKckc/JLgIUhIpdeyw=="};
        ConfigsParser.createElementNode(doc, "Container", ConfigsParser.JMX_ENGINE, attr10Name, attr10Val);

        String attr11Name[] = new String[]{ConfigsParser.ObjectName, ConfigsParser.Password};
        String attr11Val[] = new String[]{"Fiorano.etc:ServiceType=RdbmsDBManager,Name=RdbmsDBManager,type=config", ""};
        ConfigsParser.createElementNode(doc, "Container", ConfigsParser.DB_MANAGER, attr11Name, attr11Val);

    }

    private void process(String entry, DeploymentUnit deploymentUnit) throws IOException {

        String TestingHome = ESBTestHarness.getTestEnvConfig().getProperty(TestEnvironmentConstants.TESTING_HOME);
        String fioranoHome = ESBTestHarness.getTestEnvConfig().getProperty(TestEnvironmentConstants.FIORANO_HOME);
        String listFileLocation = fioranoHome + "/esb/server/profiles/" + deploymentUnit.getProfileDir() + "/FPS/deploy/FioranoMQ.lst";
        File listFile = new File(listFileLocation);

        if (!listFile.exists()) {
            throw new FileNotFoundException("Failed to locate list File: " + listFileLocation);
        }

        String arr[] = entry.split("=");
        BufferedWriter out = new BufferedWriter(new FileWriter(listFile, true));
        out.write("../../../" + arr[0] + "\n");
        out.close();
        String fileToCopy = arr[1];
        String fileName = fileToCopy.substring(fileToCopy.lastIndexOf("/"));
        fileToCopy = fileToCopy.replace("${FIORANO_HOME}", fioranoHome.replace("\\", "/"));
        copyFile(new File(TestingHome + "/configuration/profilerConfigs/peer_ssl/" + fileName), new File(fileToCopy));
    }

    private void copyFile(File source, File dest) throws IOException {

        if (!dest.exists()) {
            dest.createNewFile();
        }

        InputStream in = null;
        OutputStream out = null;

        try {

            in = new FileInputStream(source);

            out = new FileOutputStream(dest);

            byte[] buf = new byte[1024];

            int len;

            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

        }

        finally {

            try {
                in.close();
            } catch (IOException ignore) {

            }

            try {
                out.close();
            } catch (IOException ignore) {

            }

        }

    }

    public void mountlock(String gatewayServerAddress, String serverAddress, String serverLockFilePath, String gatewayMachineOS, String shareName) {
        System.out.println("Mounting lockfile..." + serverLockFilePath);

        String lockcomm = TestEnvironmentConstants.HA_MOUNT_LOCK;
        int port = TestEnvironmentConstants.ANT_SERVER_PORT;

        ClientTask clientTask = new ClientTask();
        RunTargetCommand command = new RunTargetCommand();
        command.setTarget(lockcomm);

        Vector properties = new Vector();
        addProperty(properties, TestEnvironmentConstants.GATEWAY_IP_ADD, gatewayServerAddress);
//        addProperty(properties, TestEnvironmentConstants.GATEWAY_FILE_PATH, gateWayLockFilePath);
        addProperty(properties, TestEnvironmentConstants.GATEWAY_MACHINE_OS, gatewayMachineOS);
        addProperty(properties, TestEnvironmentConstants.SHARE_NAME, shareName);
        addProperty(properties, TestEnvironmentConstants.MOUNT_POINT, serverLockFilePath);
        addProperty(properties, TestEnvironmentConstants.HA_MOUNT_LOCK, lockcomm);

        if (properties != null)
            command.setProperties(properties);

        clientTask.addConfiguredRunTarget(command);


        Project project = new Project();
        project.init();

        try {
            clientTask.setPort(port);
            clientTask.setMachine(serverAddress);
            clientTask.setProject(project);
            clientTask.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void flushFilters(String machineAddress) {
        String changecomm = TestEnvironmentConstants.FLUSH_FILTERS;
        int port = TestEnvironmentConstants.ANT_SERVER_PORT;

        ClientTask clientTask = new ClientTask();
        RunTargetCommand command = new RunTargetCommand();
        command.setTarget(changecomm);

        clientTask.addConfiguredRunTarget(command);

        Project project = new Project();
        project.init();

        clientTask.setPort(port);
        clientTask.setMachine(machineAddress);
        clientTask.setProject(project);
        clientTask.execute();

    }

    /**
     * @param machineAddress
     * @param sourceAddress
     * @param portTobeChanged
     * @param rule
     * @deprecated Please use method modifyFireWallRule instead
     */
    public void modifyFilters(String machineAddress, String sourceAddress, int portTobeChanged, String rule) {

        String changecomm = TestEnvironmentConstants.HA_MODIFY_FILTER;
        int port = TestEnvironmentConstants.ANT_SERVER_PORT;

        ClientTask clientTask = new ClientTask();
        RunTargetCommand command = new RunTargetCommand();
        command.setTarget(changecomm);

        Vector properties = new Vector();
        addProperty(properties, TestEnvironmentConstants.SOURCE_IP_ADD, sourceAddress);
        addProperty(properties, TestEnvironmentConstants.PORT_TO_CHANGE, Integer.toString(portTobeChanged));
        addProperty(properties, TestEnvironmentConstants.RULE_TO_ADD, rule);

        if (properties != null)
            command.setProperties(properties);
        clientTask.addConfiguredRunTarget(command);

        Project project = new Project();
        project.init();

        clientTask.setPort(port);
        clientTask.setMachine(machineAddress);
        clientTask.setProject(project);
        clientTask.execute();
    }


    /**
     * <br> Consider Machine A & B. let machineAddress be ip address of A & sourceAddress be ip address of B </br>
     * <br>It blocks out going traffic from machine A to a port on machine B which is specified by <param>portTobeChanged</param>.</br>
     * <br> It also blocks traffic incoming from Machine B to a port on machine A which is specified by <param>localPortTobeChanged</param>. </br>
     *
     * @param machineAddress
     * @param sourceAddress
     * @param portTobeChanged
     * @param localPortTobeChanged
     * @param rule                 can take 2 values either 'A' or 'D'. A - specifies rule has to be added, D - specifies rule has to be deleleted.
     */
    public void modifyFireWallRule(String machineAddress, String sourceAddress, int portTobeChanged, int localPortTobeChanged, String rule) {

        String changecomm = TestEnvironmentConstants.HA_MODIFY_FILTER1;
        int port = TestEnvironmentConstants.ANT_SERVER_PORT;

        ClientTask clientTask = new ClientTask();
        RunTargetCommand command = new RunTargetCommand();
        command.setTarget(changecomm);

        Vector properties = new Vector();
        addProperty(properties, TestEnvironmentConstants.SOURCE_IP_ADD, sourceAddress);
        addProperty(properties, TestEnvironmentConstants.PORT_TO_CHANGE, Integer.toString(portTobeChanged));
        addProperty(properties, TestEnvironmentConstants.LOCAL_PORT_TO_CHANGE, Integer.toString(localPortTobeChanged));
        addProperty(properties, TestEnvironmentConstants.RULE_TO_ADD, rule);

        if (properties != null)
            command.setProperties(properties);
        clientTask.addConfiguredRunTarget(command);

        Project project = new Project();
        project.init();

        clientTask.setPort(port);
        clientTask.setMachine(machineAddress);
        clientTask.setProject(project);
        clientTask.execute();
    }


    public void modifyGateway(String machineAddress, String gatewayIP, String rule) {

        String comm = TestEnvironmentConstants.HA_MODIFY_GATEWAY;
        int port = TestEnvironmentConstants.ANT_SERVER_PORT;

        ClientTask clientTask = new ClientTask();
        RunTargetCommand command = new RunTargetCommand();
        command.setTarget(comm);

        Vector properties = new Vector();
        addProperty(properties, TestEnvironmentConstants.RULE_TO_ADD, rule);
        addProperty(properties, TestEnvironmentConstants.GATEWAY_IP_ADD, gatewayIP);
        if (properties != null)
            command.setProperties(properties);

        clientTask.addConfiguredRunTarget(command);


        Project project = new Project();
        project.init();

        clientTask.setPort(port);
        clientTask.setMachine(machineAddress);
        clientTask.setProject(project);
        clientTask.execute();

    }


    public void startServerOnRemote(String remoteMachineAddress, String profileType, String profileName, boolean isWrapper, boolean isHA) {

        System.out.println("Starting Server ::" + profileName + " on Machine::" + remoteMachineAddress);
        Vector properties = new Vector();
        String binDirPath;
        String startUpCommand;
        String installedServiceName;
        if (profileType.equalsIgnoreCase(TestEnvironmentConstants.FES)) {
            binDirPath = TestEnvironmentConstants.FES_BIN_PATH;
            startUpCommand = TestEnvironmentConstants.FES_STARTUP_COMMAND;
        }
        else if (profileType.equalsIgnoreCase(TestEnvironmentConstants.FPS)){
            binDirPath = TestEnvironmentConstants.FPS_BIN_PATH;
            startUpCommand = TestEnvironmentConstants.FPS_STARTUP_COMMAND;
        }
        else if (profileType.equalsIgnoreCase(TestEnvironmentConstants.AMS)){
            binDirPath = TestEnvironmentConstants.AMS_BIN_PATH;
            startUpCommand = TestEnvironmentConstants.AMS_STARTUP_COMMAND;
        }
        else {
            binDirPath = TestEnvironmentConstants.AMS_BIN_PATH;
            startUpCommand = TestEnvironmentConstants.AMS_STARTUP_COMMAND;
        }

        if (isWrapper) {
            if (isHA) {
                installedServiceName = profileName.replace('/', '_');
            } else {
                installedServiceName = profileName;
            }
            addProperty(properties, TestEnvironmentConstants.INSTALLED_SERVICE_NAME, installedServiceName);
            addProperty(properties, TestEnvironmentConstants.IS_HA, String.valueOf(isHA));
        }
        addProperty(properties, TestEnvironmentConstants.BIN_PATH, binDirPath);
        addProperty(properties, TestEnvironmentConstants.STARTUP_COMMAND, startUpCommand);
        addProperty(properties, TestEnvironmentConstants.SERVER_PROFILE, profileName);
//        addProperty(properties, TestEnvironmentConstants.MODE, profileType.toUpperCase());
        addProperty(properties, TestEnvironmentConstants.MODE, profileType.toLowerCase());
        addProperty(properties, TestEnvironmentConstants.IS_WRAPPER, String.valueOf(isWrapper));

        callRemoteAntTarget(remoteMachineAddress, TestEnvironmentConstants.START_SERVER_TASK, properties);

    }

    //overloaded method - used when we want to lauch a server from a diff FIORANO_HOME & not from FIORANO_HOME which is mentioned in test.properties file.

    public void startServerOnRemote(String remoteMachineAddress, String profileType, String profileName, boolean isWrapper, boolean isHA, TestEnvironmentConfig testenvConfig) {

        System.out.println("Starting Server ::" + profileType + "," + profileName + " on Machine::" + remoteMachineAddress);
        Vector properties = new Vector();
        String binDirPath;
        String startUpCommand;
        String installedServiceName;
        if (profileType.equalsIgnoreCase(TestEnvironmentConstants.FES)) {
            binDirPath = TestEnvironmentConstants.FES_BIN_PATH;
            startUpCommand = TestEnvironmentConstants.FES_STARTUP_COMMAND;
        } else {
            binDirPath = TestEnvironmentConstants.FPS_BIN_PATH;
            startUpCommand = TestEnvironmentConstants.FPS_STARTUP_COMMAND;
        }
        if (profileType.equalsIgnoreCase(TestEnvironmentConstants.AMS)){
            binDirPath = TestEnvironmentConstants.AMS_BIN_PATH;
            startUpCommand = TestEnvironmentConstants.AMS_STARTUP_COMMAND;
        }
        if (isWrapper) {
            if (isHA) {
                installedServiceName = profileName.replace('/', '_');
            } else {
                installedServiceName = profileName;
            }
            addProperty(properties, TestEnvironmentConstants.INSTALLED_SERVICE_NAME, installedServiceName);
            addProperty(properties, TestEnvironmentConstants.IS_HA, String.valueOf(isHA));
        }
        addProperty(properties, TestEnvironmentConstants.FIORANO_HOME, testenvConfig.getProperty(TestEnvironmentConstants.FIORANO_HOME));
        addProperty(properties, TestEnvironmentConstants.BIN_PATH, binDirPath);
        addProperty(properties, TestEnvironmentConstants.STARTUP_COMMAND, startUpCommand);
        addProperty(properties, TestEnvironmentConstants.SERVER_PROFILE, profileName);
        addProperty(properties, TestEnvironmentConstants.MODE, profileType.toUpperCase());
        addProperty(properties, TestEnvironmentConstants.IS_WRAPPER, String.valueOf(isWrapper));

        callRemoteAntTarget(remoteMachineAddress, TestEnvironmentConstants.START_SERVER_TASK, properties);

    }

    public void initStartUpOnRemote(String remoteMachineAddress, String StartupDir, String StartupCmd) {


        Vector properties = new Vector();
        addProperty(properties, TestEnvironmentConstants.INIT_STARTUP_PATH, StartupDir);
        addProperty(properties, TestEnvironmentConstants.INIT_STARTUP_COMMAND, StartupCmd);
        callRemoteAntTarget(remoteMachineAddress, TestEnvironmentConstants.START_PRILIMNARY_STEPS, properties);

    }

    public void initCleanUpOnRemote(String remoteMachineAddress, String CleanupDir, String CleanupCmd) {
        Vector properties = new Vector();
        addProperty(properties, TestEnvironmentConstants.INIT_CLEANUP_PATH, CleanupDir);
        addProperty(properties, TestEnvironmentConstants.INIT_CLEANUP_COMMAND, CleanupCmd);
        callRemoteAntTarget(remoteMachineAddress, TestEnvironmentConstants.CLEAN_PRILIMNARY_STEPS, properties);
    }

    public void stopFES(String remoteMachineAddress, boolean isWrapper) {
        stopServerOnRemote(remoteMachineAddress, TestEnvironmentConstants.FES, "profile1", isWrapper, false);
    }

    public void stopFESHAProfile(String remoteMachineAddress, String profileName, boolean isWrapper) {
        stopServerOnRemote(remoteMachineAddress, TestEnvironmentConstants.FES, profileName, isWrapper, true);
    }


    public void stopFPS(String remoteMachineAddress, boolean isWrapper) {
        stopServerOnRemote(remoteMachineAddress, TestEnvironmentConstants.FPS, "profile1", isWrapper, false);
    }

    public void stopFPSHAProfile(String remoteMachineAddress, String profileName, boolean isWrapper) {
        stopServerOnRemote(remoteMachineAddress, TestEnvironmentConstants.FPS, profileName, isWrapper, true);
    }

    private void stopServerOnRemote(String remoteMachineAddress, String profileType, String profileName, boolean isWrapper, boolean isHA) {
        Vector properties = new Vector();

        int serverRMIPort = 0;
        String installedServiceName;
        addProperty(properties, TestEnvironmentConstants.IS_WRAPPER, String.valueOf(isWrapper));
        addProperty(properties, TestEnvironmentConstants.SERVER_PROFILE, profileName);

        if (isHA) {
            installedServiceName = profileName.replace('/', '_');
        } else {
            installedServiceName = profileName;
        }
        addProperty(properties, TestEnvironmentConstants.INSTALLED_SERVICE_NAME, installedServiceName);

        if (profileType.equalsIgnoreCase(TestEnvironmentConstants.FES)) {
            if (profileName.equalsIgnoreCase("haprofile1/primary") || profileName.equalsIgnoreCase("profile1"))
                serverRMIPort = 2047;
            if (profileName.equalsIgnoreCase("haprofile1/secondary"))
                serverRMIPort = 2048;

            addProperty(properties, TestEnvironmentConstants.RMIPORT, String.valueOf(serverRMIPort));
            shutdownEnterpriseServerOnRemote(remoteMachineAddress, properties);

        } else {
            if (profileName.equalsIgnoreCase("haprofile1/primary") || profileName.equalsIgnoreCase("profile1"))
                serverRMIPort = 2067;
            if (profileName.equalsIgnoreCase("haprofile1/secondary"))
                serverRMIPort = 2068;
            if (profileName.equalsIgnoreCase("haprofile2/primary") || profileName.equalsIgnoreCase("profile2"))
                serverRMIPort = 2077;
            if (profileName.equalsIgnoreCase("haprofile2/secondary"))
                serverRMIPort = 2078;

            addProperty(properties, TestEnvironmentConstants.RMIPORT, String.valueOf(serverRMIPort));
            shutdownPeerOnRemote(remoteMachineAddress, properties);
        }
    }

    public void shutdownServerOnRemote(String remoteMachineAddress, String profileType, String profileName, boolean isWrapper, boolean isHA) {
        Vector properties = new Vector();

        int serverRMIPort = 0;
        String installedServiceName;
        addProperty(properties, TestEnvironmentConstants.IS_WRAPPER, String.valueOf(isWrapper));
        addProperty(properties, TestEnvironmentConstants.SERVER_PROFILE, profileName);

        if (isHA) {
            installedServiceName = profileName.replace('/', '_');
        } else {
            installedServiceName = profileName;
        }
        addProperty(properties, TestEnvironmentConstants.INSTALLED_SERVICE_NAME, installedServiceName);

        if (profileType.equalsIgnoreCase(TestEnvironmentConstants.FES)) {
            if (profileName.equalsIgnoreCase("haprofile1/primary") || profileName.equalsIgnoreCase("profile1"))
                serverRMIPort = 2047;
            if (profileName.equalsIgnoreCase("haprofile1/secondary"))
                serverRMIPort = 2048;

            addProperty(properties, TestEnvironmentConstants.RMIPORT, String.valueOf(serverRMIPort));
            shutdownEnterpriseServerOnRemote(remoteMachineAddress, properties);

        } else {
            if (profileName.equalsIgnoreCase("haprofile1/primary") || profileName.equalsIgnoreCase("profile1"))
                serverRMIPort = 2067;
            if (profileName.equalsIgnoreCase("haprofile1/secondary"))
                serverRMIPort = 2068;
            if (profileName.equalsIgnoreCase("haprofile2/primary") || profileName.equalsIgnoreCase("profile2"))
                serverRMIPort = 2077;
            if (profileName.equalsIgnoreCase("haprofile2/secondary"))
                serverRMIPort = 2078;

            addProperty(properties, TestEnvironmentConstants.RMIPORT, String.valueOf(serverRMIPort));
            shutdownPeerOnRemote(remoteMachineAddress, properties);
        }

        if (profileType.equalsIgnoreCase(TestEnvironmentConstants.AMS))
        {
            if (profileName.equalsIgnoreCase("server1"))
            {
                serverRMIPort=2347;
            }
            addProperty(properties, TestEnvironmentConstants.RMIPORT, String.valueOf(serverRMIPort));
            shutdownEnterpriseServerOnRemote(remoteMachineAddress, properties);
        }
        if (profileType.equalsIgnoreCase(TestEnvironmentConstants.AGS))
        {
            if (profileName.equalsIgnoreCase("server1"))
            {
                serverRMIPort=2367;
            }
            addProperty(properties, TestEnvironmentConstants.RMIPORT, String.valueOf(serverRMIPort));
            shutdownPeerOnRemote(remoteMachineAddress, properties);
        }

    }

    private void shutdownEnterpriseServerOnRemote(String remoteMachineAddress, Vector properties) {

        addProperty(properties, TestEnvironmentConstants.MODE, TestEnvironmentConstants.FES.toLowerCase());
        callRemoteAntTarget(remoteMachineAddress, TestEnvironmentConstants.ENTERPRISE_SERVER_SHUTDOWN_COMMAND, properties);
    }

    private void shutdownPeerOnRemote(String remoteMachineAddress, Vector properties) {

        addProperty(properties, TestEnvironmentConstants.MODE, TestEnvironmentConstants.FPS.toLowerCase());
        callRemoteAntTarget(remoteMachineAddress, TestEnvironmentConstants.PEER_SHUTDOWN_COMMAND, properties);
    }


    public void prepareProfilesOnRemote(String priFESURL, String secFESURL, String machineToDeployOn) {
        Vector properties = new Vector();
        addProperty(properties, "FES_PRIMARY_URL", priFESURL);
        addProperty(properties, "FES_SECONDARY_URL", secFESURL);
        callRemoteAntTarget(machineToDeployOn, TestEnvironmentConstants.PREPARE_PROFILE_TASK, properties);
    }

    private void addProperty(Vector properties, String name, String value) {
        PropertyContainer property = new PropertyContainer();
        property.setName(name);
        property.setValue(value);
        properties.add(property);
    }


    private String getZipFile(String profilePath) throws STFException {
        String outputZipPath = "temp_" + System.currentTimeMillis() + ".zip";
        File zipFile = new File(outputZipPath);
        try {
            zipFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(zipFile));
        } catch (FileNotFoundException e) {
            //this should never happen.
            e.printStackTrace();
        }
        try {
            ZipUtil.zipDir(profilePath, null, zos, 1024);
        } catch (IOException e) {
            //Unable to create the zip File
            throw new STFException("Unable to create zip File Because of::" + e.getMessage(), e);
        } finally {
            try {
                zos.close();
            } catch (IOException e) {
                //go to hell
            }
        }
        return outputZipPath;

    }

    private void sendFile(String machineName, String fileName, String toDir) {

        ClientTask clientTask = new ClientTask();

        SendFileCommand command = new SendFileCommand();
        command.setFile(new File(fileName));
        command.setTodir(toDir);
        command.setTofile(fileName);

        clientTask.addConfiguredSendFile(command);

        remoteTaskExecution(clientTask, machineName);

    }

    private void callRemoteAntTarget(String machine, String targetName, Vector properties) {

        ClientTask clientTask = new ClientTask();

        RunTargetCommand command = new RunTargetCommand();
        command.setTarget(targetName);
        if (properties != null)
            command.setProperties(properties);

        clientTask.addConfiguredRunTarget(command);

        remoteTaskExecution(clientTask, machine);

    }

    private void remoteTaskExecution(ClientTask clientTask, String machineName) {

        int port = TestEnvironmentConstants.ANT_SERVER_PORT;
        Project project = new Project();
        project.init();

        clientTask.setMachine(machineName);
        clientTask.setPort(port);

        clientTask.setProject(project);
        try{
            clientTask.execute();

        } catch (Exception e){
            if((e.getCause() != null && e.getCause() instanceof IOException)) {
                //ignore
            }
        }
    }

    public void unMountlock(String serverAddress, String serverLockFilePath) {
        System.out.println("Un-Mounting lockfile..." + serverLockFilePath);

        String lockcomm = TestEnvironmentConstants.HA_UNMOUNT_LOCK;
        int port = TestEnvironmentConstants.ANT_SERVER_PORT;

        ClientTask clientTask = new ClientTask();
        RunTargetCommand command = new RunTargetCommand();
        command.setTarget(lockcomm);

        Vector properties = new Vector();
        addProperty(properties, TestEnvironmentConstants.MOUNT_POINT, serverLockFilePath);
        addProperty(properties, TestEnvironmentConstants.HA_MOUNT_LOCK, lockcomm);

        if (properties != null)
            command.setProperties(properties);

        clientTask.addConfiguredRunTarget(command);


        Project project = new Project();
        project.init();

        try {
            clientTask.setPort(port);
            clientTask.setMachine(serverAddress);
            clientTask.setProject(project);

            clientTask.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if((e.getCause() != null && e.getCause() instanceof IOException)) {
                //ignore
            }
        }

    }

    public void antRegister(String serverAddress, String targetName, String customDirectory, String outlog) {
        Vector properties = new Vector();
        String output = outlog;
        addProperty(properties, TestEnvironmentConstants.CUSTOM_COMPONENT_DIRECTORY, customDirectory);
        addProperty(properties, TestEnvironmentConstants.OUTPUT_LOG, output);
        callRemoteAntTarget(serverAddress, targetName, properties);
    }

    public void antreRegister(String serverAddress, String targetName, String customDirectory, String outlog) {
        Vector properties = new Vector();
        String output = outlog;
        addProperty(properties, TestEnvironmentConstants.CUSTOM_COMPONENT_DIRECTORY, customDirectory);
        addProperty(properties, TestEnvironmentConstants.OUTPUT_LOG, output);
        callRemoteAntTarget(serverAddress, targetName, properties);
    }

    public void antunRegister(String serverAddress, String targetName, String customDirectory, String outlog) {
        Vector properties = new Vector();
        String output = outlog;
        addProperty(properties, TestEnvironmentConstants.CUSTOM_COMPONENT_DIRECTORY, customDirectory);
        addProperty(properties, TestEnvironmentConstants.OUTPUT_LOG, output);
        callRemoteAntTarget(serverAddress, targetName, properties);
    }
}

/*
*&lt;taskdef name="trycatch" 
* classname="net.sf.antcontrib.logic.TryCatchTask" /&gt;
 * </code></pre>
 *
  * <h3>Crude Example</h3>
  *
  * <pre><code>
  * &lt;trycatch property=&quot;foo&quot; reference=&quot;bar&quot;&gt;
  * &lt;try&gt;
 * &lt;fail&gt;Tada!&lt;/fail&gt;
  * &lt;/try&gt;
  *
 * &lt;catch&gt;
  * &lt;echo&gt;In &amp;lt;catch&amp;gt;.&lt;/echo&gt;
  * &lt;/catch&gt;
 *
  * &lt;finally&gt;
  * &lt;echo&gt;In &amp;lt;finally&amp;gt;.&lt;/echo&gt;
  * &lt;/finally&gt;
  * &lt;/trycatch&gt;
  *
  * &lt;echo&gt;As property: ${foo}&lt;/echo&gt;
  * &lt;property name=&quot;baz&quot; refid=&quot;bar&quot; /&gt;
  * &lt;echo&gt;From reference: ${baz}&lt;/echo&gt;
  * </code></pre>
  *
  * <p>results in</p>
  *
  * <pre><code>
  * [trycatch] Caught exception: Tada!
  * [echo] In &lt;catch&gt;.
  * [echo] In &lt;finally&gt;.
  * [echo] As property: Tada!
  * [echo] From reference: Tada!
*/
