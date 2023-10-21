package com.movie.controller;
import com.movie.dto.MovieDTO;
import com.movie.dto.UserDTO;
import com.movie.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/movies")
@Tag(name = "Movies Controller", description = "RESTful API for managing movies.")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping
    @Operation(summary = "Get all movies", description = "Retrieve a list of all registered movies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful")
    })
    public ResponseEntity<List<MovieDTO>> findAll() {
        var movies = movieService.findAll();
        var moviesDTO = movies.stream().map(MovieDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(moviesDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a movie by ID", description = "Retrieve a specific movie based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<MovieDTO> findById(@PathVariable Long id) {
        var movie = movieService.findById(id);
        return ResponseEntity.ok(new MovieDTO(movie));
    }

    @PostMapping
    @Operation(summary = "Create a new movie", description = "Create a new movie and return the created movies data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Movie created successfully"),
            @ApiResponse(responseCode = "422", description = "Invalid movie data provided")
    })
    public ResponseEntity<MovieDTO> create(@RequestBody MovieDTO movieDTO) {
        var movie = movieService.create(movieDTO.toMovie());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(movie.getId())
                .toUri();
        return ResponseEntity.created(location).body(new MovieDTO(movie));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a movie", description = "Update the data of an existing movie based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie updated successfully"),
            @ApiResponse(responseCode = "404", description = "Movie not found"),
            @ApiResponse(responseCode = "422", description = "Invalid movie data provided")
    })
    public ResponseEntity<MovieDTO> update(@PathVariable Long id, @RequestBody MovieDTO movieDto) {
        var movie = movieService.update(id, movieDto.toMovie());
        return ResponseEntity.ok(new MovieDTO(movie));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a movie", description = "Delete an existing movie based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Movie deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Movie not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        movieService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
