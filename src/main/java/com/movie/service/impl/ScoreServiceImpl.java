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
    private static final Long UNCHANGEABLE_MOVIE_ID = 1L;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Transactional
    public MovieDTO saveScore(ScoreDTO dto) {

        User user = userRepository.findByEmail(dto.getEmail());
        if (user == null) {
            user = new User();
            user.setEmail(dto.getEmail());
            user = userRepository.saveAndFlush(user);
        }

        this.validateChangeableId(dto.getMovieId(), "score");

        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new NoSuchElementException("Movie not found"));

        Score score = new Score();
        score.setMovie(movie);
        score.setUser(user);
        score.setValue(dto.getScore());

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

    private void validateChangeableId(Long id, String operation) {
        if (UNCHANGEABLE_MOVIE_ID.equals(id)) {
            throw new BusinessException("Movie with ID %d cannot be %s because it does not exist".formatted(UNCHANGEABLE_MOVIE_ID, operation));
        }
    }

}
