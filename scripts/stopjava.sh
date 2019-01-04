#!/bin/bash
# redirect stdout/stderr to a file
sudo exec &> stop.out

pkill -f 'java -jar'
