#!/bin/bash
# redirect stdout/stderr to a file
sudo exec &> unpack.out

cd /tmp
unzip spring-trade-service-0.1.0.zip
cd build/libs
chmod a+rwx spring-trade-service-0.1.0.jar
sudo mv spring-trade-service-0.1.0.jar /usr/bin/
