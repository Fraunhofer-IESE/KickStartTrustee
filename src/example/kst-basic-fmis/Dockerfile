FROM alpine:3.19

RUN set -x \
    && apk upgrade --no-cache \
    && apk add --no-cache lighttpd \
    && rm -rf /var/cache/apk/* \
    && rm -rf /etc/lighttpd/* /etc/logrotate.d/lighttpd /var/log/lighttpd /var/www/localhost \
    && mkdir -p /var/www/html

COPY lighttpd/ /etc/lighttpd/

COPY build/ /var/www/html/

COPY --chmod=755 docker/* /usr/local/bin/

EXPOSE 80/tcp

ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
CMD ["-D", "-f", "/etc/lighttpd/lighttpd.conf"]
