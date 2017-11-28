@echo off

set CURDIR=%~dp0
rem call %CURDIR%..\..\..\..\scripts\env.bat
rem sleep 1

echo %FIORANO_HOME% | call %FIORANO_HOME%\esb\samples\hsql\CreateHSQLTables.bat %CURDIR%

cd %FIORANO_HOME%\esb\samples\EventProcesses\RevenueControlPacket\resources
echo %FIORANO_HOME% | call RevenueControlPacket_Install.bat

exit