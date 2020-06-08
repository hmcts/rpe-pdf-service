#!/usr/bin/env bash
set -e
state_store_resource_group="mgmt-state-store-sandbox"
state_store_storage_acccount="mgmtstatestoresandbox"
bootstrap_state_storage_container="mgmtstatestorecontainersandbox"
productEnvironment="sandbox"
product="rpe-pdf-service"

getCreds() {

    export ARM_SUBSCRIPTION_ID="$(az keyvault secret show --vault-name infra-vault-sandbox --name terraform-creds --output json | jq -r .value | jq -r .azure_subscription)"
    export ARM_CLIENT_ID="$(az keyvault secret show --vault-name infra-vault-sandbox --name terraform-creds --output json | jq -r .value | jq -r .azure_client_id)"
    export ARM_CLIENT_SECRET="$(az keyvault secret show --vault-name infra-vault-sandbox --name terraform-creds --output json | jq -r .value | jq -r .azure_client_secret)"
    export ARM_TENANT_ID="$(az keyvault secret show --vault-name infra-vault-sandbox --name terraform-creds --output json | jq -r .value | jq -r .azure_tenant_id)"
    export TF_VAR_jenkins_AAD_objectId=$(az keyvault secret show --vault-name infra-vault-sandbox --name sandbox-jenkins-object-id --query value -o tsv)
    export TF_VAR_ilbIp=10.100.76.11
    export TF_VAR_root_address_space=10.96.0.0/12
    export TF_VAR_client_id=${ARM_CLIENT_ID}
    export TF_VAR_env=${productEnvironment}
    export TF_VAR_subscription=sandbox
    export TF_VAR_tenant_id=${ARM_TENANT_ID}

}


getCreds

terraform init \
    -backend-config "storage_account_name=$state_store_storage_acccount" \
    -backend-config "container_name=$bootstrap_state_storage_container" \
    -backend-config "resource_group_name=$state_store_resource_group" \
    -backend-config "key=$product/$productEnvironment/terraform.tfstate"

terraform "$@"
