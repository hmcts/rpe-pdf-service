terraform {
  backend "azurerm" {}

  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 3.106.0"
    }
    random = {
      source = "hashicorp/random"
    }
  }
}
