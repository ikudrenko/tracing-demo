package com.demo.tracing.service;

import javax.annotation.PreDestroy;

import com.demo.tracing.domain.TracingMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sproutsocial.nsq.Publisher;
import io.opentelemetry.api.trace.Span;
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

    public NotificationPublisher(
          @Value("${nsq.publisher.host}") String publisherHost,
          @Value("${nsq.publisher.topic}") String publishTopic) {
        publisher = new Publisher("localhost:4150");
        topic = publishTopic;
    }

    private TextMapPropagator.Setter<String> setter = (s, s2, s1) -> {//todo: refactor
        log.info("TextMapPropagator.Setter carrier {}", s);
        log.info("TextMapPropagator.Setter Key {}", s2);
        log.info("TextMapPropagator.Setter Value {}", s1);
    };

    @Async
    @WithSpan
    @SneakyThrows
    public void publishMessageAsync(String message) {
        log.info("Topic {}, message {}", topic, message);

        Span span = Span.current();
        String spanID = span.getSpanContext().getSpanIdAsHexString();
        String traceID = span.getSpanContext().getTraceIdAsHexString();

//
//
//        OpenTelemetry.getGlobalPropagators()
//                     .getTextMapPropagator()
//                     .inject(Context.current(), s, setter);

        TracingMessage message1 = new TracingMessage("00-" + traceID + "-" + spanID + "-01", message);
        publisher.publish(topic, om.writeValueAsBytes(message1));
    }

    @PreDestroy
    public void destroy() {
        log.info("Destroying PublisherService");
        publisher.stop();
    }

}
