provider "azurerm" {
  version = "=1.44.0"
}

resource "azurerm_resource_group" "rg" {
  name     = "${var.product}-${var.component}-${var.env}"
  location = "${var.location}"

  tags = "${var.common_tags}"
}

resource "azurerm_application_insights" "appinsights" {
  name                = "${var.product}-${var.component}-appinsights-${var.env}"
  location            = "${var.location}"
  resource_group_name = "${azurerm_resource_group.rg.name}"
  application_type    = "Web"

  tags = "${var.common_tags}"
}

resource "azurerm_key_vault_secret" "AZURE_APPINSGHTS_KEY" {
  name         = "AppInsightsInstrumentationKey"
  value        = "${azurerm_application_insights.appinsights.instrumentation_key}"
  key_vault_id = "${data.azurerm_key_vault.rpe_shared_vault.id}"
}

data "azurerm_key_vault" "rpe_shared_vault" {
  name                = "${var.product}-shared-${var.env}"
  resource_group_name = "${var.product}-${var.env}"
}
