@echo off
REM Uso: start_node NODEID GROUP PORT
set NODE=%1
set GROUP=%2
set PORT=%3

if "%NODE%"=="" set NODE=A1
if "%GROUP%"=="" set GROUP=A
if "%PORT%"=="" set PORT=5000

echo Iniciando nó %NODE% grupo %GROUP% porta %PORT%...
start cmd /k java -cp out Main %NODE% %GROUP% %PORT%
