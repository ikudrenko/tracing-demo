package com.demo.tracing.web;

import java.util.List;

import com.demo.tracing.model.Item;
import com.demo.tracing.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {


    private final ItemService service;

    @GetMapping
    public List<Item> getFlights() {
        return service.getAllItems();
    }
}
