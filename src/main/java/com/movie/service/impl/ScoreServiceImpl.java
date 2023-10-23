package com.movie.service.impl;

import com.movie.domain.Movie;
import com.movie.domain.Score;
import com.movie.domain.User;
import com.movie.dto.MovieDTO;
import com.movie.dto.ScoreDTO;
import com.movie.repository.MovieRepository;
import com.movie.repository.ScoreRepository;
import com.movie.repository.UserRepository;
import com.movie.service.ScoreService;
import com.movie.service.exception.BusinessException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Transactional
    public MovieDTO saveScore(ScoreDTO dto) {

        User user = userRepository.findByEmail(dto.getEmail());

        if (user != null){
            user.setEmail(dto.getEmail());
        }else{
            throw new BusinessException("The email does not correspond to any registered user");
        }

        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new BusinessException(String.format("You cannot assign a score because the movie with ID %d does not exist", dto.getMovieId())));

        Score score = new Score();
        score.setMovie(movie);
        score.setUser(user);

        if(dto.getScore() < 0){
            score.setValue(dto.getScore());
        }else{
            throw new BusinessException("Score cannot be less than 0");
        }

        score = scoreRepository.saveAndFlush(score);

        double sum = 0.0;
        for (Score s: movie.getScores()) {
            sum =  sum + s.getValue();
        }

        double avg = sum / movie.getScores().size();

        movie.setScore(avg);
        movie.setCount(movie.getScores().size());

        movie = movieRepository.save(movie);

        return new MovieDTO(movie);
    }

}
