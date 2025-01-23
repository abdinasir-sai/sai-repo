#!/bin/bash

echo "" > wso2carbon.log

Help()
{
   # Display Help
   echo "Pass Option as argument to run script."
   echo "'import' to import swagger"
   echo "'migrate' to migrate apis"
   echo "'delete' to delete apis"
}

checkVersionLimit()
{
	export base64UsernamePassword=$(echo -ne "$username:$password" | base64);
	export clientJson=$( curl -k  -H "Content-type: Application/json" -H "Authorization: Basic "$base64UsernamePassword"" --data '{"clientName": "rest_api_import_export","callbackUrl": "www.google.lk","grantType":"password refresh_token","saasApp": true,"owner": "'$username'","tokenScope": "Production"}' $publisherUrl/client-registration/v0.16/register)
	export clientId=$(echo $clientJson | jq -r ".clientId // empty")
	export clientSecret=$(echo $clientJson | jq -r ".clientSecret // empty")
	export base64ClientIdClientSecret=$(echo -ne "$clientId:$clientSecret" | base64);
	export token_json=$(curl -k -s -w "\n%{http_code}" -d "grant_type=password&username="$username"&password="$password"&scope=apim:api_delete+apim:api_view+apim:app_import_export+apim:app_owner_change+apim:subscribe+apim:api_publish+apim:api_import_export+apim:app_manage+apim:app_import_export+apim:publisher_settings+apim:ep_certificates_add+apim:api_create" -H "Authorization: Basic "$base64ClientIdClientSecret"" $publisherUrl/oauth2/token | { read -r body;read -r code;echo "Response Code for token > "$code >> wso2carbon.log ;if [[ $code -ne 200 ]]; then echo "body"$body >> wso2carbon.log;  fi; echo $body; }) 
	export token=$(echo $token_json | jq -r ".access_token // empty")
    export contextSearch=$(curl -k -H "Authorization: Bearer "$token $publisherUrl"/api/am/publisher/v1/apis?query=context:"$context"&expand=false")
	export searchCount=$(echo $contextSearch |jq -r ".count // empty")
	export searchList=$(echo $contextSearch |jq -r ".list // empty")
	if [[ "$searchCount" -ge 3 ]]
	then
	 echo "API already exists three version " >> wso2carbon.log
	 for (( list=0; list<$(echo $searchList | jq '. | length'); list++ )); do
	     export searchjson=$(echo $searchList |jq -r '.['$list']')
		 export searchapiName=$(echo $searchjson |jq -r ".name // empty")
		 export searchapiVersion=$(echo $searchjson |jq -r ".version // empty")
		 echo "Name "$searchapiName" Version "$searchapiVersion >> wso2carbon.log
	  done
	fi
}

# Get the options
while getopts ":h" option; do
   case $option in
      h) # display Help
         Help
         exit;;
   esac
done

#~ read -p "Enter username: " username
#~ read -p "Enter Password: " password
#~ export username=apigateway
#~ export password=Apigateway321
export username=apigateway
export password=apigateway123
export option=$1
#read -p "Press 1 to import new swagger Press 2 to migrate api Press 3 to delete api: " option

if [[ "$option | tr " == *"import"* ]] ;then

export foldername=$(date +%Y%m%d_%H%M%S)
mkdir $PWD/$foldername

export IMPORT_JSON=$(jq -nf metadata.json)
#~ export publisherUrl=$(echo $IMPORT_JSON |jq -r ".publisherUrl // empty")
#~ export gatewayUrl=$(echo $IMPORT_JSON |jq -r ".gatewayUrl // empty")
export publisherHostname=$(echo $IMPORT_JSON |jq -r ".publisherHostname // empty")
export publisherPort=$(echo $IMPORT_JSON |jq -r ".publisherPort // empty")
export gatewayHostname=$(echo $IMPORT_JSON |jq -r ".gatewayHostname // empty")
export gatewayPort=$(echo $IMPORT_JSON |jq -r ".gatewayPort // empty")

export publisherUrl=https://$publisherHostname:$publisherPort
export gatewayUrl=https://$gatewayHostname:$gatewayPort

export apiJson=$(echo $IMPORT_JSON |jq -r '.metadata // empty')
echo $apiJson | jq '. | length' >> wso2carbon.log

for (( c=0; c<$(echo $apiJson | jq '. | length'); c++ )); do
echo "value of loop "$c >> wso2carbon.log
export json=$(echo $apiJson |jq -r '.['$c']')
echo "Current Object"$json >> wso2carbon.log
export name=$(echo $json |jq -r ".name // empty")
export version=$(echo $json |jq -r ".version // empty")
export context=$(echo $json |jq -r ".context // empty")
export filePath=$(echo $json |jq -r ".filePath // empty")
export endpoint=$(echo $json |jq -r ".endpoint // empty")
export appName=$(echo $json |jq -r ".clientName // empty")
export timeout=$(echo $json |jq -r ".timeout // empty")
export enableCors=$(echo $json |jq -r ".enableCors // empty")
export isDefaultVersion=$(echo $json |jq -r ".isDefaultVersion // empty")
export certificate_path=$(echo $json |jq -r ".certificate_path // empty")
export certificate_alias=$(echo $json |jq -r ".certificate_alias // empty")
export RequestedBy=$(echo $json |jq -r ".requested_by // empty")

checkVersionLimit
if [[ "$searchCount" -lt 3 ]];then
echo "Run Inner Script" >> wso2carbon.log
source $PWD/AutomationScript.sh
fi

done
rm -rf $foldername
echo "script end" >> wso2carbon.log

elif [[ "$option | tr " == *"migrate"* ]] ;then
export foldername=$(date +%Y%m%d_%H%M%S)
mkdir $PWD/$foldername
export IMPORT_JSON=$(cat migrate.json)

export srcPublisherHostname=$(echo $IMPORT_JSON |jq -r ".srcPublisherHostname // empty")
export srcPublisherPort=$(echo $IMPORT_JSON |jq -r ".srcPublisherPort // empty")
export srcGatewayHostname=$(echo $IMPORT_JSON |jq -r ".srcGatewayHostname // empty")
export srcGatewayPort=$(echo $IMPORT_JSON |jq -r ".srcGatewayPort // empty")
export destPublisherHostname=$(echo $IMPORT_JSON |jq -r ".destPublisherHostname // empty")
export destPublisherPort=$(echo $IMPORT_JSON |jq -r ".destPublisherPort // empty")
export destGatewayHostname=$(echo $IMPORT_JSON |jq -r ".destGatewayHostname // empty")
export destGatewayPort=$(echo $IMPORT_JSON |jq -r ".destGatewayPort // empty")

export sbntvublisherUrl=https://$srcPublisherHostname:$srcPublisherPort
export srcgatewayUrl=https://$srcGatewayHostname:$srcGatewayPort
export destpublisherUrl=https://$destPublisherHostname:$destPublisherPort
export destgatewayUrl=https://$destGatewayHostname:$destGatewayPort
export publisherUrl=https://$destPublisherHostname:$destPublisherPort
export gatewayUrl=https://$destGatewayHostname:$destGatewayPort


export apiJson=$(echo $IMPORT_JSON |jq -r '.metadata // empty')
echo $apiJson | jq '. | length' >> wso2carbon.log

for (( c=0; c<$(echo $apiJson | jq '. | length'); c++ )); do
echo "value of loop "$c >> wso2carbon.log

export json=$(echo $apiJson |jq -r '.['$c']')

export name=$(echo $json |jq -r ".name // empty")
export version=$(echo $json |jq -r ".version // empty")
#~ export username=$(echo $json |jq -r ".username // empty")
#~ export password=$(echo $json |jq -r ".password // empty")
#export context=$(echo $json |jq -r ".context // empty")
export endpoint=$(echo $json |jq -r ".endpoint // empty")
export appName=$(echo $json |jq -r ".appName // empty")
export certificate_path=$(echo $json |jq -r ".certificate_path // empty")
export certificate_alias=$(echo $json |jq -r ".certificate_alias // empty")
export base64UsernamePassword=$(echo -ne "$username:$password" | base64);

export clientJson=$( curl -k  -H "Content-type: Application/json" -H "Authorization: Basic "$base64UsernamePassword"" --data '{"clientName": "rest_api_import_export","callbackUrl": "www.google.lk","grantType":"password refresh_token","saasApp": true,"owner": "'$username'","tokenScope": "Production"}' $sbntvublisherUrl/client-registration/v0.16/register)
export clientId=$(echo $clientJson | jq -r ".clientId // empty")
export clientSecret=$(echo $clientJson | jq -r ".clientSecret // empty")
export base64ClientIdClientSecret=$(echo -ne "$clientId:$clientSecret" | base64);

if [[ ! -z "$clientId" && ! -z "$clientSecret" ]]
then


export token=$(curl -k -d "grant_type=password&username="$username"&password="$password"&scope=apim:api_delete+apim:api_view+apim:app_import_export+apim:app_owner_change+apim:subscribe+apim:api_publish+apim:api_import_export+apim:app_manage+apim:app_import_export+apim:publisher_settings" -H "Authorization: Basic "$base64ClientIdClientSecret"" $srcgatewayUrl/token | jq -r ".access_token // empty") 
#~ echo $token
if [[ ! -z "$token" ]]
then
export apiInfo_file=$PWD/$foldername/$name"_info_"$version".json"
echo $apiInfo_file >> wso2carbon.log

curl -k -H "Authorization: Bearer "$token $sbntvublisherUrl"/api/am/publisher/v1/apis?query=name:"$name"+version:"$version"&expand=true" > $apiInfo_file

export apiId=$(jq -r '.list[0].id // empty' < $apiInfo_file)
export context=$(jq -r '.list[0].context // empty' < $apiInfo_file)
export isDefaultVersion=$(jq -r '.list[0].isDefaultVersion // empty' < $apiInfo_file)
#~ export timeout=$(jq -r '.list[0].endpointConfig.production_endpoints[0].config.actionDuration // empty' < $apiInfo_file)

export production_endpoints=$(jq -r '.list[0].endpointConfig.production_endpoints' <  $apiInfo_file)
if [[ $(echo ${production_endpoints:0:1}) == "{" ]]
then
echo "JSON OBJ" >> wso2carbon.log
export timeout=$(jq -r '.list[0].endpointConfig.production_endpoints.config.actionDuration // empty' <  $apiInfo_file)
else
echo "ARRAY OF JSON" >> wso2carbon.log
export timeout=$(jq -r '.list[0].endpointConfig.production_endpoints[0].config.actionDuration // empty' <  $apiInfo_file)
fi


echo "Api Id"$apiId >> wso2carbon.log

if [[ ! -z "$apiId" ]]
then

export swagger_file=$PWD/$foldername/$name"_"$version".json"
echo $swagger_file  >> wso2carbon.log
curl -k -H "Authorization: Bearer "$token $sbntvublisherUrl"/api/am/publisher/v1/apis/"$apiId"/swagger" > $swagger_file
ls -ltrh $swagger_file
if [ -f ${swagger_file} ]; then
	if [ -s ${swagger_file} ];
	then


export filePath=$swagger_file

export option=import

source $PWD/AutomationScript.sh;

	else 
		echo "Unable to featch Swagger" >> wso2carbon.log     
	fi; 
else
	echo "Swagger file does not exists" >> wso2carbon.log
fi

else
    echo "API - "$name" Not Found" >> wso2carbon.log
fi

else
	echo "Unable to Featch Token" >> wso2carbon.log
fi

else
	echo "Unable to register Client" >> wso2carbon.log
fi

done
rm -rf $foldername
echo "script end" >> wso2carbon.log

elif [[ "$option | tr " == *"delete"* ]] ;then

export DELETE_JSON=$(cat deleteApi.json)
#~ export publisherUrl=$(echo $DELETE_JSON |jq -r ".publisherUrl // empty")
#~ export gatewayUrl=$(echo $DELETE_JSON |jq -r ".gatewayUrl // empty")

export publisherHostname=$(echo $DELETE_JSON |jq -r ".publisherHostname // empty")
export publisherPort=$(echo $DELETE_JSON |jq -r ".publisherPort // empty")
export gatewayHostname=$(echo $DELETE_JSON |jq -r ".gatewayHostname // empty")
export gatewayPort=$(echo $DELETE_JSON |jq -r ".gatewayPort // empty")

export publisherUrl=https://$publisherHostname:$publisherPort
export gatewayUrl=https://$gatewayHostname:$gatewayPort

export metaJson=$(echo $DELETE_JSON |jq -r '.metadata // empty')

for (( c=0; c<$(echo $metaJson | jq '. | length'); c++ )); do
export json=$(echo $metaJson |jq -r '.['$c']')
export name=$(echo $json |jq -r ".name // empty")
export version=$(echo $json |jq -r ".version // empty")
export endpoint=$(echo $json |jq -r ".endpoint // empty")
#~ export appName=$(echo $json |jq -r ".appName // empty")
echo $name
source $PWD/AutomationScript.sh;

done

fi
