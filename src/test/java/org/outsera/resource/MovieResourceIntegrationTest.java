package org.outsera.resource;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.outsera.dto.ProducerIntervalResponse;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MovieResourceIntegrationTest {

    @Test
    @Order(1)
    public void importMovies() {
        given()
            .when().post("/movies/import")
            .then()
            .statusCode(200);
    }

    @Test
    @Order(2)
    public void testProducerIntervalsWithGoldenRaspberryData() {
        ProducerIntervalResponse response = given()
            .when().get("/movies/producers-intervals")
            .then()
            .statusCode(200)
            .contentType(MediaType.APPLICATION_JSON)
            .extract().as(ProducerIntervalResponse.class);

        // Depuração
        System.out.println("Min intervals found: " + response.min.size());
        response.min.forEach(i -> 
            System.out.println(i.producer + ": " + i.interval + " (" + i.previousWin + "-" + i.followingWin + ")")
        );
        
        System.out.println("Max intervals found: " + response.max.size());
        response.max.forEach(i -> 
            System.out.println(i.producer + ": " + i.interval + " (" + i.previousWin + "-" + i.followingWin + ")")
        );

        // Verificação mínima
        assertEquals(1, response.min.size(), "Deveria haver 1 produtor com intervalo mínimo");
        assertEquals("Joel Silver", response.min.get(0).producer);
        assertEquals(1, response.min.get(0).interval);
        assertEquals(1990, response.min.get(0).previousWin);
        assertEquals(1991, response.min.get(0).followingWin);

        // Verificação máxima
        assertEquals(1, response.max.size(), "Deveria haver 1 produtor com intervalo máximo");
        assertEquals("Matthew Vaughn", response.max.get(0).producer);
        assertEquals(13, response.max.get(0).interval);
        assertEquals(2002, response.max.get(0).previousWin);
        assertEquals(2015, response.max.get(0).followingWin);
    }
}