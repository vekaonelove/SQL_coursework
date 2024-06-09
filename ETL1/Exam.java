package com.example.sqlspringbatch.ETL1;

import jakarta.persistence.*;
@Entity
@Table(name = "Exam", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "countryID", nullable = false)
    private Country country;

    @ManyToOne
    @JoinColumn(name = "subjectID", nullable = false)
    private Subject subject;

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
