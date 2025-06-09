package org.outsera.resource;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.outsera.dto.ProducerIntervalResponse;
import org.outsera.service.MovieService;
import org.outsera.service.ProducerIntervalService;

import jakarta.ws.rs.core.Response;

@ExtendWith(MockitoExtension.class)
class MovieResourceTest {

    @Mock
    MovieService movieService;

    @Mock
    ProducerIntervalService intervalService;

    @InjectMocks
    MovieResource resource;

    @Test
    void testImportMoviesSuccess() throws Exception {
        InputStream dummyStream = new ByteArrayInputStream("test".getBytes());
        
        Response response = resource.importMovies();
        
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(movieService).importMovies(any());
    }

    @Test
    void testGetProducerIntervals() {
        ProducerIntervalService.ProducerIntervalsProjection projection = 
            new ProducerIntervalService.ProducerIntervalsProjection(
                Collections.emptyList(),
                Collections.emptyList()
            );
        
        when(intervalService.getProducerIntervals()).thenReturn(projection);
        
        Response response = resource.getProducerIntervals();
        
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof ProducerIntervalResponse);
    }
}
