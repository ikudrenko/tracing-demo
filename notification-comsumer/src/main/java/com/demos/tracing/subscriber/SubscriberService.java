package com.demos.tracing.subscriber;

import java.util.Collections;
import javax.annotation.PreDestroy;

import com.demo.tracing.domain.TracingMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sproutsocial.nsq.Subscriber;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Span.Kind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.context.propagation.TextMapPropagator.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SubscriberService {

    private final Subscriber subscriber;
    private final ObjectMapper om = new ObjectMapper();
    private final String topic;
    private final String channel;
    private final Tracer tracer =
          OpenTelemetry.getGlobalTracerProvider().get("com.demos.tracing.subscriber.SubscriberService");
    private static final TextMapPropagator.Getter<String> getter = new Getter<>() {
        @Override
        public Iterable<String> keys(String s) {
            return () -> Collections.singletonList(s).iterator();
        }
        @Override
        public String get(String s, String s2) {
            return s;
        }
    };

    public SubscriberService(
          @Value("${nsq.topic}") String topic,
          @Value("${nsq.channel}") String channel,
          @Value("${nsq.url}") String url) {
        log.info("url {}", url);
        this.subscriber = new Subscriber(url);
        this.channel = channel;
        this.topic = topic;
    }

    @EventListener
    public void handleApplicationReadyEvent(ApplicationReadyEvent event) {
        log.info("Starting subscription on {} topic {} channel", topic, channel);
        subscriber.subscribe(topic, channel, this::handleMessage);
    }

    @SneakyThrows
    private void handleMessage(byte[] data) {
        log.info("Received message {}", new String(data));
        TracingMessage dm = om.readValue(data, TracingMessage.class);

        Context extractedContext = OpenTelemetry.getGlobalPropagators()
                                                .getTextMapPropagator()
                                                .extract(Context.current(), dm.getTraceHeader(), getter);
        Span child = tracer.spanBuilder("handleMessage").setParent(extractedContext).setSpanKind(Kind.CONSUMER).startSpan();
        try (Scope scope = child.makeCurrent()) {
            log.info("Span id {}", child.getSpanContext().getSpanIdAsHexString());
            child.setAttribute("operation", "Save to db");
        } finally {
            child.end();
        }

    }

    @PreDestroy
    public void destroy() {
        log.info("Destroying SubscriberService");
        subscriber.stop();
    }
}
