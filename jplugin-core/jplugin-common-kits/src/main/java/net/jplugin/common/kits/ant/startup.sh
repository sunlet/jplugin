#!/bin/sh

source /etc/profile

home="$( cd "$( dirname "$0"  )" && pwd  )"
cd $home
CLASSPATH=""
CLASSPATH=$CLASSPATH:$home/../application/lib/classes

for i in "$home/../application/lib"/*.jar
do
		CLASSPATH="$CLASSPATH":"$i"
done

JVM_OPTION=

#Uncomment for set memory
JVM_OPTION="${JVM_OPTION} -Xms512m -Xmx1500m"

#Uncomment for remote debug
#JVM_OPTION="${JVM_OPTION} -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8797,server=y,suspend=y"


nohup java ${JVM_OPTION} -cp $CLASSPATH \
	-Djplugin.home="../" \
	-server \
	-Dfile.encoding=utf-8 \
net.jplugin.core.kernel.PluginApp >../logs/console.log 2>&1 &

echo "Standalone app started, Please see APP_HOME/logs/console.log to see the detail"
