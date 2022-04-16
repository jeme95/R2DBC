package com.example.demo.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/tasks")
public class TaskController {

    private TaskService taskService;

//    if you want to check which cores and threads are used
//    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping()
    public List<Task> getAll() {
        List<Task> result = new ArrayList<Task>();

        taskService.getAllTasks().forEach(task -> {

//    if you want to check which cores and threads are used
//            LOGGER.info("Server produces: {}", task);

            result.add(task);
        });
        return result;
    }
}
