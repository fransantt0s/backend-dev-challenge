package com.eron.challenge.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class DirectorsResponseDto {
    
    private List<String> directors;

    public DirectorsResponseDto(List<String> directors) {
        this.directors = directors;
    }
    
}