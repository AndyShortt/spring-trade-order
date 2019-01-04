#!/bin/bash
# redirect stdout/stderr to a file
sudo exec &> run.out

cd /usr/bin
nohup java -jar spring-trade-service-0.1.0.jar &
