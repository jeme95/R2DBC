package com.example.demo.contact;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Data
@RequiredArgsConstructor
@Table("contacts")
public class Contact {
    @Id
    private int id;
    private String first_name;
    private String last_name;
    private String email;

}
