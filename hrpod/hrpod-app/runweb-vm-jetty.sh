#!/bin/bash

## This script should be run inside the Vagrant VM (using Vagrant shell).
## Currently, EzCentos is set up to run the tweet webapp on JBoss, however, it can also be run with jetty with this script.
## Copy this script file, along with the config directory, over to the /vagrant/ directory.

ZK_HOST=$1
if [[ -z $1 || -z $2 || -z $3 ]]; then
        echo "Usage: runweb-vm-jetty.sh <zookeeper hostname> <jetty-port> <warfile>"
        # Fail fast
        exit -1;
fi
echo "Starting up web server (thrift client) using ZooKeeper (EzDiscovery) host: $ZK_HOST"

APP_NAME=testapp
echo "As a thrift client, we expect to find a Thrift server for our application: $APP_NAME"

# Copy the jetty runner jar as jetty-runner.jar
JETTY_RUNNER_JAR=/vagrant/ezbakejars/jetty-runner.jar
cp /vagrant/ezbakejars/jetty-runner*.jar $JETTY_RUNNER_JAR

# Debug the output of the next command
set -x
export EZCONFIGURATION_DIR=./config
java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=4000 \
	-Dzookeeper=$ZK_HOST \
	-Dappname=$APP_NAME \
	-Xmx256m -Xms256m \
	-jar $JETTY_RUNNER_JAR \
	--path sample \
	--port $2 $3

# Disable debugging
set +x
