package com.company.persistance.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "tbl_messages")
public class Message {
    @Id
    private long id;
    private String message;
    @Column
    private long personId;
}
