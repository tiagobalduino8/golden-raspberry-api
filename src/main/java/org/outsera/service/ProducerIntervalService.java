package org.outsera.service;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.outsera.entity.Movie;
import org.outsera.repository.MovieRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProducerIntervalService {

    @Inject
    MovieRepository movieRepository;

    public ProducerIntervalsProjection getProducerIntervals() {
        List<Movie> winnerMovies = movieRepository.findWinnerMovies();
        Map<String, List<Integer>> winsByProducer = groupWinsByProducer(winnerMovies);
        List<ProducerInterval> allIntervals = calculateAllIntervals(winsByProducer);
        return findMinMaxIntervals(allIntervals);
    }

    private Map<String, List<Integer>> groupWinsByProducer(List<Movie> winnerMovies) {
        return winnerMovies.stream()
            .flatMap(movie -> movie.producers.stream()
                .map(producer -> new AbstractMap.SimpleEntry<>(producer.name, movie.year)))
            .collect(Collectors.groupingBy(
                Map.Entry::getKey,
                Collectors.mapping(
                    Map.Entry::getValue,
                    Collectors.toList()
                )
            ));
    }

    private List<ProducerInterval> calculateAllIntervals(Map<String, List<Integer>> winsByProducer) {
        List<ProducerInterval> intervals = new ArrayList<>();
        
        winsByProducer.forEach((producer, years) -> {
            if (years.size() < 2) return;
            
            List<Integer> sortedYears = years.stream()
                .distinct()
                .sorted()
                .collect(Collectors.toList());
            
            for (int i = 1; i < sortedYears.size(); i++) {
                int previousWin = sortedYears.get(i - 1);
                int followingWin = sortedYears.get(i);
                int interval = followingWin - previousWin;
                
                intervals.add(new ProducerInterval(
                    producer, 
                    interval, 
                    previousWin, 
                    followingWin
                ));  
            }
        });
        
        return intervals;
    }

    private ProducerIntervalsProjection findMinMaxIntervals(List<ProducerInterval> intervals) {
        if (intervals.isEmpty()) {
            return new ProducerIntervalsProjection(Collections.emptyList(), Collections.emptyList());
        }
        
        int minInterval = findMinInterval(intervals);
        int maxInterval = findMaxInterval(intervals);
        
        List<ProducerInterval> minIntervals = filterIntervalsByValue(intervals, minInterval);
        List<ProducerInterval> maxIntervals = filterIntervalsByValue(intervals, maxInterval);
        
        return new ProducerIntervalsProjection(minIntervals, maxIntervals);
    }

    private int findMinInterval(List<ProducerInterval> intervals) {
        return intervals.stream()
            .mapToInt(ProducerInterval::interval)
            .min()
            .orElse(0);
    }

    private int findMaxInterval(List<ProducerInterval> intervals) {
        return intervals.stream()
            .mapToInt(ProducerInterval::interval)
            .max()
            .orElse(0);
    }

    private List<ProducerInterval> filterIntervalsByValue(List<ProducerInterval> intervals, int value) {
        return intervals.stream()
            .filter(i -> i.interval() == value)
            .collect(Collectors.toList());
    }
    
    public record ProducerInterval(
        String producer, 
        int interval, 
        int previousWin, 
        int followingWin
    ) {}
    
    public record ProducerIntervalsProjection(
        List<ProducerInterval> min,
        List<ProducerInterval> max
    ) {}
}