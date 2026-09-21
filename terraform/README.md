# Deploying to Azure

This directory contains Terraform to deploy the Legacy Inventory Management System
as a container on **Azure App Service (Web App for Containers)**, backed by an
**Azure Container Registry (ACR)**.

## Architecture

- **Azure Container Registry** – stores the Docker image.
- **App Service Plan (Linux, B1)** – compute for the web app.
- **Linux Web App for Containers** – runs the image, pulls from ACR using a
  system-assigned managed identity (no admin credentials, `AcrPull` role).

The app listens on port **3010** and is served under the **`/inventory`** context path.

## Prerequisites

- [Azure CLI](https://learn.microsoft.com/cli/azure/) (`az login` completed)
- [Terraform](https://developer.hashicorp.com/terraform/downloads) >= 1.5
- [Docker](https://docs.docker.com/get-docker/)

## 1. Provision infrastructure

```pwsh
cd terraform
terraform init
terraform apply
```

Note the outputs, especially `acr_login_server`, `acr_name`, and `app_name`.

## 2. Build and push the image

Run from the project root (where the `Dockerfile` is):

```pwsh
$ACR = terraform -chdir=terraform output -raw acr_name
$LOGIN_SERVER = terraform -chdir=terraform output -raw acr_login_server

az acr login --name $ACR
docker build -t "$LOGIN_SERVER/legacy-inventory-mgmt:latest" .
docker push "$LOGIN_SERVER/legacy-inventory-mgmt:latest"
```

## 3. Restart the app to pull the new image

```pwsh
$RG = terraform -chdir=terraform output -raw resource_group_name
$APP = terraform -chdir=terraform output -raw app_name

az webapp restart --resource-group $RG --name $APP
```

## 4. Browse the app

```pwsh
terraform -chdir=terraform output -raw app_url
```

## Tear down

```pwsh
terraform -chdir=terraform destroy
```
