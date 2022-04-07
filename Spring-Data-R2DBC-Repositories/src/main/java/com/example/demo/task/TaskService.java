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

    public Boolean isValid(final Task task) {
        return task != null && !task.getDescription().isEmpty();
    }

    Mono<Task> createTask(Task task) {
        return repository.save(task);
    }

    Flux<Task> getAllTasks() {
        return repository.findAll();
    }

    @Transactional
    public Mono<Task> updateTask(final Task task) {
        return this.repository.findById(task.getId())
                .flatMap(t -> {
                    t.setDescription(task.getDescription());
                    t.setCompleted(task.getCompleted());
                    return this.repository.save(t);
                });
    }



    @Transactional
    public Mono deleteTask(final int id){
        return this.repository.findById(id)
                .flatMap(this.repository::delete);
    }

}
