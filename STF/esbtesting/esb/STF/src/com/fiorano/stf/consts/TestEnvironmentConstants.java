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
package com.fiorano.stf.consts;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: May 8, 2007
 * Time: 12:10:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestEnvironmentConstants {

    public static final String TESTING_HOME = "TESTING_HOME";
    public static final String FIORANO_HOME = "FIORANO_HOME";
    public static final String TEST_ENVIRONMENT = "TestEnvironment";
    public static final String SERVERS = "Servers";
    public static final String SERVER = "Server";
    public static final String MACHINES = "Machines";
    public static final String MACHINE = "Machine";
    public static final String HA_SERVER = "HAServer";
    public static final String HA_PRIMARY = "Primary";
    public static final String HA_SECONDARY = "Secondary";
    public static final String GATEWAY = "Gateway";
    public static final String MODE = "mode";
    public static final String IS_HA = "isHA";
    public static final String IS_WRAPPER = "isWrapper";
    public static final String PRIMARY_FESURL = "PrimaryFES";
    public static final String SECONDARY_FESURL = "SecondaryFES";
    public static final String URL = "url";
    public static final String RMIPORT = "rmiPort";

    public static final String LOCK_FILE = "lockFile";
    public static final String ssl = "ssl";
    public static final String jetty = "jetty";
    public static final String basicAuth = "basicAuth";


    //Introducing for API Servers
    public static final String AMS = "ams";
    public static final String AGS = "ags";
    public static final String PROFILE = "Profile";
    public static final String HA_PROFILE = "HAProfile";
    public static final String PROFILES = "Profiles";
    public static final String FES = "fes";
    public static final String FPS = "fps";
    public static final String HAPROFILE = "haprofile";
    public static final String PROFILE1 = "profile1";
    public static final String PROFILE2 = "profile2";
    public static final String HAPROFILE1 = "haprofile1";
    public static final String HAPROFILE2 = "haprofile2";
    public static final String ENTERPRISE_SERVER = "EnterpriseServer";
    public static final String HA_PROFILE_NAME = "ProfileName";
    public static final String NAME = "name";
    public static final String PROFILE_ID = "profileID";
    public static final String MOUNT_POINT = "mountPoint";

    public static final String HA = "ha";
    public static final String REF = "ref";
    public static final String ADDRESS = "address";
    public static final String IS_WINDOWS = "isWindows";
    public static final String SHARE_NAME = "shareName";

    public static final String PROFILE_NAME = "profileName";
    public static final Object SERVER_NAME = "serverName";

    //The port at which ant server should be running on all the machines
    public static final int ANT_SERVER_PORT = 9876;

    //AntTasks.
    public static final String PREPARE_PROFILE_TASK = "prepareProfiles";
    public static final String TYPE = "type";
    public static final String TO_DIR = "toDir";
    public static final String ZIP_FILE_PATH = "zipFilePath";
    public static final String START_SERVER_TASK = "startServer";
    public static final String BIN_PATH = "server.bin.path";
    public static final String STARTUP_COMMAND = "server.startup.command";
    public static final String SERVER_PROFILE = "server.profile";
    public static final String INSTALLED_SERVICE_NAME = "installed.service.name";
    public static final String FES_BIN_PATH = "esb/fes/bin";
    public static final String AMS_BIN_PATH = "esb/server/bin";
    public static final String FPS_BIN_PATH = "esb/fps/bin";
    public static final String FES_STARTUP_COMMAND = "fes";
    public static final String FPS_STARTUP_COMMAND = "fps";
    public static final String AMS_STARTUP_COMMAND = "server";

    public static final String FES_SHUTDOWN_COMMAND = "stopFES";
    public static final String HA_MOUNT_LOCK = "mountLock";
    public static final String HA_UNMOUNT_LOCK = "unMountLock";
    public static final String GATEWAY_IP_ADD = "gateway.ip.add";
    public static final String GATEWAY_MACHINE_OS = "gateway.machine.os";

    public static final String SOURCE_IP_ADD = "source.ip.add";
    public static final String PORT_TO_CHANGE = "port.change";
    public static final String LOCAL_PORT_TO_CHANGE = "local.port.change";
    //    public static final String GATEWAY_FILE_PATH = "gateway.file.path"; //deprecated
    public static final String HA_MODIFY_FILTER = "modifyFilter";
    /**
     * target HA_MODIFY_FILTER has been deprecated. use HA_MODIFY_FILTER1 *
     */
    public static final String HA_MODIFY_FILTER1 = "modifyFilter1";
    public static final String FLUSH_FILTERS = "flushFilters";
    public static final String RULE_TO_ADD = "rule";
    public static final String HA_MODIFY_GATEWAY = "modifyGateway";

    public static final String FESPrimary_SHUTDOWN_COMMAND = "stopFESPrimary";//chnaged on 14-04-08
    public static final String FESSecondary_SHUTDOWN_COMMAND = "stopFESSecondary";//chnaged on 14-04-08

    public static final String FPS_SHUTDOWN_COMMAND = "stopFPS";
    public static final String FPS_PRIMARY_SHUTDOWN_COMMAND = "stopPrimaryFPS";
    public static final String FPS_SECONDARY_SHUTDOWN_COMMAND = "stopSecondaryFPS";
    public static final String FPS_NAME = "fps.name";

    public static final String PEER_SHUTDOWN_COMMAND = "shutdownPeer";
    public static final String ENTERPRISE_SERVER_SHUTDOWN_COMMAND = "shutdownEnterpriseServer";

    public static final String TEST_SPECIFICATION = "TestSpecification";
    public static final String HA_TEST_SUITE = "HATestSuite";
    public static final String TEST_SUITES = "TestSuites";
    public static final String TEST_SUITE = "TestSuite";
    public static final String TEST_CASES = "TestCases";
    public static final String TEST_CASE = "TestCase";
    public static final String FLOWTEST_CASE = "FlowTestCase";
    public static final String JUNITTEST_CASE = "JUnitTestCase";
    public static final Object VALIDATION_ENABLED = "validationRequired";

    public static final String STARTUP = "Startup";
    public static final String CLEANUP = "Cleanup";
    public static final String COMMAND = "cmd";
    public static final String STARTUP_DIR = "StartUpDir";
    public static final String CLEANUP_DIR = "CleanUpDir";
    public static final String INIT_STARTUP_PATH = "init.startup.path";
    public static final String INIT_STARTUP_COMMAND = "init.startup.command";
    public static final String INIT_CLEANUP_PATH = "init.cleanup.path";
    public static final String INIT_CLEANUP_COMMAND = "init.cleanup.command";
    public static final String START_PRILIMNARY_STEPS = "startPrilimnarySteps";
    public static final String CLEAN_PRILIMNARY_STEPS = "cleanPrilimnarySteps";
    public static final String INSTANCE_NAME = "instanceName";
    public static final String INSTANCE_NAMES = "instanceNames";

    public static final String EVENT_PROCESS = "Application";
    public static final String APPLICATION_GUID = "guid";
    public static final String APPLICATION_VERSION = "version";
    public static final String APPLICATION_BASE_DIR = "dir";
    public static final String FLOW_DIR = "flow";
    public static final String APPLICATION_FILE_PATH = "applicationFilePath";
    public static final String TEST_SCENARIO = "scenario";
    public static final String APPLICATION_STARTUP_TIME = "startupTime";
    public static final String OVERWRITE_APPLICATION = "overwrite";
    public static final String DRTTEST_CASE = "DRTTestCase";
    public static final String RMITEST_CASE = "RMITestCase";
    public static final String TESTCASE_BASE_DIR = "baseDir";
    public static final String PROPERTY = "Property";
    public static final String FILE = "file";
    public static final String TESTCASE_CLASS = "testCaseClass";
    public static final String OUTPUT_DIR = "output";
    public static final String PROFILE_TEMPLATE_DIR = "/config/profiletemplates";
    public static final String PROFILES_DIR1 = "esb";
    public static final String PROFILES_DIR2 = "server";
    public static final String PROFILES_DIR3 = "profiles";

    public static final String AUTO_START = "autoStart";

    public static final String CONFIGS_XML1 = "conf";
    public static final String CONFIGS_XML2 = "Configs.xml";
    public static final String REPORTS_DIR = "reports";
    public static final String JAVA_HOME = "java1.6.0_30";
    public static final String SOA_VERSION = "soaver";
    public static final String CUSTOM_COMPONENT_DIRECTORY = "custom_component_directory";
    public static final String OUTPUT_LOG = "output.log";

}
