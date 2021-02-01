FROM  adoptopenjdk/openjdk11:alpine-jre

ENV SERVICE_NAME "api"
ENV APP_HOME /app/bin
ENV TRACING_OPTS "-javaagent:./opentelemetry-javaagent-all.jar -Dotel.exporter=jaeger,zipkin -Dotel.exporter.jaeger.endpoint=jaeger:14250 -Dotel.exporter.zipkin.endpoint=http://zipkin:9411/api/v2/spans -Dotel.exporter.jaeger.service.name="${SERVICE_NAME}" -Dotel.exporter.zipkin.service.name="${SERVICE_NAME}" -Dspring.profiles.active=docker"

RUN mkdir -p ${APP_HOME}

USER guest

COPY --chown=guest:users ./target/*.jar ${APP_HOME}/app.jar
COPY ./agents/opentelemetry-javaagent-all.jar ${APP_HOME}/

WORKDIR ${APP_HOME}
CMD java ${MY_JAVA_OPTS} ${TRACING_OPTS} -Dspring.profiles.active=docker -jar app.jar