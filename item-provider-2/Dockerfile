FROM  adoptopenjdk/openjdk11:alpine-jre

ENV SERVICE_NAME "prov2"
ENV APP_HOME /app/bin
ENV TRACING_OPTS "-javaagent:./opentelemetry-javaagent-all.jar -Dotel.exporter.jar=./opentelemetry-exporters-newrelic-auto-0.12.0.jar -Dnewrelic.api.key=NRII--J-5K9wbxM8Lt_28iCyPqkoZjTA_AGBU -Dnewrelic.service.name="${SERVICE_NAME}" -Dnewrelic.trace.uri.override=https://trace-api.eu.newrelic.com/trace/v1"

RUN mkdir -p ${APP_HOME}

USER guest

COPY --chown=guest:users ./target/*.jar ${APP_HOME}/app.jar
COPY ./agents/opentelemetry-javaagent-all.jar ${APP_HOME}/
COPY ./agents/opentelemetry-exporters-newrelic-auto-0.12.0.jar ${APP_HOME}/

WORKDIR ${APP_HOME}
CMD java ${MY_JAVA_OPTS} ${TRACING_OPTS} -Dspring.profiles.active=docker -jar app.jar