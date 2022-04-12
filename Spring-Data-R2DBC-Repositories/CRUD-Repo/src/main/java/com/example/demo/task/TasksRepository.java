package com.example.demo.task;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TasksRepository extends ReactiveCrudRepository<Task, Integer> {

    @Modifying
    @Query("UPDATE tasks SET completed = :completed WHERE id = :id")
    Mono<Integer> updateStatus(Integer id, Boolean completed);

}
