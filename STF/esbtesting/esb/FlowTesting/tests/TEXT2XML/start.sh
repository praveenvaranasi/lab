#! /bin/bash
FIORANO_HOME=$1
CURDIR=`pwd`

$FIORANO_HOME/antscripts/bin/ant -f ../../sed.xml -Dsrc=$CURDIR/srcDir -Dtrgt=$FIORANO_HOME/srcDir -DexpectedOuputFile=$CURDIR/ExpectedOutput/expectedoutput.xml copy replace