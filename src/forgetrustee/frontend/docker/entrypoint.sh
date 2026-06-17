#!/bin/sh

set -e
set -o pipefail

envsubst < /var/www/html/runtime-env.template.js > /var/www/html/env-config.js

exec "$@"
