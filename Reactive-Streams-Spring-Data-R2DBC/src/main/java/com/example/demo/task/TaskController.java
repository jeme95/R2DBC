package com.example.demo.task;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/tasks")
public class TaskController {

    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @GetMapping()
    public String getTasks() {

        taskService.getAllTasks().subscribe(new Subscriber<Task>() {
            private Subscription s;
            int arrived = 0;
            final int requestAmount = 2;

            /***
             * when the subscription is created, then initially 2 tasks are requested.
             * @param s Subscription
             */
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("Subscription successfully created");
                this.s = s;
                System.out.println("requesting " + requestAmount + " Tasks...");
                s.request(requestAmount);
            }

            /***
             * whenever two tasks are processed, then 2 more are requested.
             * and thus a server can request a very long list from the DB server piecemeal in such a
             * way that it is not overwhelmed. (considering its capacities)
             * @param task: published task
             */
            @Override
            public void onNext(Task task) {
                arrived++;
                System.out.println("Task has arrived: " + task.getDescription());
                if (arrived % 2 == 0) {
                    System.out.println("requesting another " + requestAmount + " Tasks...");
                    s.request(2);
                }
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("error happened " + t.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("completed");
            }
        });

        return "done";
    }

}
