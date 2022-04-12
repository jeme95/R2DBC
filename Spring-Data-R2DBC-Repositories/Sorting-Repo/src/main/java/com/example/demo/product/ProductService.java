package com.example.demo.product;


import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ProductService {

    private ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }


    Flux<Product> getAllSortedByPrice() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "price"));
    }


}
