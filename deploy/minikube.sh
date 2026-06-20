#!/bin/bash
set -e
set -o pipefail

# set variables
MINIKUBE_MEMORY=${1:-6144}
MINIKUBE_CPUS=${2:-2}
MINIKUBE_DRIVER=${3:-podman}
MINIKUBE_CONTAINER_RUNTIME=${4:-containerd}
APP_NAMESPACE=${7:-infrastructure-engineering-pt-group-f-backend}
AVWX_API_KEY=${8:-avwx-api-key}
GITHUB_USERNAME=${5:-$(gh api user | jq -r '.login')}
GITHUB_TOKEN=${6:-$(gh auth token)}

# start minikube and enable addons
minikube start --memory "${MINIKUBE_MEMORY}" --cpus "${MINIKUBE_CPUS}" --driver="${MINIKUBE_DRIVER}" --container-runtime="${MINIKUBE_CONTAINER_RUNTIME}"
# minikube addons enable csi-hostpath-driver # use if you want to use hostpath as storage class
# minikube addons enable metrics-server # use if you want to see pod metrics (CPU, Memory, etc)
minikube status

# create namespace
kubectl create namespace "${APP_NAMESPACE}"

# store GitHub token in a secret
kubectl -n "${APP_NAMESPACE}" create secret docker-registry ghcr-credentials \
  --docker-server=ghcr.io \
  --docker-username="${GITHUB_USERNAME}" \
  --docker-password="${GITHUB_TOKEN}"

# create Helm values file for the backend
cat <<EOF > /tmp/weather-app-backend-values.yaml
---
spring:
  profile: local

apiKeys:
  avwx: "Token ${AVWX_API_KEY}"

imagePullSecrets:
  - name: ghcr-credentials
EOF

# install backend using Helm
helm upgrade --install -n "${APP_NAMESPACE}" weather-app-backend ./charts/weather-app-backend -f /tmp/weather-app-backend-values.yaml

# expose the backend service
minikube service -n "${APP_NAMESPACE}" weather-app-backend --url
