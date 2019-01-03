#!/bin/bash
# redirect stdout/stderr to a file
exec &> /tmp/runlog.txt

nohup java -jar usr/bin/spring-trade-service-0.1.0.jar &
