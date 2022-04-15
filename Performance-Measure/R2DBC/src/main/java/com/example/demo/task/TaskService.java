package com.example.demo.task;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class TaskService {

    private TasksRepository repository;


    public TaskService(TasksRepository repository) {
        this.repository = repository;
    }

    Flux<Task> getAllTasks() {
        return repository.findAll();
    }



}
