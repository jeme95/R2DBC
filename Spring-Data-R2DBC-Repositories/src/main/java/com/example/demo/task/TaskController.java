package com.example.demo.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


    @GetMapping()
    public ResponseEntity<Flux> getAll() {
        logger.info("getAll()");
        return ResponseEntity.ok(this.taskService.getAllTasks());
    }

    @PostMapping()
    public ResponseEntity<Mono> create(@RequestBody Task task) {
        logger.info("create() : {} : {}",task.getId(),task.getDescription());
        if (taskService.isValid(task)) {
            return ResponseEntity.ok(this.taskService.createTask(task));
        }
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
    }

    @PutMapping()
    public ResponseEntity<Mono> updateTask(@RequestBody Task task) {
        logger.info("updateTask() : {} : {}",task.getId(),task.getDescription());
        if (taskService.isValid(task)) {
            return ResponseEntity.ok(this.taskService.updateTask(task));
        }
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
    }

    @DeleteMapping()
    public ResponseEntity<Mono> delete(@RequestParam int id) {
        logger.info("delete() : {}",id);
        if (id > 0) {
            return ResponseEntity.ok(this.taskService.deleteTask(id));
        }
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
    }

}
