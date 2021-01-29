package com.demo.tracing.web;

import java.util.List;

import com.demo.tracing.model.Item;
import com.demo.tracing.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemRepository repo;

    @GetMapping
    public List<Item> getFlights() {
        return repo.findAll();
    }
}
