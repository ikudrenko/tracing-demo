FROM  adoptopenjdk/openjdk11:alpine-jre

ENV SERVICE_NAME "prov2"
ENV APP_HOME /app/bin

RUN mkdir -p ${APP_HOME}

USER guest

COPY --chown=guest:users ./target/*.jar ${APP_HOME}/app.jar
COPY ./agents/opentelemetry-javaagent-all.jar ${APP_HOME}/
COPY ./agents/opentelemetry-exporters-newrelic-auto-0.12.0.jar ${APP_HOME}/

WORKDIR ${APP_HOME}
CMD java ${TRACING_OPTS} -Dspring.profiles.active=docker -jar app.jar