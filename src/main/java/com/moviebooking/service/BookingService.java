package com.moviebooking.service;

import com.moviebooking.entity.Movie;
import com.moviebooking.entity.Seat;
import com.moviebooking.entity.Theatre;

import java.util.List;

public interface BookingService {
    List<Movie> getAllMovies();
    List<Theatre> getAllTheatres();
    List<Seat> getAvailableSeats(Long theatreId);
    String bookTicket(Long userId, Long movieId, Long theatreId, Long seatId);
}
