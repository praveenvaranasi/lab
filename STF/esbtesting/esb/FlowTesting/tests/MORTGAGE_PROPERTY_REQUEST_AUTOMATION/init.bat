@echo off

set CURDIR=%~dp0
rem set FIORANO_HOME=%~dp1

rem call %CURDIR%..\..\..\..\scripts\env.bat
echo %FIORANO_HOME% | call %FIORANO_HOME%\esb\samples\EventProcesses\MortgagePropertyRequestAutomation\resources\init.bat

exit
