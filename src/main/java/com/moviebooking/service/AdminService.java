package com.moviebooking.service;

import com.moviebooking.entity.Movie;
import com.moviebooking.entity.Theatre;

import java.util.List;

public interface AdminService {
    String addMovie(Movie movie);
    String addTheatre(Theatre theatre);
    List<Movie> getAllMovies();
    List<Theatre> getAllTheatres();
}
