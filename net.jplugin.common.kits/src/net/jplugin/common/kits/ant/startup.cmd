@echo off
cd /d %~dp0

set jplugin.home=%~dp0\..
set CLASSPATH=

set lib_path=..\application\lib
@setlocal enableextensions enabledelayedexpansion
set classpath=%lib_path%\classes
for %%c in (%lib_path%\*.*) do set classpath=!classpath!;%%c
endlocal&set classpath=%classpath%

set JVM_OPTION=
rem set JVM_OPTION=%JVM_OPTION% -Xms512m -Xmx1024m
rem set JVM_OPTION=%JVM_OPTION% -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8797,server=y,suspend=y

@echo on
java -Djplugin.home=%jplugin.home% %JVM_OPTION% -cp %CLASSPATH%  -server net.jplugin.core.kernel.PluginApp
 
