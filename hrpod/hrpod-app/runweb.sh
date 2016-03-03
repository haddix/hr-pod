#!/bin/bash
MONGO_ZK_HOST=$1
if [[ -z $MONGO_ZK_HOST ]]; then
        echo "Usage: runweb.sh <zookeeper hostname>"
        # Fail fast
        exit -1;
fi
echo "Starting up web server (thrift client) using ZooKeeper (EzDiscovery) host: $MONGO_ZK_HOST"

export EZCONFIGURATION_DIR=./config 
mvn jetty:run -Dzookeeper=$MONGO_ZK_HOST 
