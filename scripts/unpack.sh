#!/bin/bash
# redirect stdout/stderr to a file
exec &> /var/log/unpacklog.txt

cd /tmp
unzip spring-trade-service-0.1.0.zip
mv spring-trade-service-0.1.0.jar /usr/bin/
