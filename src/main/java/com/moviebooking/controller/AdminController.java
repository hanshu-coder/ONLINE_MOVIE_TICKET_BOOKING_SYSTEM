package com.moviebooking.controller;

import com.moviebooking.entity.Movie;
import com.moviebooking.entity.Theatre;
import com.moviebooking.entity.User;
import com.moviebooking.repository.MovieRepository;
import com.moviebooking.repository.TheatreRepository;
import com.moviebooking.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:5500") // Allow frontend access on this port
public class AdminController {

    @Autowired
    private MovieRepository movieRepo;

    @Autowired
    private TheatreRepository theatreRepo;

    @Autowired
    private UserRepository userRepo;

    // 🔐 Admin and User Login
    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody User user) {
        // Admin credentials check
        if ("admin@example.com".equals(user.getEmail()) && "admin123".equals(user.getPassword())) {
            return ResponseEntity.ok("Welcome, Admin!");
        }

        // Normal user credentials check
        User existingUser = userRepo.findByEmail(user.getEmail());
        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
            return ResponseEntity.ok("Welcome, User!");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    // 🎬 Add a new movie
    @PostMapping(path = "/addMovie", consumes = "application/json")
    public ResponseEntity<String> addMovie(@RequestBody Movie movie) {
        try {
            movieRepo.save(movie);
            return ResponseEntity.status(HttpStatus.CREATED).body("Movie added successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    // 🎭 Add a new theatre
    @PostMapping(path = "/addTheatre", consumes = "application/json")
    public ResponseEntity<String> addTheatre(@RequestBody Theatre theatre) {
        try {
            theatreRepo.save(theatre);
            return ResponseEntity.status(HttpStatus.CREATED).body("Theatre added successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    // 📽️ View all movies
    @GetMapping("/view-movies")
    public ResponseEntity<List<Movie>> viewMovies() {
        List<Movie> movies = movieRepo.findAll();
        return ResponseEntity.ok(movies);
    }

    // 🏛️ View all theatres
    @GetMapping("/view-theatres")
    public ResponseEntity<List<Theatre>> viewTheatres() {
        List<Theatre> theatres = theatreRepo.findAll();
        return ResponseEntity.ok(theatres);
    }
}
