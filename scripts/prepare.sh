#!/bin/bash
# redirect stdout/stderr to a file
exec &> /var/log/preparelog.txt

sudo apt-get -y update
sudo apt-get -y install default-jre
sudo apt-get -y install awscli
sudo apt-get -y install zip
