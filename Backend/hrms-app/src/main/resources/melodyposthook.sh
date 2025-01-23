#!/bin/bash
sleep=180
HOSTNAME=$(hostname)

# Fetch IPs directly from the /etc/hosts file based on the hostname
HOST_IPv4=$(grep -E "^[0-9]+\.[0-9]+\.[0-9]+\.[0-9]+\s+$HOSTNAME" /etc/hosts | awk '{print $1}' | head -n 1)
HOST_IPv6=$(grep -E "^[a-fA-F0-9:]+.*\s+$HOSTNAME" /etc/hosts | awk '{print $1}' | head -n 1)

# Determine which IP to use based on availability
if [ -n "$HOST_IPv4" ]; then
    HOST_IP="$HOST_IPv4"
    http_endpoint="http://${HOST_IPv4}:$MELODY_PORT$READINESS"
    echo "Using IPv4: $HOST_IPv4"
elif [ -n "$HOST_IPv6" ]; then
    HOST_IP="$HOST_IPv6"
    http_endpoint="http://[${HOST_IPv6}]:$MELODY_PORT$READINESS"
    echo "Using IPv6: $HOST_IPv6"
else
    echo "No valid IP address found for hostname in /etc/hosts."
    exit 1
fi

max_iterations=5
wait_seconds=10
now=$(date +"%Y-%m-%d_%H-%M-%S")
iterations=0

echo "IP is $HOST_IP"
echo "hostname is $HOSTNAME"
echo "PORT is $MELODY_PORT"
echo "CONTEXT is $CONTEXT"
echo "MELODY_CONTEXT is $MELODY_CONTEXT"
echo "READINESS is $READINESS"
echo "MELODY_CREDENTIALS is $MELODY_CREDENTIALS"
echo "MELODY_URL is $MELODY_URL"

# Health Check Loop
while true
do
    ((iterations++))
    echo "Attempt $iterations"
    sleep $wait_seconds

    http_code=$(curl --verbose -g -s -o result.txt -w '%{http_code}' "$http_endpoint")

    if [ "$http_code" -eq 200 ]; then
        echo "Server $HOSTNAME is Up"
        break
    fi

    if [ "$iterations" -ge "$max_iterations" ]; then
        echo "Loop Timeout"
        exit 1
    fi
done

echo "melody url is $MELODY_URL"
set -x

# Set the app_url based on the IP type (IPv4 or IPv6)
if [ -n "$HOST_IPv4" ]; then
    app_url="http://$MELODY_CREDENTIALS@${HOST_IPv4}:$MELODY_PORT$MELODY_CONTEXT"
    
    curl -vk -g $MELODY_URL -H 'Authorization: Basic dmlzaW9ud2F2ZXM6eU5uOXExbUFmTQ==' -d "appName=${HOSTNAME}_${now}&appUrls=http://$MELODY_CREDENTIALS@${HOST_IPv4}:$MELODY_PORT$MELODY_CONTEXT"
elif [ -n "$HOST_IPv6" ]; then
    app_url="http://$MELODY_CREDENTIALS@[${HOST_IPv6}]:$MELODY_PORT$MELODY_CONTEXT"
    
    curl -vk -g $MELODY_URL -H 'Authorization: Basic dmlzaW9ud2F2ZXM6eU5uOXExbUFmTQ==' -d "appName=${HOSTNAME}_${now}&appUrls=http://$MELODY_CREDENTIALS@[${HOST_IPv6}]:$MELODY_PORT$MELODY_CONTEXT"
fi



sleep 60;
#!/bin/bash

#variables
echo -e "\n****** This script will delete all unavailable melody pods...(who indicated as red on collect-server) ******\n"
echo "deployment Name is : $DEPLOYMENT_NAME"
echo "MELODY_URL is : $MELODY_URL"
echo -e "MELODY_CREDENTIALS is : $MELODY_CREDENTIALS\n"

AVAILABLE_APP=$(curl -s --user "$MELODY_CREDENTIALS" "$MELODY_URL/" | grep -i -A2 "Application unavailable" | awk '/a>/' | sed 's+</a>+ +g' | grep "$DEPLOYMENT_NAME")

echo "Listing pods which are unavailable (red pods on collect-server) ..."
echo -e "\n"
echo "$AVAILABLE_APP"

my_array=($AVAILABLE_APP)
count=0

for i in ${my_array[@]}; do count=$(($count+1)); done
echo -e "\n"
echo "$DEPLOYMENT_NAME having a total of $count pods."
echo -e "\n"

pods_to_be_deleted=$(echo "$count-0"|bc)
#test=$(echo "$count-0"|bc)
#echo $pods_to_be_deleted

echo "********NOTE: It will delete from Top to Down*******"
echo -e "\n"

if [ $pods_to_be_deleted > 0 ]
then
for (( i = 0; i < $pods_to_be_deleted; i++ ));
do
redpod=${my_array[i]}

#curl  --user "$MELODY_CREDENTIALS" "$MELODY_URL"action=remove_application\&application=$redpod 

  echo -e "${my_array[i]}  unavailable pods removed successfully\n"
done

else
    echo "do nothing ..."
fi

#echo "${my_array[@]} $pods_to_be_deleted unavailable pods removed successfully"
