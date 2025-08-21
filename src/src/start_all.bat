@echo off
call scripts\compile.bat

echo Iniciando todos os nós...

rem ===== Grupo A =====
start cmd /k scripts\start_node.bat A1 A 5001 A2:5002,A3:5003
start cmd /k scripts\start_node.bat A2 A 5002 A1:5001,A3:5003
start cmd /k scripts\start_node.bat A3 A 5003 A1:5001,A2:5002

rem ===== Grupo B =====
start cmd /k scripts\start_node.bat B1 B 6001 B2:6002,B3:6003
start cmd /k scripts\start_node.bat B2 B 6002 B1:6001,B3:6003
start cmd /k scripts\start_node.bat B3 B 6003 B1:6001,B2:6002

echo Todos os nós foram iniciados em janelas separadas.
pause