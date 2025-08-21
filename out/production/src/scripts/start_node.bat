@echo off
REM Uso: start_node NODEID GROUP PORT PARES
set NODE=%1
set GROUP=%2
set PORT=%3
set PARES=%4

if "%NODE%"=="" set NODE=A1
if "%GROUP%"=="" set GROUP=A
if "%PORT%"=="" set PORT=5000
if "%PARES%"=="" set PARES=""

echo Iniciando nó %NODE% grupo %GROUP% porta %PORT% pares %PARES%...
java -cp out Main %NODE% %GROUP% %PORT% %PARES%