@echo off

set CURDIR=%~dp0
rem call %CURDIR%..\..\..\..\scripts\env.bat
rem sleep 1

echo %FIORANO_HOME% | call %FIORANO_HOME%\esb\samples\hsql\CreateHSQLTables.bat %CURDIR%

cd %FIORANO_HOME%\esb\samples\EventProcesses\RetailTelevision\resources\
mkdir ..\production_requests
xcopy "media_production_request_1.txt" "..\production_requests\" /e /f /i

exit



