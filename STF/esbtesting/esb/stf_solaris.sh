#!/bin/bash


## setting the factory since junit report tasks needs this.
ANT_OPTS="-Djavax.xml.transform.TransformerFactory=com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl"
export ANT_OPTS
### read test.properties & set fiorano_home for this script
source ../configuration/test.properties

echo -----------------------------------
echo .
echo ----------you can safely ignore above print line statements-------------

$FIORANO_HOME/antscripts/bin/ant
