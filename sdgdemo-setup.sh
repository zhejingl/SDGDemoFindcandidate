oc new project sdgdemo
oc -n <control plane project> patch --type='json' smmr default -p '[{"op": "add", "path": "/spec/members", "value":["'"sdgdemo"'"]}]'
