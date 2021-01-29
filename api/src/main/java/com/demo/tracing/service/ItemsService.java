package com.demo.tracing.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.demo.tracing.domain.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemsService {

    private final ItemsClient itemsClient;

    @SneakyThrows
    public List<Item> getItems() {
        CompletableFuture<List<Item>> items1 = itemsClient.getProvider1FlightsAsync();
        CompletableFuture<List<Item>> items2 = itemsClient.getProvider2FlightsAsync();

        CompletableFuture
              .allOf(items1, items2)
              .join();
        List<Item> result = new ArrayList<>();
        result.addAll(items1.get());
        result.addAll(items2.get());

        return result;
    }
}
