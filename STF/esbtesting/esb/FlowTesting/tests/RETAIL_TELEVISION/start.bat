@echo off

set FIORANO=%*
set FIORANO_HOME="%FIORANO%"
echo %FIORANO_HOME%

call %FIORANO_HOME%\esb\samples\hsql\starthsql.bat| call .\init.bat

rem exit
