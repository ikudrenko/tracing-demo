package com.demo.tracing.service;

import java.time.Instant;
import java.util.List;

import com.demo.tracing.model.Item;
import com.demo.tracing.repository.ItemRepository;
import io.opentelemetry.extension.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository repository;
    private final NotificationPublisher publisher;

    @WithSpan
    public List<Item> getAllItems() {
        publisher.publishMessageAsync("New get All items request" + Instant.now());
        return repository.findAll();
    }
}
