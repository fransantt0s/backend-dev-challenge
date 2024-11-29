package com.eron.challenge.service.impl;

import com.eron.challenge.dto.MovieDto;
import com.eron.challenge.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class MovieServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MovieServiceImpl movieService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetDirectors_withThreshold() throws Exception {
        MovieDto movie1 = new MovieDto("Title1", "2021", "Rated", "2022-01-01", "130 min", "Action", "Director1", "Writer1", "Actor1");
        MovieDto movie2 = new MovieDto("Title2", "2022", "Rated", "2025-01-01", "140 min", "Drama", "Director1", "Writer2", "Actor2");
        MovieDto movie3 = new MovieDto("Title3", "2023", "Rated", "2023-01-01", "120 min", "Comedy", "Director2", "Writer3", "Actor3");
        ResponseDto responseDtoPage1 = new ResponseDto(1, 1, 2, 2, Arrays.asList(movie1, movie2));
        ResponseDto responseDtoPage2 = new ResponseDto(2, 2, 2, 2, Arrays.asList(movie3));
        
        when(restTemplate.getForEntity("https://eron-movies.wiremockapi.cloud/api/movies/search?page=1", byte[].class))
                .thenReturn(ResponseEntity.ok(objectMapper.writeValueAsBytes(responseDtoPage1)));
        when(restTemplate.getForEntity("https://eron-movies.wiremockapi.cloud/api/movies/search?page=2", byte[].class))
                .thenReturn(ResponseEntity.ok(objectMapper.writeValueAsBytes(responseDtoPage2)));
        
        List<String> directors = movieService.getDirectors(2);
        
        assertEquals(Arrays.asList("Director1"), directors); 
    }

}