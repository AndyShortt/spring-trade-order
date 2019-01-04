#!/bin/bash
# redirect stdout/stderr to a file
sudo exec &> stop.out

/etc/init.d/spring-trade-service stop
