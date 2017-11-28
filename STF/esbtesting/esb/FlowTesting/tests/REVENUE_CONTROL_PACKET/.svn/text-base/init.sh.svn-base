#! /bin/bash
sleep 1
TEMP='pwd'
#. ./esb/samples/hsql/CreateHSQLTables.sh $TEMP
echo $FIORANO_HOME
$FIORANO_HOME/esb/samples/hsql/CreateHSQLTables.sh $TEMP

cd $FIORANO_HOME/esb/samples/EventProcesses/RevenueControlPacket/resources
. ./RevenueControlPacket_Install.sh