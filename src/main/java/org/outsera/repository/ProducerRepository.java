package org.outsera.repository;

import java.util.Optional;

import org.outsera.entity.Producer;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProducerRepository implements PanacheRepository<Producer> {
	public Optional<Producer> findByName(String name) {
		return find("name", name).firstResultOptional();
	}
}
