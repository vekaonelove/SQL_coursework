package com.example.sqlspringbatch.model;

import jakarta.persistence.*;

@Entity
@Table(name = "City", uniqueConstraints = {@UniqueConstraint(columnNames = {"countryID", "name"})})
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "countryID", nullable = false)
    private Country country;

    @Column(nullable = false)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        if (getCountry() != null ? !getCountry().equals(city.getCountry()) : city.getCountry() != null) return false;
        return getName() != null ? getName().equals(city.getName()) : city.getName() == null;
    }

    @Override
    public int hashCode() {
        int result = getCountry() != null ? getCountry().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }
}
