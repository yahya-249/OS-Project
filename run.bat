@echo off
echo Compiling...
if not exist out mkdir out
javac -d out src\*.java
if %errorlevel% neq 0 (
    echo Compilation FAILED. Make sure Java JDK is installed.
    exit /b 1
)
echo Running...
java -cp out Main
