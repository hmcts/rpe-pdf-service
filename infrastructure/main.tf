locals {
  s2s_url = "${var.env == "prod" ? var.prod-s2s-url : var.test-s2s-url}"
}

module "pdf-service-api" {
  source = "git@github.com:contino/moj-module-webapp.git?ref=master"
  product = "${var.product}-${var.microservice}"
  location = "${var.location}"
  env = "${var.env}"
  ilbIp = "${var.ilbIp}"

  app_settings = {
    S2S_URL = "${local.s2s_url}"

    ROOT_APPENDER = "CONSOLE"
    REFORM_TEAM = "${var.product}"
    REFORM_SERVICE_NAME = "${var.microservice}"
    REFORM_ENVIRONMENT = "${var.env}"
  }
}
