TRACING ?= none
DOCKER_COMPOSE_FILE_NAME ?= none
NEW_RELIC_API_KEY =NRII--J-5K9wbxM8Lt_28iCyPqkoZjTA_AGBU

export NEW_RELIC_API_KEY

env:
	@echo ${NEW_RELIC_API_KEY}


newrelic-up: TRACING=newrelic
newrelic-up: DOCKER_COMPOSE_FILE_NAME=newrelic-compose.yml
newrelic-up: build
newrelic-up: docker_up

jaeger-zipkin-up: TRACING=jaeger-zipkin
jaeger-zipkin-up: DOCKER_COMPOSE_FILE_NAME=jaeger-zipkin-compose.yml
jaeger-zipkin-up: build
jaeger-zipkin-up: docker_up


docker_up:
	@echo ${NEW_RELIC_API_KEY}
	docker-compose -f ${DOCKER_COMPOSE_FILE_NAME} up -d

build:
	./mvnw clean install -Dtracing=${TRACING}
