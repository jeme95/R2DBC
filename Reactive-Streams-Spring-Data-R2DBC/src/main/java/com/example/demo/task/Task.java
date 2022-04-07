package com.example.demo.task;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Data
@RequiredArgsConstructor
@Table("tasks")
public class Task {
    @Id
    private int id;
    @NonNull
    private String description;
    private Boolean completed;

}

