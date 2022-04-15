package com.example.demo.task;

import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private TaskRepository repository;


    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    Iterable<Task> getAllTasks() {
        return repository.findAll();
    }

}
