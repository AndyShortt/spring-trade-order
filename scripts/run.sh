#!/bin/bash

#sudo ln -s /usr/bin/spring-trade-service-0.1.0.jar /etc/init.d/spring-trade-service
sudo systemctl start spring-trade-service.service

#cd /usr/bin
#nohup java -jar spring-trade-service-0.1.0.jar > log.txt &
