@echo off
set FIORANO=%*
set FIORANO_HOME="%FIORANO%"

%FIORANO_HOME%/antscripts/bin/ant -f ../../sed.xml -Dsrc=%FIORANO_HOME%\output -Dtrgt=%~dp0/output copy delTempDirs