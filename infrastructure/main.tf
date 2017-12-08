module "pdf-service-api" {
  source = "git@github.com:contino/moj-module-webapp.git?ref=private-ase"
  product = "${var.product}-${var.microservice}"
  location = "${var.location}"
  env = "${var.env}"
  ilbIp = "${var.ilbIp}"

  app_settings = {
    ROOT_APPENDER = "JSON_CONSOLE"
    REFORM_TEAM = "${var.product}"
    REFORM_SERVICE_NAME = "${var.microservice}"
    REFORM_ENVIRONMENT = "${var.env}"
  }
}
