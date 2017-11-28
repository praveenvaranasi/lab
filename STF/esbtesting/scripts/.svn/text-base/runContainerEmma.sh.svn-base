#! /bin/bash

if [ "$FIORANO_HOME" = "" ]; then
    FIORANO_HOME=../../..
fi
export ESB_HOME_DIR=$FIORANO_HOME/esb
export ESB_USER_DIR=$FIORANO_HOME/runtimedata


export LOCALCLASSPATH=$JAVA_HOME/lib/jndi.jar:$JAVA_HOME/lib/tools.jar:$FIORANO_HOME/extlib/wrapper/lib/wrapper.jar:$FIORANO_HOME/extlib/wrapper/lib/wrappertest.jar:$FIORANO_HOME/fmq/lib/fmq-kernel-all.jar:$FIORANO_HOME/extlib/bsf/bsf.jar:$FIORANO_HOME/extlib/js/js_15R3.jar:$FIORANO_HOME/extlib/base64/base64-2.1.jar:$FIORANO_HOME/extlib/piccolo/Piccolo.jar:$FIORANO_HOME/esb/server/profiles:$FIORANO_HOME/extlib/mx4j/mx4j-jmx.jar:$FIORANO_HOME/extlib/mx4j/mx4j-remote.jar:$FIORANO_HOME/extlib/mx4j/mx4j-tools.jar:$FIORANO_HOME/extlib/mx4j/mx4j-rjmx.jar:$FIORANO_HOME/extlib/sax/sax2.jar:$FIORANO_HOME/esb/tools/mapper/lib/esb-tools-DBLookup.jar:$FIORANO_HOME/extlib/jaxp/jaxp-api.jar:$FIORANO_HOME/extlib/jta/jta-1_0_1B.jar:$FIORANO_HOME/extlib/jms/jms.jar:$FIORANO_HOME/extlib/sslava/sslava.jar:$FIORANO_HOME/extlib/sslava/sslava_1.2.3_patch_5.jar:$FIORANO_HOME/extlib/jsse/jcert.jar:$FIORANO_HOME/extlib/jsse/jsse.jar:$FIORANO_HOME/extlib/jetty/jetty.jar:$FIORANO_HOME/extlib/jsse/jnet.jar:$FIORANO_HOME/extlib/wutka/dtdparser121.jar:$FIORANO_HOME/extlib/sms/sms.jar:$FIORANO_HOME/extlib/snmp/snmp.jar:$FIORANO_HOME/extlib/javamail/mail.jar:$FIORANO_HOME/extlib/jaf/activation.jar:$FIORANO_HOME/extlib/httpclient/httpclient.jar:$FIORANO_HOME/extlib/castor/castor-1.0.2-xml.jar:$FIORANO_HOME/extlib/oracle/classes12.zip:$FIORANO_HOME/extlib/xerces/xercesImpl.jar:$FIORANO_HOME/extlib/xalan/xalan.jar:$FIORANO_HOME/extlib/derby/derby.jar:$FIORANO_HOME/extlib/log4j/log4j-1.2.9.jar:$FIORANO_HOME/framework/lib/fiorano-lm-api.jar:$FIORANO_HOME/framework/lib/fiorano-util.jar:$FIORANO_HOME/framework/lib/fiorano-lm-impl.jar:$FIORANO_HOME/extlib/ant/ant.jar:$FIORANO_HOME/esb/lib/esb-fes-krnl-all.jar:$FIORANO_HOME/esb/lib/esb-fps-krnl-all.jar:$FIORANO_HOME/licenses:$FIORANO_HOME/xml-catalog:$FIORANO_HOME/extlib/mx4j/mx4j-rimpl.jar

export JAVA_CMD=$JAVA_HOME/bin/java

if [ "$ORIGINAL_PATH" = "" ]; then
    export ORIGINAL_PATH=$PATH
fi
export PATH=$PATH:$FIORANO_HOME/framework/lib:$FMQ_DIR/lib/common

export FIORANO_PROFILE_DIR=$ESB_HOME_DIR/server/profiles
export FIORANO_PROFILE=EnterpriseServer

export CONTAINER_CMD_LINE_ARGUMENTS="-profilesDir $FIORANO_PROFILE_DIR -profile $FIORANO_PROFILE -dbPath $ESB_USER_DIR/EnterPriseServers/$FIORANO_PROFILE/run -nobackground -mode fes"

echo $CONTAINER_CMD_LINE_ARGUMENTS

export CONTAINER_OPTS="-Djava.library.path=$FIORANO_HOME/fmq/lib/common:$FIORANO_HOME/framework/lib -DFIORANO_HOME=$FIORANO_HOME -DSCRIPT=fes -DFESB_HOME=../../server -DESB_USER_DIR=$ESB_USER_DIR -DESB_REPOSITORY_DIRECTORY=$FIORANO_HOME/esb/server/repository -DAPPSERVER_FES_HOME=../webapps -DAPPSERVER_FPS_HOME=../webapps -DFMQ_HOME=../../../fmq -DHTTPClient.forceHTTP_1.0=true -DHtmlAdaptor=true -DHtmlAdaptorPort=9083 -Djavax.net.ssl.trustStore=../../server/profiles/certs/jssecacerts -Dlog4j.configuration=log4j_config.txt -Dmx4j.log.priority=error -Djavax.management.builder.initial=mx4j.server.MX4JMBeanServerBuilder -Daxis.EngineConfigFactory=com.fiorano.bc.axis.configuration.FioranoEngineConfigurationFactoryServlet -Djava.naming.factory.initial=org.apache.naming.java.javaURLContextFactory -Djava.naming.factory.url.pkgs=org.apache.naming -Dorg.mortbay.log.class=fiorano.esb.jetty.log.JettyFileLogger -Djava.endorsed.dirs=../../../extlib/jaxp:../../../extlib/sax:../../../extlib/dom:../../../extlib/xerces:../../../extlib/saxon:../../../extlib/xalan:../../../extlib/xml-commons-resolver:../../../extlib/mx4j -Djava.ext.dirs=$JAVA_HOME/lib/ext:$JAVA_HOME/jre/lib/ext:../../../esb/lib/ext -DSCRIPT_FILE=fes.sh -Xms128m -Xmx512m" 


export SRC_HOME=$FIORANO_HOME/../sources
export EMMA_OUT_BASE=$FIORANO_HOME/esb/fes/bin/Emma
export EMMA_OUT_FILE=$EMMA_OUT_BASE/rawoutput.es
export EMMA_HTML_OUT_FILE=$EMMA_OUT_BASE/coverage_lastrun/index.html
export FES_SRC_PATH=$SRC_HOME/esb/fes/core/src

mkdir Emma

echo $JAVA_CMD -cp $SRC_HOME/3rdParty/emma/emma.jar emma report -in $EMMA_OUT_FILE -r html -sp $FES_SRC_PATH -Dreport.html.out.file=$EMMA_OUT_BASE/FullCoverage/index.html > $EMMA_OUT_BASE/createFullEmmaReport.sh

$JAVA_CMD -cp $SRC_HOME/3rdParty/emma/emma.jar $ESB_JVM_SERVER_ARGS $BOOT_OPTS $CONTAINER_OPTS emma run -sp $FES_SRC_PATH -raw -outfile $EMMA_OUT_FILE -merge y -r html -Dreport.html.out.file=$EMMA_HTML_OUT_FILE -ix +com.fiorano.esb.* -classpath $LOCALCLASSPATH fiorano.server.boot.EsbContainer $CONTAINER_CMD_LINE_ARGUMENTS

while [ -f "$FIORANO_PROFILE_DIR/$FIORANO_PROFILE/FES/restart" ]
do
    echo
    echo ------------------------------------------------------
    echo
    rm -f "$FIORANO_PROFILE_DIR/$FIORANO_PROFILE/FES/restart"
    $CMD $*
done
