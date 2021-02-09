@echo off

if defined JAVA_HOME (
  %JAVA_HOME%\bin\java -jar JARNAME %*
) ELSE (
  echo JAVA_HOME not set! Please point it to the folder containing you JDK 11 or better.
)
