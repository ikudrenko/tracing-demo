# tracing-demo

OpenTelemetry demo projects supports shows ability of tracing using auto-instrumentation feature.

# Architecture diagram
![diagramm](ServiceArchitecture.png)

# Run
## Jaeger
http://localhost:16686/
## Zipkin
http://localhost:9411/zipkin/
```
make jaeger-zipkin-up
```
## New Relic
For new relic integration API key should be provided.
API key should be set in Makefile in NEW_RELIC_API_KEY variable 
```
make newrelic-up
```

# Endpoint
- API: http://localhost:8081/items

