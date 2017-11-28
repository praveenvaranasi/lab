@echo off

rem setting the factory since junit report tasks needs this.
set ANT_OPTS=-Djavax.xml.transform.TransformerFactory=com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl

rem read test.properties & set fiorano_home for this script
FOR /F "eol=; tokens=2,2 delims==" %%i IN ('findstr /i "FIORANO_HOME" ..\configuration\test.properties') DO set FIORANO_HOME=%%i

echo -------------------------------------------------------
echo Fiorano_home is set to %FIORANO_HOME%
echo Using ant executable within FIORANO_HOME/antscripts/bin

echo -------------------------------------------------------


call "%FIORANO_HOME%/antscripts/bin/ant"