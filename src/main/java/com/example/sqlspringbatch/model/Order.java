package com.example.sqlspringbatch.model;
import jakarta.persistence.*;

@Entity
@Table(name = "\"Order\"")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subjectID", nullable = false)
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "performerID", nullable = false)
    private Performer performer;

    @ManyToOne
    @JoinColumn(name = "examID", nullable = false)
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "cityID", nullable = false)
    private City city;

    @ManyToOne
    @JoinColumn(name = "customerID", nullable = false)
    private Customer customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Performer getPerformer() {
        return performer;
    }

    public void setPerformer(Performer performer) {
        this.performer = performer;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}

