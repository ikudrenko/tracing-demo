package com.demo.tracing.repository;

import java.util.List;

import com.demo.tracing.model.Item;
import io.opentelemetry.extension.annotations.WithSpan;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Long> {

    @WithSpan
    List<Item> findAll();
}
