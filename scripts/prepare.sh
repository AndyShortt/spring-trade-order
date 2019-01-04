#!/bin/bash
# redirect stdout/stderr to a file
sudo exec &> prepare.out

sudo apt-get -y update
sudo apt-get -y install default-jre
sudo apt-get -y install awscli
sudo apt-get -y install zip
