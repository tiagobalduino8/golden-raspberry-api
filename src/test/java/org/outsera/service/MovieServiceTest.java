package org.outsera.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.outsera.entity.Movie;
import org.outsera.entity.Producer;
import org.outsera.repository.MovieRepository;
import org.outsera.repository.ProducerRepository;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    MovieRepository movieRepository;

    @Mock
    ProducerRepository producerRepository;

    @InjectMocks
    MovieService service;

    @Test
    void testParseProducers() {
        List<String> producers = service.parseProducers("Producer A, Producer B and Producer C");
        
        assertEquals(3, producers.size());
        assertEquals("Producer A", producers.get(0));
        assertEquals("Producer B", producers.get(1));
        assertEquals("Producer C", producers.get(2));
    }

    @Test
    void testImportMovies() throws IOException {
        // Adicione o cabeçalho que será ignorado
        String csv = "year;title;studios;producers;winner\n" +
                    "1980;Movie Title;Studio;Producer A and Producer B;yes\n" +
                    "1990;Another Movie;Studio;Producer C;yes\n";
        
        InputStream inputStream = new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8));
        
        // Configure para qualquer string não vazia
        when(producerRepository.findByName(argThat(name -> !name.isEmpty())))
            .thenReturn(Optional.empty());
        
        service.importMovies(inputStream);
        
        verify(movieRepository, times(2)).persistAndFlush(any(Movie.class));
        verify(producerRepository, times(3)).persist(any(Producer.class));
    }

    @Test
    void testExistingProducer() throws IOException {
        String csv = "year;title;studios;producers;winner\n" +
                     "2000;Existing Producer Movie;Studio;Existing Producer;yes\n";
        
        InputStream inputStream = new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8));
        
        Producer existing = new Producer();
        existing.name = "Existing Producer"; 
        when(producerRepository.findByName("Existing Producer"))
            .thenReturn(Optional.of(existing));
        
        try {
            service.importMovies(inputStream);
        } catch (Exception e) {
            fail("Exceção inesperada: " + e.getMessage());
        }
        
        verify(producerRepository, never()).persist(any(Producer.class));
        verify(movieRepository, times(1)).persistAndFlush(any(Movie.class));
    }
}