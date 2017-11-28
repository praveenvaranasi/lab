@echo off
REM This script is used to kill all the suspended/started Processes (given as argument)
REM that are running as orphan "processes" in the windows environment
REM This invariably kills all the programs running as processes 

REM CAUTION:ALL THE processes would be killed.

:KILL
process -k %1
if not errorlevel 2 goto KILL
