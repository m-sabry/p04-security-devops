
RED="\e[1;31m"
GREEN="\e[0;32m"
NC='\033[0m' # No Color


# Docker installation
echo "---- ${RED} First: Docker setup ${NC} ------"
echo "${GREEN} 1. Uninstall old docker version ${NC}"
apt-get remove docker docker-engine docker.io containerd runc

echo ""
echo "${GREEN} 2. installing docker ${NC}"
apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg-agent \
    software-properties-common

echo ""
echo "${GREEN} 3. Add Docker’s official GPG key ${NC} "
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -

echo ""
echo "${GREEN} 4. Set up the docker stable repository ${NC} "
add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
   $(lsb_release -cs) \
   stable"

echo ""
echo "${GREEN} 5. Installing docker ${NC} "
apt-get update
apt-get install -y docker-ce docker-ce-cli containerd.io

echo ""
echo "${GREEN} 6. Adding current usr to docker group ${NC} "
usermod -a -G docker $USER

echo ""
echo "${GREEN} 7. Rebooting system ${NC} "
reboot