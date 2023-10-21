package com.movie.repository;

import com.movie.domain.Score;
import com.movie.domain.ScorePK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, ScorePK> {

}
