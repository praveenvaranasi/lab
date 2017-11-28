#! /bin/bash
sleep 1
TEMP='pwd'
#. ./esb/samples/hsql/CreateHSQLTables.sh $TEMP
$FIORANO_HOME/esb/samples/hsql/CreateHSQLTables.sh $TEMP
cd $FIORANO_HOME/esb/samples/EventProcesses/RetailTelevision/resources/ 
#ant
mkdir -p ../production_requests
cp media_production_request_1.txt ../production_requests/
