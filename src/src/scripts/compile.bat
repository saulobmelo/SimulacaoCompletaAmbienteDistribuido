@echo off
if exist out rmdir /s /q out
mkdir out

echo Compilando código...
javac -cp out *.java
echo Compilação concluída. Classes em out\