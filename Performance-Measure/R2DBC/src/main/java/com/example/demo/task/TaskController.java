package com.example.demo.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("api/tasks")
public class TaskController {

//    if you want to check which cores and threads are used
//    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @GetMapping()
    public Flux<Task> getAll() {

        //  if you want to check which cores and threads are used
        //  return taskService.getAllTasks().doOnNext(task -> LOGGER.info("Server produces: {}", task));
        return taskService.getAllTasks();
    }

}
