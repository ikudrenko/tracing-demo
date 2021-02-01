TRACING ?= none
DOCKER_COMPOSE_FILE_NAME ?= none
NEW_RELIC_API_KEY =NRII--J-5K9wbxM8Lt_28iCyPqkoZjTA_AGBU

#New relic trace exporter requires API key
#
export NEW_RELIC_API_KEY


newrelic-up: TRACING=newrelic
newrelic-up: DOCKER_COMPOSE_FILE_NAME=newrelic-compose.yml
newrelic-up: build
newrelic-up: docker_up

newrelic-down: DOCKER_COMPOSE_FILE_NAME=newrelic-compose.yml
newrelic-down: docker_stop

jaeger-zipkin-up: TRACING=jaeger-zipkin
jaeger-zipkin-up: DOCKER_COMPOSE_FILE_NAME=jaeger-zipkin-compose.yml
jaeger-zipkin-up: build
jaeger-zipkin-up: docker_up

jaeger-zipkin-down: DOCKER_COMPOSE_FILE_NAME=jaeger-zipkin-compose.yml
jaeger-zipkin-down: docker_stop

docker_up:
	docker-compose -f ${DOCKER_COMPOSE_FILE_NAME} up -d

docker_stop:
	docker-compose -f ${DOCKER_COMPOSE_FILE_NAME} down

build:
	./mvnw clean install -Dtracing=${TRACING}
