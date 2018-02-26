module "pdf-service-api" {
  source = "git@github.com:contino/moj-module-webapp.git?ref=master"
  product = "${var.product}-${var.microservice}"
  location = "${var.location}"
  env = "${var.env}"
  ilbIp = "${var.ilbIp}"
  subscription = "${var.subscription}"

  app_settings = {
    S2S_URL = "${var.s2s_url}"

    ROOT_APPENDER = "CONSOLE"
    REFORM_TEAM = "${var.product}"
    REFORM_SERVICE_NAME = "${var.microservice}"
    REFORM_ENVIRONMENT = "${var.env}"
  }
}
