package org.outsera.repository;

import java.util.List;

import org.outsera.entity.Movie;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MovieRepository implements PanacheRepository<Movie> {
	public List<Movie> findWinnerMovies() {
		return list("winner", true);
	}
}
