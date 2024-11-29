package com.eron.challenge.controller;

import com.eron.challenge.dto.DirectorsResponseDto;
import com.eron.challenge.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieController {
    
    @Autowired
    private MovieService movieService;

    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);
    
    @GetMapping("/api/directors")
    public ResponseEntity<DirectorsResponseDto> getDirectors(@RequestParam int threshold) {
        try {
            List<String> directors = movieService.getDirectors(threshold);
            DirectorsResponseDto response = new DirectorsResponseDto(directors);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e) {
            logger.error("Error getting directors", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
