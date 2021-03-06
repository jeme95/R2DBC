package com.example.demo.task;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

    public Mono<Task> updateTask2(final Task task) {
        return repository.save(task);
    }

    @Transactional
    public Mono deleteTask(final int id) {
        return this.repository.findById(id)
                .flatMap(this.repository::delete);
    }

    public Mono<Long> numberTasks() {
        return repository.count();
    }

    public Mono<Boolean> existsById(Integer id) {
        return repository.existsById(id);
    }

    public Mono<Integer> updateStatus(final Task task) {
        return repository.updateStatus(task.getId(), task.getCompleted());
    }

    Flux<Task> getDescriptionContains(String searchTerm) {
        return repository.findByDescriptionContains(searchTerm);
    }

    Flux<Task> getCompleted() {
        return repository.findAllByCompletedTrue();
    }

    Mono<Page<Task>> getPagination(PageRequest pageRequest) {
        return this.repository.findAllBy(pageRequest)
                .collectList()
                .zipWith(this.repository.count())
                .map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
    }

    // ??? more functionality omitted.


}
