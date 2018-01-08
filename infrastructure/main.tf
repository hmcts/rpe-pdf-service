locals {
  default_s2s_url = "http://idam-s2s-${var.env}.service.${data.terraform_remote_state.core_apps_compute.ase_name[0]}.internal"
  s2s_url = "${var.s2s_url != "" ? var.s2s_url : local.default_s2s_url}"
}

module "pdf-service-api" {
  source = "git@github.com:contino/moj-module-webapp.git"
  product = "${var.product}-${var.microservice}"
  location = "${var.location}"
  env = "${var.env}"
  ilbIp = "${var.ilbIp}"

  app_settings = {
    ROOT_APPENDER = "JSON_CONSOLE"
    REFORM_TEAM = "${var.product}"
    REFORM_SERVICE_NAME = "${var.microservice}"
    REFORM_ENVIRONMENT = "${var.env}"
    S2S_URL = "${local.s2s_url}"
  }
}
