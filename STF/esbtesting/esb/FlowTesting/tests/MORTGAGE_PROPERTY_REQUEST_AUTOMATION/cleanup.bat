@echo off

set CURDIR=%~dp0
rem set FIORANO_HOME=%1

echo %FIORANO_HOME% | call %FIORANO_HOME%\esb\samples\EventProcesses\MortgagePropertyRequestAutomation\resources\cleanup.bat

rem exit
