package com.eron.challenge.service.impl;

import com.eron.challenge.dto.MovieDto;
import com.eron.challenge.dto.ResponseDto;
import com.eron.challenge.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieServiceImpl.class);

    public List<String> getDirectors(int threshold) {
        Map<String, Integer> directorMovies = new HashMap<>();
        int page = 1;
        int totalPages = 1;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            while (page <= totalPages) {
                String apiUrl = "https://eron-movies.wiremockapi.cloud/api/movies/search?page=" + page;
                ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(apiUrl, byte[].class);

                if (responseEntity.getBody() != null) {
                    ResponseDto response = objectMapper.readValue(responseEntity.getBody(), ResponseDto.class);
                    totalPages = response.getTotalPages();
                    List<MovieDto> movies = response.getData();
                    
                    movies.stream()
                            .map(MovieDto::getDirector)
                            .forEach(director -> directorMovies.put(director, directorMovies.getOrDefault(director, 0) + 1));
                }
                page++;
            }
        } catch (Exception e) {
            LOGGER.error("Error retrieving movie data", e);
        }

        List<String> result = directorMovies.entrySet().stream()
                .filter(entry -> entry.getValue() >= threshold)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());

        return result;
    }

}

