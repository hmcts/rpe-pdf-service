locals {
  ase_name = "${data.terraform_remote_state.core_apps_compute.ase_name[0]}"

  local_env = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "aat" : "saat" : var.env}"
  local_ase = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "aat" : "saat" : local.ase_name}"

  s2s_url = "http://rpe-service-auth-provider-${local.local_env}.service.${local.local_ase}.internal"
}

module "pdf-service-api" {
  source = "git@github.com:contino/moj-module-webapp.git?ref=master"
  product = "${var.product}-${var.component}"
  location = "${var.location}"
  env = "${var.env}"
  ilbIp = "${var.ilbIp}"
  subscription = "${var.subscription}"

  app_settings = {
    S2S_URL = "${local.s2s_url}"

    ROOT_APPENDER = "CONSOLE"
    REFORM_TEAM = "${var.product}"
    REFORM_SERVICE_NAME = "${var.component}"
    REFORM_ENVIRONMENT = "${var.env}"
  }
}
