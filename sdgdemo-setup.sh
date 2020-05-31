oc new project sdgdemo
oc -n <control plane project> patch --type='json' smmr default -p '[{"op": "add", "path": "/spec/members", "value":["'"sdgdemo"'"]}]'
#deploy applications
oc apply -n bookinfo -f https://raw.githubusercontent.com/Maistra/istio/maistra-1.1/samples/bookinfo/platform/kube/bookinfo.yaml
oc apply -n bookinfo -f https://raw.githubusercontent.com/Maistra/istio/maistra-1.1/samples/bookinfo/platform/kube/bookinfo.yaml
#update istio-ingress gateway
oc apply -n bookinfo -f https://raw.githubusercontent.com/Maistra/istio/maistra-1.1/samples/bookinfo/networking/bookinfo-gateway.yaml