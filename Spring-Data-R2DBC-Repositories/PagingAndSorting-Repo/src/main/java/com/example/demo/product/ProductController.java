package com.example.demo.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;


@RestController
@RequestMapping("api/tasks")
public class ProductController {

    private ProductService taskService;
    Logger logger = LoggerFactory.getLogger(ProductController.class);

    public ProductController(ProductService taskService) {
        this.taskService = taskService;
    }

    @GetMapping()
    public ResponseEntity<Flux> getAll() {
        logger.info("getAll()");
        return ResponseEntity.ok(taskService.allProducts);
    }


}
