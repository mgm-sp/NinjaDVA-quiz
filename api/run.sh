#!/bin/sh

cd "$(dirname "$0")" || exit

PIDFILE=/tmp/quiz-api.pid
export MAVEN_OPTS="-Xmx1024m"

stopserver () {
	if [ -f "$PIDFILE" ]; then
		kill "$(cat $PIDFILE)"
		rm $PIDFILE
	fi
}

startserver () {
	mvn package -DskipTests
	mvn exec:java -Dexec.mainClass="quiz.Publisher"&
	echo $! > $PIDFILE
	sleep 5
}

if [ -f $PIDFILE ]; then
	stopserver
fi

startserver

CURRENT=$(find src/ -type f |xargs md5sum)
LAST="$CURRENT"
while true; do
	CURRENT=$(find src/ -type f |xargs md5sum)
	if [ "$CURRENT" != "$LAST" ];then
		echo "restarting"
		stopserver
		startserver
	fi
	LAST="$CURRENT"
	sleep 1
done

#java -classpath target/quiz-api-1.0-SNAPSHOT.jar quiz.Publisher
