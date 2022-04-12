package com.example.demo.product;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Data
@RequiredArgsConstructor
@Table("products")
public class Product {
    @Id
    private int id;
    private String name;
    private double price;

}

