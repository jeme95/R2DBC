package com.example.demo.product;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
public interface ProductRepository extends ReactiveSortingRepository<Product, Integer> {

    Flux<Product> findAll(Sort sort);
}
