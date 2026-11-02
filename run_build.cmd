@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-21"
set "PATH=%JAVA_HOME%\bin;%PATH%"
cd /d "C:\Users\hp\Desktop\labs\ikram\TP8-Spring-RestController"
call mvn clean test
