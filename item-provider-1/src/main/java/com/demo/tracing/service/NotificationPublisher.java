package com.demo.tracing.service;

import javax.annotation.PreDestroy;

import com.demo.tracing.domain.TracingMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sproutsocial.nsq.Publisher;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.extension.annotations.WithSpan;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationPublisher {

    private final Publisher publisher;
    private final String topic;
    private final ObjectMapper om = new ObjectMapper();
    private static final TextMapPropagator.Setter<TracingMessage> setter =
          (tracingMessage, key, traceHeader) -> tracingMessage.setTraceHeader(traceHeader);

    public NotificationPublisher(
          @Value("${nsq.publisher.host}") String publisherHost,
          @Value("${nsq.publisher.topic}") String publishTopic) {
        publisher = new Publisher(publisherHost);
        topic = publishTopic;
    }


    @Async
    @WithSpan
    @SneakyThrows
    public void publishMessageAsync(String message) {
        log.info("Topic {}, message {}", topic, message);

        TracingMessage message1 = new TracingMessage(null, message);

        OpenTelemetry.getGlobalPropagators()
                     .getTextMapPropagator()
                     .inject(Context.current(), message1, setter);

        publisher.publish(topic, om.writeValueAsBytes(message1));
    }

    @PreDestroy
    public void destroy() {
        log.info("Destroying PublisherService");
        publisher.stop();
    }

}
