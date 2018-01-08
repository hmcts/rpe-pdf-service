module "backend" {
  source   = "git@github.com:contino/moj-module-webapp?ref=master"
  product  = "${var.product}-backend"
  location = "${var.location}"
  env      = "${var.env}"
  ilbIp    = "${var.ilbIp}"

  app_settings = {
    S2S_URL = "http://idam-s2s-${var.env}.service.${data.terraform_remote_state.core_apps_compute.ase_name[0]}.internal"
  }
}