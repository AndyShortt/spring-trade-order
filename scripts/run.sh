#!/bin/bash
# redirect stdout/stderr to a file
exec &> /var/log/runlog.txt

cd /usr/bin
nohup java -jar spring-trade-service-0.1.0.jar &
