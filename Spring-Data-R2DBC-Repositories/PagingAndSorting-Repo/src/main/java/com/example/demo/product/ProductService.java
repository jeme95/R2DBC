package com.example.demo.product;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private ProductRepository repository;

    Pageable firstPageWithTwoElements = PageRequest.of(0, 2);
    Pageable secondPageWithFiveElements = PageRequest.of(1, 5);

    Page<Product> allProducts = repository.findAll(firstPageWithTwoElements);

    List<Product> allTenDollarProducts =
            repository.findAllByPrice(10, secondPageWithFiveElements);

}
