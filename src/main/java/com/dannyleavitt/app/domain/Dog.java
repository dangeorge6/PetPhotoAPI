package com.dannyleavitt.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Dog.
 */
@Entity
@Table(name = "dog")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Dog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "years_old")
    private Integer years_old;

    @ManyToOne
    private Breed breed;

    @OneToMany(mappedBy = "dog", cascade = CascadeType.PERSIST)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DogPhoto> dogPhotos = new HashSet<>();

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

    public Integer getYears_old() {
        return years_old;
    }

    public void setYears_old(Integer years_old) {
        this.years_old = years_old;
    }

    public Breed getBreed() {
        return breed;
    }

    public void setBreed(Breed breed) {
        this.breed = breed;
    }

    public Set<DogPhoto> getDogPhotos() {
        return dogPhotos;
    }

    public void setDogPhotos(Set<DogPhoto> dogPhotos) {
        this.dogPhotos = dogPhotos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Dog dog = (Dog) o;
        if(dog.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, dog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Dog{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", years_old='" + years_old + "'" +
            '}';
    }
}
