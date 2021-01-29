package com.demo.tracing.web;

import java.util.List;

import com.demo.tracing.domain.Item;
import com.demo.tracing.service.ItemsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ItemsController {

    @Autowired
    private ItemsService itemService;

    @GetMapping("/items")
    public List<Item> getFlights() {

        return itemService.getItems();
    }
}
