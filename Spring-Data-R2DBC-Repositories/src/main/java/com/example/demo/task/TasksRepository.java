package com.example.demo.task;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TasksRepository extends ReactiveCrudRepository<Task, Integer> {}
