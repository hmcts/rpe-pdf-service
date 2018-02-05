module "pdf-service-api" {
  source = "git@github.com:contino/moj-module-webapp.git?ref=add-java-to-staging-slot"
  product = "${var.product}-${var.microservice}"
  location = "${var.location}"
  env = "${var.env}"
  ilbIp = "${var.ilbIp}"

  app_settings = {
    S2S_URL = "${var.s2s-url}"

    ROOT_APPENDER = "CONSOLE"
    REFORM_TEAM = "${var.product}"
    REFORM_SERVICE_NAME = "${var.microservice}"
    REFORM_ENVIRONMENT = "${var.env}"
  }
}
