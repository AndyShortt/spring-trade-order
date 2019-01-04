#!/bin/bash

sudo ln -s /usr/bin/spring-trade-service-0.1.0.jar /etc/init.d/spring-trade-service
service spring-trade-service start

#cd /usr/bin
#nohup java -jar spring-trade-service-0.1.0.jar > log.txt &
