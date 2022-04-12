package com.example.demo.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/tasks")
public class TaskController {

    private TaskService taskService;
    Logger logger = LoggerFactory.getLogger(TaskController.class);

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    //  Request-Example: http://localhost:8080/api/tasks
    @GetMapping()
    public ResponseEntity<Flux> getAll() {
        logger.info("getAll()");
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PostMapping()
    public ResponseEntity<Mono> create(@RequestBody Task task) {
        logger.info("create() : {} : {}", task.getId(), task.getDescription());
        if (taskService.isValid(task)) {
            return ResponseEntity.ok(taskService.createTask(task));
        }
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
    }

    @PutMapping()
    public ResponseEntity<Mono> updateTask(@RequestBody Task task) {
        logger.info("updateTask() : {} : {}", task.getId(), task.getDescription());
        if (taskService.isValid(task)) {
            return ResponseEntity.ok(taskService.updateTask(task));
        }
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
    }

    @PutMapping("save")
    public ResponseEntity<Mono> updateTask2(@RequestBody Task task) {
        logger.info("updateTask2() : {} : {}", task.getId(), task.getDescription());
        if (taskService.isValid(task)) {
            return ResponseEntity.ok(taskService.updateTask2(task));
        }
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
    }

    @DeleteMapping()
    public ResponseEntity<Mono> delete(@RequestParam int id) {
        logger.info("delete() : {}", id);
        if (id > 0) {
            return ResponseEntity.ok(taskService.deleteTask(id));
        }
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
    }

    //  Request-Example: http://localhost:8080/api/tasks/number
    @GetMapping("number")
    public ResponseEntity<Mono<Long>> getNumberTasks() {
        logger.info("getNumberTasks()");
        return ResponseEntity.ok(taskService.numberTasks());
    }

    @PutMapping("status")
    public ResponseEntity<Mono> updateStatus(@RequestBody Task task) {
        logger.info("updateStatus() : {} : {}", task.getId(), task.getDescription());
        if (taskService.isValid(task)) {
            return ResponseEntity.ok(taskService.updateStatus(task));
        }
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
    }

    //  Request-Example: http://localhost:8080/api/tasks/contains?searchTerm=task
    @GetMapping("contains")
    public ResponseEntity<Flux<Task>> getDescriptionContains(@RequestParam("searchTerm") String searchTerm) {
        logger.info("getDescriptionContains({}) ", searchTerm);
        return ResponseEntity.ok(taskService.getDescriptionContains(searchTerm));
    }

    //  Request-Example: http://localhost:8080/api/tasks/completed
    @GetMapping("completed")
    public ResponseEntity<Flux> getCompleted() {
        logger.info("getCompleted()");
        return ResponseEntity.ok(taskService.getCompleted());
    }


}
