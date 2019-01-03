#!/bin/bash
# redirect stdout/stderr to a file
exec &> /tmp/unpacklog.txt

cd /usr/bin
unzip spring-trade-service-0.1.0.zip
