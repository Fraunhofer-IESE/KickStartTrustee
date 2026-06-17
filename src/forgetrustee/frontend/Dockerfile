# Copyright 2026 Fraunhofer IESE
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

FROM alpine:3.24

RUN set -x \
    && apk upgrade --no-cache \
    && apk add --no-cache lighttpd gettext \
    && rm -rf /var/cache/apk/* \
    && rm -rf /etc/lighttpd/* /etc/logrotate.d/lighttpd /var/log/lighttpd /var/www/localhost \
    && mkdir -p /var/www/html

COPY docker/lighttpd/ /etc/lighttpd/

COPY dist/ /var/www/html/

COPY docker/runtime-env.template.js /var/www/html/runtime-env.template.js

COPY docker/entrypoint.sh /entrypoint.sh

RUN chmod +x /entrypoint.sh

EXPOSE 80/tcp

HEALTHCHECK --interval=10s --timeout=5s CMD wget -q -T 5 --spider http://127.0.0.1:80/index.html || exit 1

ENTRYPOINT ["/entrypoint.sh"]
CMD ["/usr/sbin/lighttpd", "-D", "-f", "/etc/lighttpd/lighttpd.conf"]
