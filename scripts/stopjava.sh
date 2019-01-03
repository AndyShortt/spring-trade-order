#!/bin/bash
# redirect stdout/stderr to a file
exec &> /var/log/stoplog.txt

pkill -f 'java -jar'
