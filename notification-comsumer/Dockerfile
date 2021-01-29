FROM  adoptopenjdk/openjdk11:alpine-jre
ENV APP_HOME /app/bin

RUN mkdir -p ${APP_HOME}

USER guest

COPY --chown=guest:users ./target/*.jar ${APP_HOME}/app.jar

WORKDIR ${APP_HOME}
CMD java ${MY_JAVA_OPTS} -Dspring.profiles.active=docker -jar app.jar