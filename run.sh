#!/bin/bash

JAVA_OPTS="$JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -XX:MaxRAMPercentage=75.0"

exec java -Dsun.misc.URLClassPath.disableJarChecking=true $JAVA_OPTS -jar /app.jar