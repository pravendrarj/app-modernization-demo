# Deploying to Azure Kubernetes Service (AKS)

> Author: **pravendrarj** — App Modernization Demo

This directory contains an **ARM template** that provisions an AKS cluster and an
Azure Container Registry (ACR), plus Kubernetes manifests to run the
Legacy Inventory Management System on the cluster.

## Contents

| File | Purpose |
|---|---|
| [`aks-deploy.json`](aks-deploy.json) | ARM template: AKS cluster + ACR + `AcrPull` role assignment |
| [`aks-deploy.parameters.json`](aks-deploy.parameters.json) | Default parameter values |
| [`k8s/deployment.yaml`](k8s/deployment.yaml) | Kubernetes Deployment + LoadBalancer Service |

## Prerequisites

- [Azure CLI](https://learn.microsoft.com/cli/azure/) (`az login` completed)
- [kubectl](https://kubernetes.io/docs/tasks/tools/)
- [Docker](https://docs.docker.com/get-docker/)

## 1. Provision the infrastructure

```pwsh
az group create --name appmod-rg --location eastus

az deployment group create `
  --resource-group appmod-rg `
  --template-file arm/aks-deploy.json `
  --parameters arm/aks-deploy.parameters.json
```

Capture the outputs:

```pwsh
$ACR = az deployment group show -g appmod-rg -n aks-deploy --query properties.outputs.acrName.value -o tsv
$LOGIN_SERVER = az deployment group show -g appmod-rg -n aks-deploy --query properties.outputs.acrLoginServer.value -o tsv
$AKS = az deployment group show -g appmod-rg -n aks-deploy --query properties.outputs.aksClusterName.value -o tsv
```

## 2. Build and push the image

Run from the project root (where the `Dockerfile` is):

```pwsh
az acr login --name $ACR
docker build -t "$LOGIN_SERVER/legacy-inventory-mgmt:latest" .
docker push "$LOGIN_SERVER/legacy-inventory-mgmt:latest"
```

## 3. Deploy to the cluster

```pwsh
az aks get-credentials --resource-group appmod-rg --name $AKS

# Point the manifest at your registry
(Get-Content arm/k8s/deployment.yaml) -replace '<ACR_LOGIN_SERVER>', $LOGIN_SERVER | kubectl apply -f -
```

## 4. Get the public endpoint

```pwsh
kubectl get service app-modernization-demo --watch
```

Once an `EXTERNAL-IP` appears, the app is reachable at
`http://<EXTERNAL-IP>/inventory`.

## Tear down

```pwsh
az group delete --name appmod-rg --yes --no-wait
```
