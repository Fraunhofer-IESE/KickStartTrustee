#!/bin/sh

if [[ -z "${KEYCLOAK_BASE_URL}" ]]; then
    echo "KEYCLOAK_BASE_URL env variable not set!";
    exit 1;
fi

if [[ -z "${KEYCLOAK_REALM}" ]]; then
    echo "KEYCLOAK_REALM env variable not set!";
    exit 1;
fi

if [[ -z "${KEYCLOAK_CLIENT_ID}" ]]; then
    echo "KEYCLOAK_CLIENT_ID env variable not set!";
    exit 1;
fi

if [[ -z "${KST_API_BASE_PATH}" ]]; then
    echo "KST_API_BASE_PATH env variable not set!";
    exit 1;
fi

if [[ -z "${KST_OWNER_UI}" ]]; then
    echo "KST_OWNER_UI env variable not set!";
    exit 1;
fi

cat <<EOF > /var/www/html/config.json
{
    "keycloakBaseUrl": "${KEYCLOAK_BASE_URL}",
    "keycloakRealm": "${KEYCLOAK_REALM}",
    "keycloakClientId": "${KEYCLOAK_CLIENT_ID}",
    "kstApiBasePath": "${KST_API_BASE_PATH}",
    "kstOwnerUI": "${KST_OWNER_UI}"
}
EOF
