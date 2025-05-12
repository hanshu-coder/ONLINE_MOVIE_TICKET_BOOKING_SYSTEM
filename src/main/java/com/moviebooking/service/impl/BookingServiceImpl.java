package com.moviebooking.service.impl;

import com.moviebooking.entity.*;
import com.moviebooking.repository.*;
import com.moviebooking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired 
    private MovieRepository movieRepo;
    
    @Autowired 
    private TheatreRepository theatreRepo;
    
    @Autowired 
    private SeatRepository seatRepo;
    
    @Autowired 
    private BookingRepository bookingRepo;
    
    @Autowired 
    private UserRepository userRepo;

    @Override
    public List<Movie> getAllMovies() {
        return movieRepo.findAll();
    }

    @Override
    public List<Theatre> getAllTheatres() {
        return theatreRepo.findAll();
    }

    @Override
    public List<Seat> getAvailableSeats(Long theatreId) {
        return seatRepo.findByTheatreId(theatreId);
    }

    @Override
    public String bookTicket(Long userId, Long movieId, Long theatreId, Long seatId) {
        User user = userRepo.findById(userId).orElse(null);
        Movie movie = movieRepo.findById(movieId).orElse(null);
        Theatre theatre = theatreRepo.findById(theatreId).orElse(null);
        Seat seat = seatRepo.findById(seatId).orElse(null);

        if (user == null || movie == null || theatre == null || seat == null || "BOOKED".equals(seat.getStatus())) {
            return "Invalid booking details or seat already booked!";
        }

        seat.setStatus("BOOKED");
        seatRepo.save(seat);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setMovie(movie);
        booking.setTheatre(theatre);
        booking.setSeat(seat);
        booking.setBookingTime(LocalDateTime.now());
        booking.setPaymentStatus("PAID"); // Simulate payment

        bookingRepo.save(booking);
        return "Booking Confirmed!";
    }
}
