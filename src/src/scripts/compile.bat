@echo off
if exist out rmdir /s /q out
mkdir out

echo Compilando código...
javac -d out *.java common/*.java core/*.java net/*.java rmi/*.java
echo Compilação concluída. Classes em out\