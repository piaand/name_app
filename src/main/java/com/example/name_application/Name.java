package com.example.name_application;

import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import javax.persistence.Column;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Name extends AbstractPersistable<Long> {

    private Long amount;

    @Column(name="firstName", unique=true)
    private String name;
}
