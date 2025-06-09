package org.outsera.dto;

import java.util.List;

public class ProducerIntervalResponse {
    public List<IntervalDTO> min;
    public List<IntervalDTO> max;
    
    public ProducerIntervalResponse(List<IntervalDTO> min, List<IntervalDTO> max) {
        this.min = min;
        this.max = max;
    }
    
    public static class IntervalDTO {
        public String producer;
        public int interval;
        public int previousWin;
        public int followingWin;
        
        public IntervalDTO(String producer, int interval, int previousWin, int followingWin) {
            this.producer = producer;
            this.interval = interval;
            this.previousWin = previousWin;
            this.followingWin = followingWin;
        }
    }
}
