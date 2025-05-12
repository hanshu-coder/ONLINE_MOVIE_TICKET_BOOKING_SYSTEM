package com.moviebooking.service.impl;

import com.moviebooking.entity.Movie;
import com.moviebooking.entity.Theatre;
import com.moviebooking.repository.MovieRepository;
import com.moviebooking.repository.TheatreRepository;
import com.moviebooking.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private MovieRepository movieRepo;

    @Autowired
    private TheatreRepository theatreRepo;

    @Override
    public String addMovie(Movie movie) {
        movieRepo.save(movie);
        return "Movie added successfully!";
    }

    @Override
    public String addTheatre(Theatre theatre) {
        theatreRepo.save(theatre);
        return "Theatre added successfully!";
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepo.findAll();
    }

    @Override
    public List<Theatre> getAllTheatres() {
        return theatreRepo.findAll();
    }
}
