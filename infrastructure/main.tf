locals {
  ase_name = "${data.terraform_remote_state.core_apps_compute.ase_name[0]}"

  local_env = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "aat" : "saat" : var.env}"
  local_ase = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "core-compute-aat" : "core-compute-saat" : local.ase_name}"

  sku_size = "${var.env == "prod" || var.env == "sprod" || var.env == "aat" ? "I2" : "I1"}"
}

module "pdf-service-api" {
  source        = "git@github.com:hmcts/cnp-module-webapp?ref=master"
  product       = "${var.product}-${var.component}"
  location      = "${var.location}"
  env           = "${var.env}"
  ilbIp         = "${var.ilbIp}"
  subscription  = "${var.subscription}"
  common_tags   = "${var.common_tags}"
  common_tags   = "${merge(var.common_tags, map("Team Name", "Software Engineering"))}"
  asp_name      = "${var.product}-${var.component}-${var.env}"
  asp_rg        = "${var.product}-${var.component}-${var.env}"
  instance_size = "${local.sku_size}"
  enable_ase = false

  app_settings = {
    ROOT_APPENDER       = "CONSOLE"
    REFORM_TEAM         = "${var.product}"
    REFORM_SERVICE_NAME = "${var.component}"
    REFORM_ENVIRONMENT  = "${var.env}"
  }
}
