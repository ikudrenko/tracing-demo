package com.demo.tracing.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.demo.tracing.domain.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class ItemsClient {

    private final String providers1URI;
    private final String providers2URI;


    @Autowired
    private RestTemplate restTemplate;

    public ItemsClient(@Value("${provider1.uri}") String providers1URI,
          @Value("${provider2.uri}") String providers2URI) {
        this.providers1URI = providers1URI;
        this.providers2URI = providers2URI;
        log.info("providers1URI = {}", providers1URI);
        log.info("providers2URI = {}", providers2URI);

    }

    public List<Item> getProvider1Items() {
        log.debug("Getting provider 1 flights {}", providers1URI);
        return restTemplate.getForObject(providers1URI, List.class);
    }

    public List<Item> getProvider2Items() {
        log.debug("Getting provider 2 flights {}", providers2URI);
        return restTemplate.getForObject(providers2URI, List.class);
    }

    @Async
    public CompletableFuture<List<Item>> getProvider1FlightsAsync() {
        return CompletableFuture.completedFuture(getProvider1Items());
    }

    @Async
    public CompletableFuture<List<Item>> getProvider2FlightsAsync() {
        return CompletableFuture.completedFuture(getProvider2Items());
    }
}