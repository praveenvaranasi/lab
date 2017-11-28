@echo off

set CUR_DIR=%~dp0

call env.bat
cd %FIORANO_HOME%\esb\fes\bin
start fes.bat

