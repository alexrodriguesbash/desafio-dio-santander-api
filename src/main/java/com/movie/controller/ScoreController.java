package com.movie.controller;

import com.movie.dto.MovieDTO;
import com.movie.dto.ScoreDTO;
import com.movie.service.ScoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/scores")
@Tag(name = "Score Controller", description = "Score for a film.")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @PutMapping
    @Operation(summary = "Create a new score", description = "Create a new score for a film.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Score created successfully"),
            @ApiResponse(responseCode = "422", description = "User not found")
    })
    public MovieDTO saveScore(@RequestBody ScoreDTO dto){
        return scoreService.saveScore(dto);
    }

}
