package org.outsera.config;

import java.io.IOException;
import java.io.InputStream;

import org.outsera.service.MovieService;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class Startup {

	@Inject
	MovieService movieService;

	void onStart(@Observes StartupEvent ev) {
		try {
			InputStream csvStream = getClass().getClassLoader().getResourceAsStream("movielist.csv");
			if (csvStream != null) {
				movieService.importMovies(csvStream);
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to import movies", e);
		}
	}
}
