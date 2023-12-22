FROM eclipse-temurin:17.0.9_9-jre as builder

ARG APP_NAME=kickstarttrustee
ARG JAR_FILE=build/libs/server-0.0.1-SNAPSHOT.jar

ENV APP_HOME /var/${APP_NAME}

WORKDIR $APP_HOME

COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM eclipse-temurin:17.0.9_9-jre

ARG APP_NAME=kickstarttrustee

ENV APP_HOME=/var/${APP_NAME}
ENV APP_PORT=8090
ENV user=${APP_NAME}
ENV group=${APP_NAME}
ENV uid=1000
ENV gid=1000

ENV JDK_JAVA_OPTIONS="-Xms384m -Xmx512m -XX:MaxMetaspaceSize=192M -XX:MetaspaceSize=96M"

# Create a group for the service
RUN groupadd -g ${gid} ${group} \
    && useradd -d "$APP_HOME" -u ${uid} -g ${gid} -m -s /bin/bash ${user} \
    && mkdir -p "$APP_HOME"

COPY --from=builder $APP_HOME/dependencies/ $APP_HOME/
RUN true
COPY --from=builder $APP_HOME/snapshot-dependencies/ $APP_HOME/
RUN true
COPY --from=builder $APP_HOME/spring-boot-loader/ $APP_HOME/
RUN true
COPY --from=builder $APP_HOME/application/ $APP_HOME/

# file permissions
RUN chown -R ${APP_NAME}:${APP_NAME} "$APP_HOME"

# service port
EXPOSE ${APP_PORT}

USER ${user}

WORKDIR $APP_HOME

HEALTHCHECK --interval=15s --timeout=5s CMD curl -f http://127.0.0.1:8090/actuator/health || exit 1

CMD ["java","-server","org.springframework.boot.loader.JarLauncher"]
