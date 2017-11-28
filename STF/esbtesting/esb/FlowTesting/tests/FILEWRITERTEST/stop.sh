#! /bin/bash
FIORANO_HOME=$1
CURDIR=`pwd`

$FIORANO_HOME/antscripts/bin/ant -f ../../sed.xml -Dsrc=$FIORANO_HOME/output -Dtrgt=$CURDIR/output copy delTempDirs