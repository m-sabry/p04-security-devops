#!/bin/bash

RED="\e[1;31m"
GREEN="\e[0;32m"
NC='\033[0m' # No Color

echo ""
echo "${GREEN} 1. Starting docker service ${NC} "
service docker start

echo""
# Jenkins installation
# Run the jenkinsci/blueocean image as a container 
echo "${GREEN} 2. Run the jenkinsci/blueocean image ${NC} "
docker run --rm -u root -d --name jenkins -p 8080:8080 -v jenkins-data:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock  jenkinsci/blueocean