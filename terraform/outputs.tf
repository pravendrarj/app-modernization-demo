output "resource_group_name" {
  description = "Name of the resource group."
  value       = azurerm_resource_group.main.name
}

output "acr_login_server" {
  description = "Login server of the Azure Container Registry."
  value       = azurerm_container_registry.main.login_server
}

output "acr_name" {
  description = "Name of the Azure Container Registry."
  value       = azurerm_container_registry.main.name
}

output "app_name" {
  description = "Name of the deployed Web App."
  value       = azurerm_linux_web_app.main.name
}

output "app_url" {
  description = "Public URL of the deployed application."
  value       = "https://${azurerm_linux_web_app.main.default_hostname}/inventory"
}
