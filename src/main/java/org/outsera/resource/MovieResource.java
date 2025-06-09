package org.outsera.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.outsera.dto.ProducerIntervalResponse;
import org.outsera.service.MovieService;
import org.outsera.service.ProducerIntervalService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

//MovieResource.java
@Path("/movies")
public class MovieResource {

 @Inject
 MovieService movieService;
 
 @Inject
 ProducerIntervalService intervalService;

 @POST
 @Path("/import")
 @Transactional
 public Response importMovies() throws IOException {
     InputStream csvStream = getClass().getClassLoader().getResourceAsStream("movielist.csv");
     if (csvStream == null) {
         return Response.status(Response.Status.NOT_FOUND).entity("CSV file not found").build();
     }
     
     movieService.importMovies(csvStream);
     return Response.ok().entity("Filmes importados com sucesso").build();
 }

 @GET
 @Path("/producers-intervals")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getProducerIntervals() {
     ProducerIntervalService.ProducerIntervalsProjection projection = 
         intervalService.getProducerIntervals();
     
     List<ProducerIntervalResponse.IntervalDTO> minList = projection.min().stream()
         .map(i -> new ProducerIntervalResponse.IntervalDTO(
             i.producer(), i.interval(), i.previousWin(), i.followingWin()))
         .collect(Collectors.toList());
     
     List<ProducerIntervalResponse.IntervalDTO> maxList = projection.max().stream()
         .map(i -> new ProducerIntervalResponse.IntervalDTO(
             i.producer(), i.interval(), i.previousWin(), i.followingWin()))
         .collect(Collectors.toList());
     
     return Response.ok(new ProducerIntervalResponse(minList, maxList)).build();
 }
}
