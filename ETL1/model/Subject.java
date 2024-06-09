package com.example.sqlspringbatch.ETL1.model;
import jakarta.persistence.*;


@Entity
@Table(name = "Subject", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
