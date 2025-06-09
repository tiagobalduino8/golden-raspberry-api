package org.outsera.entity;

import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

@Entity
public class Producer extends PanacheEntity {
 public String name;
 
 @ManyToMany(mappedBy = "producers")
 public List<Movie> movies;
}
