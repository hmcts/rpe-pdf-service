locals {
  ase_name = "${data.terraform_remote_state.core_apps_compute.ase_name[0]}"

  local_env = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "aat" : "saat" : var.env}"
  local_ase = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "core-compute-aat" : "core-compute-saat" : local.ase_name}"

  s2s_url = "http://rpe-service-auth-provider-${local.local_env}.service.${local.local_ase}.internal"
  sku_size = "${var.env == "prod" || var.env == "sprod" || var.env == "aat" ? "I2" : "I1"}"
}

module "pdf-service-api" {
  source = "git@github.com:hmcts/cnp-module-webapp.git?ref=master"
  product = "${var.product}-${var.component}"
  location = "${var.location}"
  env = "${var.env}"
  ilbIp = "${var.ilbIp}"
  subscription = "${var.subscription}"
  common_tags = "${var.common_tags}"
  asp_name      = "${var.product}-${var.component}-${var.env}"
  asp_rg        = "${var.product}-${var.component}-${var.env}"
  instance_size = "${local.sku_size}"

  app_settings = {
    S2S_URL = "${local.s2s_url}"

    ROOT_APPENDER = "CONSOLE"
    REFORM_TEAM = "${var.product}"
    REFORM_SERVICE_NAME = "${var.component}"
    REFORM_ENVIRONMENT = "${var.env}"
  }
}
