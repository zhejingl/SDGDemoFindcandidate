oc new project sdgdemo
oc -n istio-system patch --type='json' smmr default -p '[{"op": "add", "path": "/spec/members", "value":["'"sdgdemo"'"]}]'
#deploy applications
#candidate
oc apply -n sdgdemo -f https://raw.githubusercontent.com/zhejingl/SDGDemoFindcandidate/master/istio/deployment/deployment.yaml
#titles
oc apply -n sdgdemo -f https://raw.githubusercontent.com/zhejingl/SDGDemoBoot/master/deployment/deployment.yaml


#update istio-ingress gateway
oc apply -n sdgdemo -f https://raw.githubusercontent.com/zhejingl/SDGDemoFindcandidate/master/istio/networking/sdgcandidate-gateway.yaml

export GATEWAY_URL=$(oc -n istio-system get route istio-ingressgateway -o jsonpath='{.spec.host}')

#if mutual TLS is not enabled: 
oc apply -n sdgdemo -f https://raw.githubusercontent.com/zhejingl/SDGDemoFindcandidate/master/istio/networking/destination-rull-all.yaml

#if mutual TLS is enabled: 
oc apply -n sdgdemo -f https://raw.githubusercontent.com/zhejingl/SDGDemoFindcandidate/master/istio/networking/destination-rule-all-mtls

