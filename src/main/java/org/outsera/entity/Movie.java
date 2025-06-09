package org.outsera.entity;

import java.util.ArrayList;
import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class Movie extends PanacheEntity {
 @Column(name = "release_year")	
 public int year;
 public String title;
 public String studios;
 public boolean winner;

 @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
 @JoinTable(name = "movie_producer",
     joinColumns = @JoinColumn(name = "movie_id"),
     inverseJoinColumns = @JoinColumn(name = "producer_id"))
 public List<Producer> producers = new ArrayList<>();
}
