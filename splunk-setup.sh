#!/bin/bash

RED="\e[1;31m"
GREEN="\e[0;32m"
NC='\033[0m' # No Color

echo ""
echo "${GREEN} 1. Starting docker service ${NC} "
service docker start

echo""
# Splunk installation
# Run the splunk image as a container
echo "${GREEN} 2. Run the splunk ${NC} "
docker run -d -p 8000:8000 -e "SPLUNK_START_ARGS=--accept-license" -e "SPLUNK_PASSWORD=<password>" --name splunk2 splunk/splunk:latest
