@echo off
set FIORANO=%*
set FIORANO_HOME="%FIORANO%"

%FIORANO_HOME%/antscripts/bin/ant -f ../../sed.xml -Dsrc=%FIORANO_HOME%\output -Dtrgt=%~dp0/output -DexpectedOuputFile=%~dp0/ExpectedOutput/expectedoutput.xml copy delTempDirs revert