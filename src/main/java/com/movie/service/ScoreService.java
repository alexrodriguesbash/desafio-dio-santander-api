package com.movie.service;

import com.movie.dto.MovieDTO;
import com.movie.dto.ScoreDTO;

public interface ScoreService {
    public MovieDTO saveScore(ScoreDTO dto);
}
