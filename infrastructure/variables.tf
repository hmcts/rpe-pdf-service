variable "product" {
  type    = "string"
  default = "cmc"
}

variable "microservice" {
  type = "string"
  default = "pdf-service"
}

variable "location" {
  type    = "string"
  default = "UK South"
}

variable "env" {
  type = "string"
}

variable "test-s2s-url" {
  default = "http://betaDevBccidamS2SLB.reform.hmcts.net"
}

variable "prod-s2s-url" {
  default = "http://betaProdccidamAppLB.reform.hmcts.net:4502"
}

variable "ilbIp"{}

variable "subscription" {}
