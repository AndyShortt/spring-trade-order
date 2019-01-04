#!/bin/bash
# redirect stdout/stderr to a file
sudo exec &> run.out

sudo link -s /usr/bin/spring-trade-service-0.1.0.jar /etc/init.d/spring-trade-service
/etc/init.d/myapp start

#cd /usr/bin
#nohup java -jar spring-trade-service-0.1.0.jar > log.txt &
