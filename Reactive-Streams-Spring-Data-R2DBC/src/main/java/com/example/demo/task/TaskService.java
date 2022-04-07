package com.example.demo.task;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
