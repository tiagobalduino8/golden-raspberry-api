package org.outsera.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.outsera.entity.Movie;
import org.outsera.entity.Producer;
import org.outsera.repository.MovieRepository;
import org.outsera.repository.ProducerRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class MovieService {

	@Inject
	MovieRepository movieRepository;

	@Inject
	ProducerRepository producerRepository;

	@Transactional
	public void importMovies(InputStream csvStream) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvStream, StandardCharsets.UTF_8))) {
			reader.readLine();

			String line;
			while ((line = reader.readLine()) != null) {
				String[] fields = line.split(";", -1);
				if (fields.length < 5)
					continue;

				Movie movie = new Movie();
				movie.year = Integer.parseInt(fields[0].trim());
				movie.title = fields[1].trim();
				movie.studios = fields[2].trim();
				movie.winner = "yes".equalsIgnoreCase(fields[4].trim());

				parseProducers(fields[3].trim()).forEach(producerName -> {
					Producer producer = producerRepository.findByName(producerName).orElseGet(() -> {
						Producer p = new Producer();
						p.name = producerName;
						producerRepository.persist(p);
						return p;
					});
					movie.producers.add(producer);
				});

				movieRepository.persistAndFlush(movie);
			}
		}
	}

	public List<String> parseProducers(String producersString) {
		return Arrays.stream(producersString.split(",(?![^()]*\\))|\\b(and)\\b")).map(String::trim)
				.filter(name -> !name.isEmpty()).collect(Collectors.toList());
	}
}
