package com.movie.service.impl;

import com.movie.domain.Movie;
import com.movie.repository.MovieRepository;
import com.movie.service.MovieService;
import com.movie.service.exception.BusinessException;
import com.movie.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Optional.ofNullable;

@Service
public class MovieServiceImpl implements MovieService {
    private static final Long UNCHANGEABLE_MOVIE_ID = 1L;

    @Autowired
    private MovieRepository movieRepository;

    @Transactional(readOnly = true)
    public List<Movie> findAll() {
        return this.movieRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Movie findById(Long id) {
        return this.movieRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Movie create(Movie movieToCreate) {
        ofNullable(movieToCreate).orElseThrow(() -> new BusinessException("Movie to create must not be null."));

        this.validateChangeableId(movieToCreate.getId(), "created");
        return this.movieRepository.save(movieToCreate);
    }

    @Transactional
    public Movie update(Long id, Movie movieToUpdate) {
        this.validateChangeableId(id, "updated");
        Movie dbMovie = this.findById(id);
        if (!dbMovie.getId().equals(movieToUpdate.getId())) {
            throw new BusinessException("Update IDs must be the same.");
        }
        dbMovie.setTitle(movieToUpdate.getTitle());
        dbMovie.setScore(movieToUpdate.getScore());
        dbMovie.setCount(movieToUpdate.getCount());
        dbMovie.setImage(movieToUpdate.getImage());

        return this.movieRepository.save(dbMovie);
    }

    @Override
    public void delete(Long id) {
        this.validateChangeableId(id, "deleted");
        Movie dbMovie = this.findById(id);
        this.movieRepository.delete(dbMovie);
    }

    private void validateChangeableId(Long id, String operation) {
        if (UNCHANGEABLE_MOVIE_ID.equals(id)) {
            throw new BusinessException("Movie with ID %d can not be %s.".formatted(UNCHANGEABLE_MOVIE_ID, operation));
        }
    }

}
