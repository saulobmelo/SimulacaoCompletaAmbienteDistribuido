@echo off
call scripts\compile.bat

echo Iniciando todos os nós...

call scripts\start_node.bat A1 A 5001
call scripts\start_node.bat A2 A 5002
call scripts\start_node.bat A3 A 5003
call scripts\start_node.bat B1 B 6001
call scripts\start_node.bat B2 B 6002
call scripts\start_node.bat B3 B 6003

echo Todos os nós foram iniciados em janelas separadas.