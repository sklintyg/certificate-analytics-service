#!/bin/bash
#
# Spring Boot Launch Script

if [ "${SCRIPT_DEBUG}" = "true" ]; then
    set -x
    echo "Script debugging is enabled, allowing bash commands and their arguments to be printed as they are executed"
fi

APP=$(ls /deployments/ | egrep '\.jar$')
exec java $JVM_OPTS $JAVA_OPTS -jar /deployments/$APP


