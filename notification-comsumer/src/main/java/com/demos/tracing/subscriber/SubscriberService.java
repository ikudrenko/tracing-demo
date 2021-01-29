package com.demos.tracing.subscriber;

import javax.annotation.PreDestroy;

import com.demo.tracing.domain.TracingMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sproutsocial.nsq.Subscriber;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SubscriberService {

    private final Subscriber subscriber ;
    private ObjectMapper om = new ObjectMapper();
    private final String topic;
    private final String channel;

    public SubscriberService(@Value("${nsq.topic}") String topic,
          @Value("${nsq.channel}") String channel,
          @Value("${nsq.url}") String url) {
        subscriber = new Subscriber(url);
        log.info("SUbscription url {}", url);
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
        log.info("TracingMessage  {}", TracingMessage.class);
//
//        Context extractedContext = OpenTelemetry.getGlobalPropagators()
//                                                .getTextMapPropagator()
//                                                .extract(Context.current(), dm.getTraceparent(), getter);
//        Span span1 = Span.fromContext(extractedContext);
//        Span child = tracer.spanBuilder("handleMessage").setParent(extractedContext).setSpanKind(Kind.CONSUMER).startSpan();
//        try (Scope scope = child.makeCurrent()) {
//            log.info("Span id {}", child.getSpanContext().getSpanIdAsHexString());
//            child.setAttribute("operation", "Save to db");
//        } finally {
//            child.end();
//        }
//        log.info("Span id {}", span1.getSpanContext().getSpanIdAsHexString());

    }

    @PreDestroy
    public void destroy() {
        log.info("Destroying SubscriberService");
        subscriber.stop();
    }
}
