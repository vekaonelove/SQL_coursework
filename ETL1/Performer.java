package com.example.sqlspringbatch.ETL1;

import jakarta.persistence.*;

@Entity
@Table(name = "Performer", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "surname"})})
public class Performer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @ManyToOne
    @JoinColumn(name = "experienceID")
    private ExperienceLevel experienceLevel;

    @ManyToOne
    @JoinColumn(name = "subjectID")
    private Subject subject;

    private String phoneNumber;
    private String email;

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

