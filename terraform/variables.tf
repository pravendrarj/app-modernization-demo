variable "prefix" {
  description = "Short prefix used to name Azure resources."
  type        = string
  default     = "legacyinv"
}

variable "location" {
  description = "Azure region to deploy resources into."
  type        = string
  default     = "eastus"
}

variable "image_tag" {
  description = "Container image tag to deploy."
  type        = string
  default     = "latest"
}

variable "app_service_sku" {
  description = "SKU for the Linux App Service Plan."
  type        = string
  default     = "B1"
}

variable "acr_sku" {
  description = "SKU for the Azure Container Registry."
  type        = string
  default     = "Basic"
}

variable "app_port" {
  description = "Port the container listens on."
  type        = number
  default     = 3010
}

variable "tags" {
  description = "Tags applied to all resources."
  type        = map(string)
  default = {
    application = "legacy-inventory-mgmt"
    author      = "pravendrarj"
    managed_by  = "terraform"
  }
}
