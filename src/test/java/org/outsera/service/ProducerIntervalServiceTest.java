package org.outsera.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.outsera.entity.Movie;
import org.outsera.entity.Producer;
import org.outsera.repository.MovieRepository;

@ExtendWith(MockitoExtension.class)
class ProducerIntervalServiceTest {

    @Mock
    MovieRepository movieRepository;

    @InjectMocks
    ProducerIntervalService service;

    @Test
    void testEmptyResults() {
        when(movieRepository.findWinnerMovies()).thenReturn(Collections.emptyList());
        
        ProducerIntervalService.ProducerIntervalsProjection result = service.getProducerIntervals();
        
        assertTrue(result.min().isEmpty());
        assertTrue(result.max().isEmpty());
    }

    @Test
    void testSingleProducerMultipleWins() {
        Movie movie1 = new Movie();
        movie1.year = 2000;
        movie1.producers = Collections.singletonList(createProducer("Producer A"));
        
        Movie movie2 = new Movie();
        movie2.year = 2005;
        movie2.producers = Collections.singletonList(createProducer("Producer A"));
        
        when(movieRepository.findWinnerMovies()).thenReturn(Arrays.asList(movie1, movie2));
        
        ProducerIntervalService.ProducerIntervalsProjection result = service.getProducerIntervals();
        
        assertEquals(1, result.min().size());
        assertEquals(1, result.max().size());
        assertEquals(5, result.min().get(0).interval());
        assertEquals("Producer A", result.min().get(0).producer());
        assertEquals(2000, result.min().get(0).previousWin());
        assertEquals(2005, result.min().get(0).followingWin());
    }

    @Test
    void testMultipleProducers() {
        Movie movie1 = new Movie();
        movie1.year = 1990;
        movie1.producers = Collections.singletonList(createProducer("Producer A"));
        
        Movie movie2 = new Movie();
        movie2.year = 1995;
        movie2.producers = Collections.singletonList(createProducer("Producer A"));
        
        Movie movie3 = new Movie();
        movie3.year = 2000;
        movie3.producers = Collections.singletonList(createProducer("Producer B"));
        
        Movie movie4 = new Movie();
        movie4.year = 2010;
        movie4.producers = Collections.singletonList(createProducer("Producer B"));
        
        when(movieRepository.findWinnerMovies()).thenReturn(Arrays.asList(movie1, movie2, movie3, movie4));
        
        ProducerIntervalService.ProducerIntervalsProjection result = service.getProducerIntervals();
        
        assertEquals(1, result.min().size());
        assertEquals(1, result.max().size());
        assertEquals(5, result.min().get(0).interval());
        assertEquals(10, result.max().get(0).interval());
    }

    private Producer createProducer(String name) {
        Producer p = new Producer();
        p.name = name;
        return p;
    }
}
