FROM eclipse-temurin:25-jre-alpine AS builder

ARG APP_NAME=forgetrustee-backend
ARG JAR_FILE=target/backend-0.0.1-SNAPSHOT.jar

ENV APP_HOME=/var/${APP_NAME}

COPY ${JAR_FILE} /application.jar
RUN java -Djarmode=tools -jar  /application.jar extract --layers --destination $APP_HOME/

FROM eclipse-temurin:25-jre-alpine

ARG APP_NAME=forgetrustee-backend

ENV APP_HOME=/var/${APP_NAME}
ENV APP_PORT=8080
ENV user=${APP_NAME}
ENV group=${APP_NAME}
ENV uid=1000
ENV gid=1000

ENV JDK_JAVA_OPTIONS="-XX:+UseParallelGC -XX:ParallelGCThreads=2 -Xms256m -Xmx512m -XX:MaxMetaspaceSize=192M -XX:MetaspaceSize=96M"

# Create a group for the service
RUN addgroup -g ${gid} ${group} \
    && adduser -S -h "$APP_HOME" -u ${uid} -G ${group} -s /bin/ash ${user} \
    && mkdir -p "$APP_HOME" \
    && chown -R ${user}:${group} "$APP_HOME"

WORKDIR $APP_HOME

COPY --from=builder --chown=${user}:${group} $APP_HOME/dependencies/ ./
COPY --from=builder --chown=${user}:${group} $APP_HOME/spring-boot-loader/ ./
COPY --from=builder --chown=${user}:${group} $APP_HOME/snapshot-dependencies/ ./
COPY --from=builder --chown=${user}:${group} $APP_HOME/application/ ./

COPY --chown=${user}:${group} metadata/ $APP_HOME/metadata/

# service port
EXPOSE ${APP_PORT}

VOLUME ${APP_HOME}/data

USER ${user}

HEALTHCHECK --interval=15s --timeout=5s CMD wget -q -T 5 --spider http://127.0.0.1:8080/actuator/health || exit 1

CMD ["java","-server","-jar","application.jar"]
