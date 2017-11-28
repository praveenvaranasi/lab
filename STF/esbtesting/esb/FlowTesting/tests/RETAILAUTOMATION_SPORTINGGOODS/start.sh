#!/bin/bash

FIORANO_HOME=$1
export FIORANO_HOME
$FIORANO_HOME/esb/samples/hsql/starthsql.sh|./init.sh