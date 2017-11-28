@echo off

set CUR_DIR=%~dp0

call env.bat

cd %FIORANO_HOME%\fmq\bin
start runContainer.bat

