#!/bin/bash

echo "Inside inner script"

function subscribeApi {
#~ echo $name
#~ echo $version
echo "API ID in function is "$1           >> wso2carbon.log
#~ curl $publisherUrl'/api/am/store/v1/search?limit=10&offset=0&query=name%3A%22'$name'%22%20version%3A%22'$version'%22' -H "Authorization: Bearer "$token -k |jq -r '.list | length'
while [ $(curl $publisherUrl'/api/am/store/v1/search?limit=10&offset=0&query=name%3A%22'$name'%22%20version%3A%22'$version'%22' -H "Authorization: Bearer "$token -k |jq -r '.list | length') -lt 1 ]
do
sleep 5
#~ curl $publisherUrl'/api/am/store/v1/search?limit=10&offset=0&query=name%3A%22'$name'%22%20version%3A%22'$version'%22' -H "Authorization: Bearer "$token -k 
	echo "Api not exist on devportal"
done

export applicationJson=$(curl -k -s -w "\n%{http_code}" -H "Authorization: Bearer "$token"" $publisherUrl"/api/am/store/v1/applications?sortBy=name&sortOrder=asc&limit=10000&offset=0"  | { read -r body;read -r code;echo "Response Code for Application > "$code >> wso2carbon.log ; if [[ $code -ne 200 ]]; then echo "body"$body >> wso2carbon.log;  fi ; echo $body; })

export appArray=${appName//,/$'\n'}
for app in $appArray; do

echo "App Name is "$app      >> wso2carbon.log
export applicationId=$(echo $applicationJson | jq -r '.list[] | select(.name == "'$app'") | .applicationId')


echo "Applicatin iD is "$applicationId >> wso2carbon.log

if [[ ! -z "$applicationId" ]] ;then

echo "Application "$app" already Exist Subscribing Api "$name >> wso2carbon.log
export subscribeJson=$(curl -s -w "\n%{http_code}" -X POST -k  $publisherUrl"/api/am/store/v1/subscriptions" -H "Content-Type:Application/json" -H "Authorization: Bearer "$token"" --data '{"apiId":"'$1'","applicationId":"'$applicationId'","throttlingPolicy":"Unlimited"}' | { read -r body;read -r code;echo "Response Code for Subscribe > "$code >> wso2carbon.log ; if [[ $code -ne 201 ]]; then echo "body"$body >> wso2carbon.log;  fi ; echo $body; })

#~ echo $subscribeJson >> wso2carbon.log

elif [[  -z "$applicationId" ]] ;then

echo "Application "$app" not found Creating" >> wso2carbon.log

export appJson=$(curl -k -s -w "\n%{http_code}" -H "Authorization: Bearer "$token"" -H "Content-Type: application/json" -X POST -d '{"name":"'$app'","throttlingPolicy":"Unlimited","description":"","tokenType":"JWT","groups":null,"attributes":{}}' $publisherUrl"/api/am/store/v1/applications" | { read -r body;read -r code;echo "Response Code for Application > "$code >> wso2carbon.log ; if [[ $code -ne 200 ]]; then echo "body"$body >> wso2carbon.log;  fi ; echo $body; })


export applicationId=$(echo $appJson | jq -r '.applicationId // empty')

#~ echo $applicationId >> wso2carbon.log

if [[ ! -z "$applicationId" ]]
then

echo "subscribing api to application "$app >> wso2carbon.log
export subscribeJson=$(curl -s -w "\n%{http_code}" -X POST -k  $publisherUrl"/api/am/store/v1/subscriptions" -H "Content-Type:Application/json" -H "Authorization: Bearer "$token"" --data '{"apiId":"'$1'","applicationId":"'$applicationId'","throttlingPolicy":"Unlimited"}'  | { read -r body;read -r code;echo "Response Code for Subscribe > "$code >> wso2carbon.log ; if [[ $code -ne 200 ]]; then echo "body"$body >> wso2carbon.log;  fi ; echo $body; })

echo $subscribeJson >> wso2carbon.log

curl -k -H "Authorization: Bearer "$token -H "Content-Type: application/json" -X POST --data-binary '{"keyType":"PRODUCTION","grantTypesToBeSupported":["refresh_token","urn:ietf:params:oauth:grant-type:saml2-bearer","password","client_credentials","iwa:ntlm","urn:ietf:params:oauth:grant-type:device_code","urn:ietf:params:oauth:grant-type:jwt-bearer"],"callbackUrl":"","validityTime":3600}' $publisherUrl"/api/am/store/v1/applications/"$applicationId"/generate-keys" | { read -r body;read -r code;echo "Response Code for Generate Keys > "$code >> wso2carbon.log ; if [[ $code -ne 200 ]]; then echo "body"$body >> wso2carbon.log;  fi ; }


export subscriptionId=$(echo $subscribeJson | jq -r '.subscriptionId // empty')
if [[ ! -z "$subscriptionId" ]]
then
echo "Process completed API "$name" Subscribed to application "$app >> wso2carbon.log
else
echo "Unable to Subscribe API to application "$app >> wso2carbon.log
fi
else
   echo "Error Creating Application "$app >> wso2carbon.log
fi

fi
done


}

if [[ "$option | tr " == *"import"* ]] ;then


if [[ ! -z "$username" && ! -z "$password" && ! -z "$publisherUrl" && ! -z "$gatewayUrl" && ! -z "$filePath" && ! -z "$name" && ! -z "$version" && ! -z "$context" && ! -z "$endpoint" && ! -z "$appName" ]]
then

export apiInfo_file=$PWD/$foldername/$name"_info_"$version".json"

export base64UsernamePassword=$(echo -ne "$username:$password" | base64);

export clientJson=$( curl -k -s -w "\n%{http_code}" -H "Content-type: Application/json" -H "Authorization: Basic "$base64UsernamePassword"" --data '{"clientName": "rest_api_import_export","callbackUrl": "www.google.lk","grantType":"password refresh_token","saasApp": true,"owner": "'$username'","tokenScope": "Production"}' $publisherUrl/client-registration/v0.16/register  | { read -r body;read -r code;echo "Response Code for client-registration > "$code >> wso2carbon.log ; if [[ $code -ne 200 ]]; then echo "body"$body >> wso2carbon.log;  fi ; echo $body; })

#~ echo "clientJson > "$clientJson 

export clientId=$(echo $clientJson | jq -r ".clientId // empty")
export clientSecret=$(echo $clientJson | jq -r ".clientSecret // empty")
export base64ClientIdClientSecret=$(echo -ne "$clientId:$clientSecret" | base64);

if [[ ! -z "$clientId" && ! -z "$clientSecret" ]]
then

if [[  -z "$timeout"  || "$timeout" -lt 180000 || "$timeout" -eq 180000 ]] ;then


export token_json=$(curl -k -s -w "\n%{http_code}" -d "grant_type=password&username="$username"&password="$password"&scope=apim:api_delete+apim:api_view+apim:app_import_export+apim:app_owner_change+apim:subscribe+apim:api_publish+apim:api_import_export+apim:app_manage+apim:app_import_export+apim:publisher_settings+apim:ep_certificates_add+apim:api_create" -H "Authorization: Basic "$base64ClientIdClientSecret"" $publisherUrl/oauth2/token | { read -r body;read -r code;echo "Response Code for Token > "$code >> wso2carbon.log ;if [[ $code -ne 200 ]]; then echo "body"$body >> wso2carbon.log;  fi; echo $body; }) 

export token=$(echo $token_json | jq -r ".access_token // empty")
echo "token"$token

if [[ ! -z "$token" ]]
then

export settingsJson=$(curl -k -s -w "\n%{http_code}" -H "Authorization: Bearer "$token $publisherUrl"/api/am/publisher/v1/settings" | { read -r body;read -r code;echo "Response Code for Settings > "$code >> wso2carbon.log ;if [[ $code -ne 200 ]]; then echo "body"$body >> wso2carbon.log;  fi; echo $body; })

export mediationList=$(curl $publisherUrl'/api/am/publisher/v1/mediation-policies?limit=25&offset=0' -H 'authorization: Bearer '$token -k  |jq .list)

export requestIdPolicyIn=$(echo $mediationList | jq -c '.[] | select(.name | contains("Request-ID-policy-in")) |  .id')
export requestIdPolicyOut=$(echo $mediationList | jq -c '.[] | select(.name | contains("Request-ID-policy-out")) |  .id')

echo $requestIdPolicyIn
echo $requestIdPolicyOut

export setEnv=$(echo $settingsJson |jq -r '.environment | map(.name)')


if [[ $endpoint == *","* ]]; then

IFS=',' read -ra firstendpoint <<< "$endpoint"


if [[ "$isDefaultVersion" && "$isDefaultVersion" == "true" ]]
then

export additionalProp='{"name":"'$name'","mediationPolicies":[{"id":"$requestIdPolicyIn","name":"Request-ID-policy-in","type":"IN"},{"id":"$requestIdPolicyOut","name":"Request-ID-policy-out","type":"OUT"}],"version":"'$version'","context":"'$context'","visibility":"RESTRICTED","visibleRoles":["admin","apiviewer"],"policies":["Unlimited"],"endpointConfig":{"endpoint_type":"load_balance","algoCombo":"org.apache.synapse.endpoints.algorithms.RoundRobin","sessionManagement":"","failOver":"True","sessionTimeOut":"","algoClassName":"org.apache.synapse.endpoints.algorithms.RoundRobin","production_endpoints":[{"url":"'$firstendpoint'","config":{"retryDelay":"","actionDuration":"180000","retryTimeOut":"","suspendDuration":"","suspendErrorCode":[],"retryErroCode":[],"factor":"","suspendMaxDuration":"","actionSelect":"fault"}}]},"additionalProperties":{"RequestedBy":"'$RequestedBy'"},"gatewayEnvironments":'$setEnv'}'



for n in $(echo $endpoint | sed "s/,/ /g"); do

if [[ $n == $firstendpoint ]]; then
echo 'first endpoint'

else
export balancerJson='{"endpoint_type":"http","template_not_supported":false,"config":{"retryDelay":"","actionDuration":"180000","retryTimeOut":"","suspendDuration":"","suspendErrorCode":[],"retryErroCode":[],"factor":"","suspendMaxDuration":"","actionSelect":"fault"},"url":"'$n'"}'
#export additionalProp=$(echo $additionalProp |jq '.endpointConfig.sandbox_endpoints += '[$balancerJson])
export additionalProp=$(echo $additionalProp |jq '.endpointConfig.production_endpoints += '[$balancerJson])
fi
done

export apiId=$(curl -k -s -w "\n%{http_code}" -H "Authorization: Bearer "$token"" -F file=@"$filePath" -F additionalProperties="$additionalProp" -X POST $publisherUrl/api/am/publisher/v1/apis/import-openapi | { read -r body;read -r code;echo "Response Code for Create API > "$code >> wso2carbon.log ;echo $body >> $apiInfo_file; if [[ $code -ne 201 ]]; then echo "body"$body >> wso2carbon.log;  fi ; echo $body; } | jq -r ".id // empty")

#~ export import_response=$(curl -k -s -w "\n%{http_code}" -H "Authorization: Bearer "$token"" -F file=@"$filePath" -F additionalProperties="$additionalProp" -X POST $publisherUrl/api/am/publisher/v1/apis/import-openapi | { read -r body;read -r code;echo "Response Code Token > "$code >> wso2carbon.log ;echo $body >> apiInfo_file; echo $code; })

#~ export apiId=$(jq -r ".id // empty" < $apiInfo_file)



echo $(cat $apiInfo_file | jq '.isDefaultVersion=true')  > $apiInfo_file

echo "Update Api to default. "
curl -k -s -w "\n%{http_code}" -X POST -H "Authorization: Bearer "$token"" -H "Content-Type: application/json" -d @$apiInfo_file $publisherUrl"/api/am/publisher/v1/apis/"$apiId | { read -r body;read -r code;echo "Response Code for update default > "$code >> wso2carbon.log ; if [[ $code -ne 200 ]]; then echo "body"$body >> wso2carbon.log;  fi ; }


else 

export additionalProp='{"name":"'$name'","version":"'$version'","context":"'$context'","visibility":"RESTRICTED","visibleRoles":["admin","apiviewer"],"policies":["Unlimited"],"endpointConfig":{    "endpoint_type":"load_balance","algoCombo":"org.apache.synapse.endpoints.algorithms.RoundRobin","sessionManagement":"","failOver":"True","sessionTimeOut":"","algoClassName":"org.apache.synapse.endpoints.algorithms.RoundRobin","production_endpoints":[{"url":"'$firstendpoint'","config":{"retryDelay":"","actionDuration":"180000","retryTimeOut":"","suspendDuration":"","suspendErrorCode":[],"retryErroCode":[],"factor":"","suspendMaxDuration":"","actionSelect":"fault"}}]},"additionalProperties":{"RequestedBy":"'$RequestedBy'"},"gatewayEnvironments":'$setEnv',"mediationPolicies":[{"id":"$requestIdPolicyIn","name":"Request-ID-policy-in","type":"IN"},{"id":"$requestIdPolicyOut","name":"Request-ID-policy-out","type":"OUT"}]}'



for n in $(echo $endpoint | sed "s/,/ /g"); do

if [[ $n == $firstendpoint ]]; then
echo 'first endpoint'

else

export balancerJson='{"endpoint_type":"http","template_not_supported":false,"config":{"retryDelay":"","actionDuration":"180000","retryTimeOut":"","suspendDuration":"","suspendErrorCode":[],"retryErroCode":[],"factor":"","suspendMaxDuration":"","actionSelect":"fault"},"url":"'$n'"}'

#export additionalProp=$(echo $additionalProp |jq '.endpointConfig.sandbox_endpoints += '[$balancerJson])
export additionalProp=$(echo $additionalProp |jq '.endpointConfig.production_endpoints += '[$balancerJson])
fi
done

export apiId=$(curl -k -s -w "\n%{http_code}" -H "Authorization: Bearer "$token"" -F file=@"$filePath" -F additionalProperties="$additionalProp" -X POST $publisherUrl/api/am/publisher/v1/apis/import-openapi | { read -r body;read -r code;echo "Response Code for Create API > "$code >> wso2carbon.log ;echo $body >> $apiInfo_file; if [[ $code -ne 201 ]]; then echo "body"$body >> wso2carbon.log;  fi ; echo $body; } | jq -r ".id // empty")

fi

else

if [[ "$isDefaultVersion" && "$isDefaultVersion" == "true" ]]
then
if [[ "$enableCors" ]] ; then
export apiId=$(curl -k -s -w "\n%{http_code}" -H "Authorization: Bearer "$token"" -F file=@"$filePath" -F additionalProperties='{"name":"'$name'","mediationPolicies":[{"id":"$requestIdPolicyIn","name":"Request-ID-policy-in","type":"IN"},{"id":"$requestIdPolicyOut","name":"Request-ID-policy-out","type":"OUT"}],"version":"'$version'","context":"'$context'","visibility":"RESTRICTED","visibleRoles":["admin","apiviewer"],"policies":["Unlimited"],"corsConfiguration":{"corsConfigurationEnabled":true,"accessControlAllowCredentials":false,"accessControlAllowOrigins":["*"],"accessControlAllowHeaders":["authorization","Access-Control-Allow-Origin","Content-Type","SOAPAction","apikey","audience","requestid","x-module",
"basic-FlowType"],"accessControlAllowMethods":["GET","PUT","POST","DELETE","PATCH","OPTIONS"]},"endpointConfig":{"endpoint_type":"http","production_endpoints":{"url":"'$endpoint'","config":{"retryDelay":"","actionDuration":"180000","retryTimeOut":"","suspendDuration":"0","suspendErrorCode":[],"retryErroCode":[],"factor":"","suspendMaxDuration":"0","actionSelect":"fault"}}},"additionalProperties":{"RequestedBy":"'$RequestedBy'"},"gatewayEnvironments":'"$setEnv"'}' -X POST $publisherUrl/api/am/publisher/v1/apis/import-openapi | { read -r body;read -r code;echo "Response Code for Create API > "$code >> wso2carbon.log ;echo $body >> $apiInfo_file; if [[ $code -ne 201 ]]; then echo "body"$body >> wso2carbon.log;  fi ; echo $body; } | jq -r ".id // empty")
else
export apiId=$(curl -k -s -w "\n%{http_code}" -H "Authorization: Bearer "$token"" -F file=@"$filePath" -F additionalProperties='{"name":"'$name'","mediationPolicies":[{"id":"$requestIdPolicyIn","name":"Request-ID-policy-in","type":"IN"},{"id":"$requestIdPolicyOut","name":"Request-ID-policy-out","type":"OUT"}],"version":"'$version'","context":"'$context'","visibility":"RESTRICTED","visibleRoles":["admin","apiviewer"],"policies":["Unlimited"],"endpointConfig":{"endpoint_type":"http","production_endpoints":{"url":"'$endpoint'","config":{"retryDelay":"","actionDuration":"180000","retryTimeOut":"","suspendDuration":"0","suspendErrorCode":[],"retryErroCode":[],"factor":"","suspendMaxDuration":"0","actionSelect":"fault"}}},"additionalProperties":{"RequestedBy":"'$RequestedBy'"},"gatewayEnvironments":'"$setEnv"'}' -X POST $publisherUrl/api/am/publisher/v1/apis/import-openapi | { read -r body;read -r code;echo "Response Code for Create API > "$code >> wso2carbon.log ;echo $body >> $apiInfo_file; if [[ $code -ne 201 ]]; then echo "body"$body >> wso2carbon.log;  fi ; echo $body; } | jq -r ".id // empty")
fi

#~ export apiId=$( jq -r ".id // empty" < $apiInfo_file)

echo $(cat $apiInfo_file | jq '.isDefaultVersion=true')  > $apiInfo_file
echo "Update Api to default. "
curl -k -s -w "\n%{http_code}" -X POST -H "Authorization: Bearer "$token"" -H "Content-Type: application/json" -d @$apiInfo_file $publisherUrl"/api/am/publisher/v1/apis/"$apiId | { read -r body;read -r code;echo "Response Code for update default > "$code >> wso2carbon.log ; if [[ $code -ne 200 ]]; then echo "body"$body >> wso2carbon.log;  fi ; }

else 
if [[ "$enableCors" ]] ; then
export apiId=$(curl -k -s -w "\n%{http_code}" -H "Authorization: Bearer "$token"" -F file=@"$filePath" -F additionalProperties='{"name":"'$name'","version":"'$version'","context":"'$context'","visibility":"RESTRICTED","visibleRoles":["admin","apiviewer"],"policies":["Unlimited"],"corsConfiguration":{"corsConfigurationEnabled":true,"accessControlAllowCredentials":false,"accessControlAllowOrigins":["*"],"accessControlAllowHeaders":["authorization","Access-Control-Allow-Origin","Content-Type","SOAPAction","apikey","audience","requestid","x-module",
"basic-FlowType"],"accessControlAllowMethods":["GET","PUT","POST","DELETE","PATCH","OPTIONS"]},"endpointConfig":{"endpoint_type":"http","production_endpoints":{"url":"'$endpoint'","config":{"retryDelay":"","actionDuration":"180000","retryTimeOut":"","suspendDuration":"0","suspendErrorCode":[],"retryErroCode":[],"factor":"","suspendMaxDuration":"0","actionSelect":"fault"}}},"additionalProperties":{"RequestedBy":"'$RequestedBy'"},"gatewayEnvironments":'"$setEnv"',"mediationPolicies":[{"id":"$requestIdPolicyIn","name":"Request-ID-policy-in","type":"IN"},{"id":"$requestIdPolicyOut","name":"Request-ID-policy-out","type":"OUT"}]}' -X POST $publisherUrl/api/am/publisher/v1/apis/import-openapi | { read -r body;read -r code;echo "Response Code for Create API > "$code >> wso2carbon.log ;echo $body >> $apiInfo_file; if [[ $code -ne 201 ]]; then echo "body"$body >> wso2carbon.log;  fi ; echo $body; } | jq -r ".id // empty")
else
export apiId=$(curl -k -s -w "\n%{http_code}" -H "Authorization: Bearer "$token"" -F file=@"$filePath" -F additionalProperties='{"name":"'$name'","version":"'$version'","context":"'$context'","visibility":"RESTRICTED","visibleRoles":["admin","apiviewer"],"policies":["Unlimited"],"endpointConfig":{"endpoint_type":"http","production_endpoints":{"url":"'$endpoint'","config":{"retryDelay":"","actionDuration":"180000","retryTimeOut":"","suspendDuration":"0","suspendErrorCode":[],"retryErroCode":[],"factor":"","suspendMaxDuration":"0","actionSelect":"fault"}}},"additionalProperties":{"RequestedBy":"'$RequestedBy'"},"gatewayEnvironments":'"$setEnv"',"mediationPolicies":[{"id":"$requestIdPolicyIn","name":"Request-ID-policy-in","type":"IN"},{"id":"$requestIdPolicyOut","name":"Request-ID-policy-out","type":"OUT"}]}' -X POST $publisherUrl/api/am/publisher/v1/apis/import-openapi | { read -r body;read -r code;echo "Response Code for Create API > "$code >> wso2carbon.log ;echo $body >> $apiInfo_file; if [[ $code -ne 201 ]]; then echo "body"$body >> wso2carbon.log;  fi ; echo $body; } | jq -r ".id // empty")
fi


#~ export import_response=$(curl -k -s -w "\n%{http_code}" -H "Authorization: Bearer "$token"" -F file=@"$filePath" -F additionalProperties="$additionalProp" -X POST $publisherUrl/api/am/publisher/v1/apis/import-openapi | { read -r body;read -r code;echo "Response Code Token > "$code >> wso2carbon.log ;echo $body >> apiInfo_file; echo $body; })

fi

fi


if [[ ! -z "$apiId" ]]
then

if [[ ! -z "$certificate_path" && ! -z "certificate_alias" ]]
then
echo "Inside certificate"
if [[ $endpoint == *","* ]]; then
#~ IFS=',' read -ra firstendpoint <<< "$endpoint"

#~ curl -X POST $publisherUrl"/api/am/publisher/v1/endpoint-certificates" -H "Authorization: Bearer "$token -H "accept: application/json" -H "Content-Type: multipart/form-data" -F "certificate=@"$certificate_path";type=application/pkix-cert" -F "alias="$certificate_alias"" -F "endpoint="$firstendpoint"" -k 
echo "Certificate Not Supported In Loadbalancer" >> wso2carbon.log

else
curl -s -w "\n%{http_code}" -X POST $publisherUrl"/api/am/publisher/v1/endpoint-certificates" -H "Authorization: Bearer "$token -H "accept: application/json" -H "Content-Type: multipart/form-data" -F "certificate=@"$certificate_path";type=application/pkix-cert" -F "alias="$certificate_alias"" -F "endpoint="$endpoint"" -k  | { read -r body;read -r code;echo "Response Code for certificate > "$code >> wso2carbon.log ; if [[ $code -ne 201 ]]; then echo "body"$body >> wso2carbon.log;  fi ; }
fi

fi

export publishJson=$(curl  -k  -s -w "\n%{http_code}" -H "Authorization: Bearer "$token"" "$publisherUrl/api/am/publisher/v1/apis/change-lifecycle?action=Publish&apiId="$apiId""  -X POST | { read -r body;read -r code;echo "Response Code for Publish Json > "$code >> wso2carbon.log ; if [[ $code -ne 200 ]]; then echo "body"$body >> wso2carbon.log;  fi ; echo $body; } )



export publishedState=$(echo $publishJson | jq -r ".lifecycleState.state // empty")


if [[ "$publishedState" == "Published" ]]
then

subscribeApi $apiId
echo "subscribed"
else
	echo "Error PUBLISHING API"$name >> wso2carbon.log
fi

else
	echo "Error creating api "$name >> wso2carbon.log
fi

else
	echo "Unable to Featch Token" >> wso2carbon.log
fi

elif [[ "$timeout" -gt 180000 ]] ;then



export token_json=$(curl -k -s -w "\n%{http_code}" -d "grant_type=password&username="$username"&password="$password"&scope=apim:api_delete+apim:api_view+apim:app_import_export+apim:app_owner_change+apim:subscribe+apim:api_publish+apim:api_import_export+apim:app_manage+apim:app_import_export+apim:publisher_settings+apim:ep_certificates_add+apim:api_create" -H "Authorization: Basic "$base64ClientIdClientSecret"" $publisherUrl/oauth2/token | { read -r body;read -r code;echo "Response Code for Token > "$code >> wso2carbon.log ;if [[ $code -ne 200 ]]; then echo "body"$body >> wso2carbon.log;  fi; echo $body; }) 
#~ echo $token_json

export token=$(echo $token_json | jq -r ".access_token // empty")


if [[ ! -z "$token" ]]
then



export setEnv=$(curl -k -s -w "\n%{http_code}" -H "Authorization: Bearer "$token $publisherUrl"/api/am/publisher/v1/settings" | { read -r body;read -r code;echo "Response Code for Settings > "$code >> wso2carbon.log ;if [[ $code -ne 200 ]]; then echo "body"$body >> wso2carbon.log;  fi; echo $body; } |jq -r '.environment | map(.name)')

#~ echo "test"
#~ export setEnv=$(echo $settingsJson |jq -r '.environment | map(.name)')

export mediationList=$(curl $publisherUrl'/api/am/publisher/v1/mediation-policies?limit=25&offset=0' -H 'authorization: Bearer '$token -k  |jq .list)

export requestIdPolicyIn=$(echo $mediationList | jq -c '[.[] | select(.name | contains("Request-ID-policy-in")) |  .id]')
export requestIdPolicyOut=$(echo $mediationList | jq -c '[.[] | select(.name | contains("Request-ID-policy-out")) |  .id]')

echo $requestIdPolicyIn
echo $requestIdPolicyOut

if [[ $endpoint == *","* ]]; then

IFS=',' read -ra firstendpoint <<< "$endpoint"


if [[ "$isDefaultVersion" && "$isDefaultVersion" == "true" ]]
then


export additionalProp='{"name":"'$name'","mediationPolicies":[{"id":"$requestIdPolicyIn","name":"Request-ID-policy-in","type":"IN"},{"id":"$requestIdPolicyOut","name":"Request-ID-policy-out","type":"OUT"}],"version":"'$version'","context":"'$context'","visibility":"RESTRICTED","visibleRoles":["admin","apiviewer"],"policies":["Unlimited"],"endpointConfig":{"endpoint_type":"load_balance","algoCombo":"org.apache.synapse.endpoints.algorithms.RoundRobin","sessionManagement":"","failOver":"True","sessionTimeOut":"","algoClassName":"org.apache.synapse.endpoints.algorithms.RoundRobin","production_endpoints":[{"url":"'$firstendpoint'","config":{"retryDelay":"","actionDuration":"'$timeout'","retryTimeOut":"","suspendDuration":"","suspendErrorCode":[],"retryErroCode":[],"factor":"","suspendMaxDuration":"","actionSelect":"fault"}}]},"additionalProperties":{"RequestedBy":"'$RequestedBy'","APITimeout":"'$timeout'"},"gatewayEnvironments":'$setEnv'}'




for n in $(echo $endpoint | sed "s/,/ /g"); do
if [[ $n == $firstendpoint ]]; then
echo 'first endpoint'

else
export balancerJson='{"endpoint_type":"http","template_not_supported":false,"config":{"retryDelay":"","actionDuration":"'$timeout'","retryTimeOut":"","suspendDuration":"","suspendErrorCode":[],"retryErroCode":[],"factor":"","suspendMaxDuration":"","actionSelect":"fault"},"url":"'$n'"}'
#export additionalProp=$(echo $additionalProp |jq '.endpointConfig.sandbox_endpoints += '[$balancerJson])
export additionalProp=$(echo $additionalProp |jq '.endpointConfig.production_endpoints += '[$balancerJson])
fi
done

#~ export apiId=$(curl -k -H "Authorization: Bearer "$token"" -F file=@"$filePath" -F additionalProperties="$additionalProp" -X POST $publisherUrl/api/am/publisher/v1/apis/import-openapi | jq -r ".id // empty")

export apiId=$(curl -k -s -w "\n%{http_code}" -H "Authorization: Bearer "$token"" -F file=@"$filePath" -F additionalProperties="$additionalProp" -X POST $publisherUrl/api/am/publisher/v1/apis/import-openapi | { read -r body;read -r code;echo "Response Code for Create API > "$code >> wso2carbon.log ;echo $body >> $apiInfo_file; if [[ $code -ne 201 ]]; then echo "body"$body >> wso2carbon.log;  fi ; echo $body; } | jq -r ".id // empty")

#~ export apiId=$(jq -r ".id // empty" < $apiInfo_file)

echo $(cat $apiInfo_file | jq '.isDefaultVersion=true')  > $apiInfo_file

curl -k -s -w "\n%{http_code}" -X POST -H "Authorization: Bearer "$token"" -H "Content-Type: application/json" -d @$apiInfo_file $publisherUrl"/api/am/publisher/v1/apis/"$apiId | { read -r body;read -r code;echo "Response Code for update default > "$code >> wso2carbon.log ; if [[ $code -ne 200 ]]; then echo "body"$body >> wso2carbon.log;  fi ; }

else 


export additionalProp='{"name":"'$name'","mediationPolicies":[{"id":"$requestIdPolicyIn","name":"Request-ID-policy-in","type":"IN"},{"id":"$requestIdPolicyOut","name":"Request-ID-policy-out","type":"OUT"}],"version":"'$version'","context":"'$context'","visibility":"RESTRICTED","visibleRoles":["admin","apiviewer"],"policies":["Unlimited"],"endpointConfig":{    "endpoint_type":"load_balance","algoCombo":"org.apache.synapse.endpoints.algorithms.RoundRobin","sessionManagement":"","failOver":"True","sessionTimeOut":"","algoClassName":"org.apache.synapse.endpoints.algorithms.RoundRobin","production_endpoints":[{"url":"'$firstendpoint'","config":{"retryDelay":"","actionDuration":"'$timeout'","retryTimeOut":"","suspendDuration":"","suspendErrorCode":[],"retryErroCode":[],"factor":"","suspendMaxDuration":"","actionSelect":"fault"}}]},"additionalProperties":{"RequestedBy":"'$RequestedBy'"},"gatewayEnvironments":'$setEnv'}'



for n in $(echo $endpoint | sed "s/,/ /g"); do
if [[ $n == $firstendpoint ]]; then
echo 'first endpoint'

else
export balancerJson='{"endpoint_type":"http","template_not_supported":false,"config":{"retryDelay":"","actionDuration":"'$timeout'","retryTimeOut":"","suspendDuration":"","suspendErrorCode":[],"retryErroCode":[],"factor":"","suspendMaxDuration":"","actionSelect":"fault"},"url":"'$n'"}'

#export additionalProp=$(echo $additionalProp |jq '.endpointConfig.sandbox_endpoints += '[$balancerJson])
export additionalProp=$(echo $additionalProp |jq '.endpointConfig.production_endpoints += '[$balancerJson])
 fi
done


export apiId=$(curl -k -s -w "\n%{http_code}" -H "Authorization: Bearer "$token"" -F file=@"$filePath" -F additionalProperties="$additionalProp" -X POST $publisherUrl/api/am/publisher/v1/apis/import-openapi | { read -r body;read -r code;echo "Response Code for Create API > "$code >> wso2carbon.log ;echo $body >> $apiInfo_file; if [[ $code -ne 201 ]]; then echo "body"$body >> wso2carbon.log;  fi ; echo $body; } | jq -r ".id // empty")

fi

else

if [[ "$isDefaultVersion" && "$isDefaultVersion" == "true" ]]
then


export apiId=$(curl -k -s -w "\n%{http_code}" -H "Authorization: Bearer "$token"" -F file=@"$filePath" -F additionalProperties='{"name":"'$name'","mediationPolicies":[{"id":"$requestIdPolicyIn","name":"Request-ID-policy-in","type":"IN"},{"id":"$requestIdPolicyOut","name":"Request-ID-policy-out","type":"OUT"}],"version":"'$version'","context":"'$context'","visibility":"RESTRICTED","visibleRoles":["admin","apiviewer"],"policies":["Unlimited"],"isDefaultVersion":true,"endpointConfig":{"endpoint_type":"http","production_endpoints":{"url":"'$endpoint'","config":{"retryDelay":"","actionDuration":"'$timeout'","retryTimeOut":"","suspendDuration":"0","suspendErrorCode":[],"retryErroCode":[],"factor":"","suspendMaxDuration":"0","actionSelect":"fault"}}},"additionalProperties":{"RequestedBy":"'$RequestedBy'","APITimeout":"'$timeout'"},"gatewayEnvironments":'"$setEnv"'}' -X POST $publisherUrl/api/am/publisher/v1/apis/import-openapi | { read -r body;read -r code;echo "Response Code for Create API > "$code >> wso2carbon.log ;echo $body >> $apiInfo_file; if [[ $code -ne 201 ]]; then echo "body"$body >> wso2carbon.log;  fi ; echo $body; } | jq -r ".id // empty")

else 


export apiId=$(curl -k -s -w "\n%{http_code}" -H "Authorization: Bearer "$token"" -F file=@"$filePath" -F additionalProperties='{"name":"'$name'","mediationPolicies":[{"id":"$requestIdPolicyIn","name":"Request-ID-policy-in","type":"IN"},{"id":"$requestIdPolicyOut","name":"Request-ID-policy-out","type":"OUT"}],"version":"'$version'","context":"'$context'","visibility":"RESTRICTED","visibleRoles":["admin","apiviewer"],"policies":["Unlimited"],"endpointConfig":{"endpoint_type":"http","production_endpoints":{"url":"'$endpoint'","config":{"retryDelay":"","actionDuration":"'$timeout'","retryTimeOut":"","suspendDuration":"0","suspendErrorCode":[],"retryErroCode":[],"factor":"","suspendMaxDuration":"0","actionSelect":"fault"}}},"additionalProperties":{"RequestedBy":"'$RequestedBy'"},"gatewayEnvironments":'"$setEnv"'}' -X POST $publisherUrl/api/am/publisher/v1/apis/import-openapi | { read -r body;read -r code;echo "Response Code for Create API > "$code >> wso2carbon.log ;echo $body >> $apiInfo_file; if [[ $code -ne 201 ]]; then echo "body"$body >> wso2carbon.log;  fi ; echo $body; } | jq -r ".id // empty")



fi

fi
#~ echo $apiObject


#~ export apiId=$(echo $apiObject | jq -r ".id // empty")



if [[ ! -z "$apiId" ]]
then

if [[ ! -z "$certificate_path" && ! -z "certificate_alias" ]]
then
if [[ $endpoint == *","* ]]; then

echo "Certificate Not Supported In Loadbalancer" >> wso2carbon.log
#~ IFS=',' read -ra firstendpoint <<< "$endpoint"

#~ curl -X POST $publisherUrl"/api/am/publisher/v1/endpoint-certificates" -H "Authorization: Bearer "$token -H "accept: application/json" -H "Content-Type: multipart/form-data" -F "certificate=@"$certificate_path";type=application/pkix-cert" -F "alias="$certificate_alias"" -F "endpoint="$firstendpoint"" -k 

else

curl -s -w "\n%{http_code}" -X POST $publisherUrl"/api/am/publisher/v1/endpoint-certificates" -H "Authorization: Bearer "$token -H "accept: application/json" -H "Content-Type: multipart/form-data" -F "certificate=@"$certificate_path";type=application/pkix-cert" -F "alias="$certificate_alias"" -F "endpoint="$endpoint"" -k  | { read -r body;read -r code;echo "Response Code for certificate > "$code >> wso2carbon.log ; if [[ $code -ne 201 ]]; then echo "body"$body >> wso2carbon.log;  fi ; }


fi

fi
export publishJson=$(curl  -k -s -w "\n%{http_code}" -H "Authorization: Bearer "$token"" "$publisherUrl/api/am/publisher/v1/apis/change-lifecycle?action=Publish&apiId="$apiId""  -X POST | { read -r body;read -r code;echo "Response Code for Publish Json > "$code >> wso2carbon.log ; if [[ $code -ne 200 ]]; then echo "body"$body >> wso2carbon.log;  fi ; echo $body; } )



export publishedState=$(echo $publishJson | jq -r ".lifecycleState.state // empty")



if [[ "$publishedState" == "Published" ]]
then
subscribeApi $apiId
echo "subscribed"
else
	echo "Error PUBLISHING API "$name >> wso2carbon.log
fi

else
	echo "Error creating api "$name >> wso2carbon.log
fi

else
	echo "Unable to Featch Token" >> wso2carbon.log
fi


fi



else
	echo "Unable to register Client" >> wso2carbon.log
fi

else 
	echo "Insufficient Data" >> wso2carbon.log

fi

elif [[ "$option | tr " == *"delete"* ]] ;then


export base64UsernamePassword=$(echo -ne "$username:$password" | base64);

export clientJson=$( curl -k -s -w "\n%{http_code}" -H "Content-type: Application/json" -H "Authorization: Basic "$base64UsernamePassword"" --data '{"clientName": "rest_api_import_export","callbackUrl": "www.google.lk","grantType":"password refresh_token","saasApp": true,"owner": "'$username'","tokenScope": "Production"}' $publisherUrl/client-registration/v0.16/register | { read -r body;read -r code;echo "Response Code for client-registration > "$code >> wso2carbon.log ; if [[ $code -ne 200 ]]; then echo "body"$body >> wso2carbon.log;  fi ; echo $body; })

#~ echo $clientJson >> wso2carbon.log

export clientId=$(echo $clientJson | jq -r ".clientId // empty")
export clientSecret=$(echo $clientJson | jq -r ".clientSecret // empty")
export base64ClientIdClientSecret=$(echo -ne "$clientId:$clientSecret" | base64);

if [[ ! -z "$clientId" && ! -z "$clientSecret" ]]
then


export token_json=$(curl -k -s -w "\n%{http_code}" -d "grant_type=password&username="$username"&password="$password"&scope=apim:api_delete+apim:api_view+apim:app_import_export+apim:app_owner_change+apim:subscribe+apim:api_publish+apim:api_import_export+apim:app_manage+apim:app_import_export+apim:subscription_view+apim:sub_manage+apim:publisher_settings+apim:ep_certificates_add+apim:api_create" -H "Authorization: Basic "$base64ClientIdClientSecret"" $publisherUrl/oauth2/token | { read -r body;read -r code;echo "Response Code for Token > "$code >> wso2carbon.log ;if [[ $code -ne 200 ]]; then echo "body"$body >> wso2carbon.log;  fi; echo $body; }) 

export token=$(echo $token_json | jq -r ".access_token // empty")

echo $token

if [[ ! -z "$token" ]]
then




export apiId=$(curl -k -s -w "\n%{http_code}" -H "Authorization: Bearer "$token $publisherUrl"/api/am/publisher/v1/apis?query=name:%22"$name"%22+version:"$version"&expand=true" | { read -r body;read -r code;echo "Response Code for apiId > "$code >> wso2carbon.log ;if [[ $code -ne 200 ]]; then echo "body"$body >> wso2carbon.log;  fi; echo $body; } |jq -r '.list[0].id // empty')

echo "apiId    "$apiId >> wso2carbon.log

if [[ ! -z "$apiId" ]]
then

export subscriptionJSON=$(curl -k -s -w "\n%{http_code}" -H "Authorization: Bearer "$token $publisherUrl"/api/am/publisher/v1/subscriptions?apiId="$apiId | { read -r body;read -r code;echo "Response Code for Subscriptions > "$code >> wso2carbon.log ;if [[ $code -ne 200 ]]; then echo "body"$body >> wso2carbon.log;  fi; echo $body; })
export list=$(echo $subscriptionJSON |jq -r '.list')
echo $list | jq '. | length'
if [ $(echo $list | jq '. | length') == 0 ]
then



curl -k -s -w "\n%{http_code}" $publisherUrl'/api/am/publisher/v1/apis/delete' -H 'authorization: Bearer '$token -H 'Content-Type: application/json' --data-binary '{"id":"'$apiId'","name":"","context":"","version":""}' --compressed  | { read -r body;read -r code;echo "Response Code for Delete Api > "$code >> wso2carbon.log ;if [[ $code -ne 200 ]]; then echo "body"$body >> wso2carbon.log;  fi;  }
echo "API Deleted Successfully" >> wso2carbon.log

else

for (( i=0; i<$(echo $list | jq '. | length'); i++ )); do
export applicationId=$(echo $list |jq -r '.['$i'].subscriptionId')



export unsubscribe=$(curl -k -s -w "\n%{http_code}" -H "Authorization: Bearer "$token -H "Content-Type: application/json" -X DELETE $publisherUrl"/api/am/store/v1/subscriptions/"$applicationId | { read -r body;read -r code;echo "Response Code for Remove subscription > "$code >> wso2carbon.log ;if [[ $code -ne 200 ]]; then echo "body"$body >> wso2carbon.log;  fi; echo $body; })


done

curl -k -s -w "\n%{http_code}" $publisherUrl'/api/am/publisher/v1/apis/delete' -H 'authorization: Bearer '$token -H 'Content-Type: application/json' --data-binary '{"id":"'$apiId'","name":"","context":"","version":""}' --compressed   | { read -r body;read -r code;echo "Response Code for Delete Api > "$code >> wso2carbon.log ;if [[ $code -ne 200 ]]; then echo "body"$body >> wso2carbon.log;  fi; echo $body; }

echo "API Deleted Successfully" >> wso2carbon.log
echo "done" >> wso2carbon.log



fi

else
	echo "API "$name" Not found" >> wso2carbon.log
fi

else
	echo "Unable to Featch Token" >> wso2carbon.log
fi

else
	echo "Unable to register Client" >> wso2carbon.log
fi


fi
