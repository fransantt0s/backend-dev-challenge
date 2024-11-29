package com.eron.challenge.controller;
import com.eron.challenge.dto.MovieDto;
import com.eron.challenge.dto.ResponseDto;
import com.eron.challenge.service.impl.MovieServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.manyTimes;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class MovieControllerTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MovieServiceImpl movieService;

    private MockRestServiceServer mockServer;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        movieService = new MovieServiceImpl(restTemplate);
        mockServer = MockRestServiceServer.createServer(restTemplate);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetDirectors_withThreshold() throws Exception {
        MovieDto movie1 = new MovieDto("Title1", "2021", "Rated", "2022-01-01", "130 min", "Action", "Director1", "Writer1", "Actor1");
        MovieDto movie2 = new MovieDto("Title2", "2022", "Rated", "2023-04-01", "140 min", "Drama", "Director1", "Writer2", "Actor2");
        MovieDto movie3 = new MovieDto("Title3", "2023", "Rated", "2023-05-01", "120 min", "Comedy", "Director2", "Writer3", "Actor3");

        ResponseDto page1 = new ResponseDto(1, 1, 2, 2, Arrays.asList(movie1, movie2));
        ResponseDto page2 = new ResponseDto(2, 1, 2, 2, Arrays.asList(movie3));
        
        mockServer.expect(manyTimes(), requestTo("https://eron-movies.wiremockapi.cloud/api/movies/search?page=1"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(page1), MediaType.APPLICATION_JSON));
        mockServer.expect(manyTimes(), requestTo("https://eron-movies.wiremockapi.cloud/api/movies/search?page=2"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(page2), MediaType.APPLICATION_JSON));
        
        List<String> result = movieService.getDirectors(2);
        
        assertEquals(Arrays.asList("Director1"), result);
        mockServer.verify();
    }
}
