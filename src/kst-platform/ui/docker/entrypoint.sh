#!/bin/sh

source /usr/local/bin/setupenvconfig.sh

exec /usr/sbin/lighttpd "$@"
