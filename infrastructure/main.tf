module "backend" {
  source   = "git@github.com:contino/moj-module-webapp?ref=master"
  product  = "${var.product}-backend"
  location = "${var.location}"
  env      = "${var.env}"
  ilbIp    = "${var.ilbIp}"

  app_settings = {
    S2S_URL = "${var.s2s_url}"
  }
}
