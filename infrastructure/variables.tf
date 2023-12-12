variable "product" {
}

variable "component" {
}

variable "location" {
  default = "UK South"
}

variable "env" {
}

variable "subscription" {}

variable "common_tags" {
  type = map(string)
}

variable "daily_data_cap_in_gb" {
  description = "Specifies the Application Insights component daily data volume cap in GB"
  default     = 100
}
