provider "azurerm" {
  features {}
}

resource "azurerm_resource_group" "rg" {
  name     = "${var.product}-${var.component}-${var.env}"
  location = var.location

  tags = var.common_tags
}

module "application_insights" {
  source = "git@github.com:hmcts/terraform-module-application-insights?ref=main"

  env     = var.env
  product = var.product
  name    = "${var.product}-${var.component}-appinsights"

  resource_group_name = azurerm_resource_group.rg.name

  common_tags = var.common_tags

  daily_data_cap_in_gb = var.daily_data_cap_in_gb

}

moved {
  from = azurerm_application_insights.appinsights
  to   = module.application_insights.azurerm_application_insights.this
}



resource "azurerm_key_vault_secret" "AZURE_APPINSGHTS_KEY" {
  name         = "app-insights-connection-string"
  value        = module.application_insights.connection_string
  key_vault_id = data.azurerm_key_vault.rpe_shared_vault.id
}

data "azurerm_key_vault" "rpe_shared_vault" {
  name                = "${var.product}-shared-${var.env}"
  resource_group_name = "${var.product}-${var.env}"
}
