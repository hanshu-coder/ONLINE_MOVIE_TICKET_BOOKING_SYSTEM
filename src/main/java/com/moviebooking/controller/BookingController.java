package com.moviebooking.controller;

import com.moviebooking.entity.*;
import com.moviebooking.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/booking")
@CrossOrigin
public class BookingController {

    @Autowired private MovieRepository movieRepo;
    @Autowired private TheatreRepository theatreRepo;
    @Autowired private SeatRepository seatRepo;
    @Autowired private BookingRepository bookingRepo;
    @Autowired private UserRepository userRepo;

    // ---------------------- MOVIE, THEATRE, SEAT APIs ----------------------

    @GetMapping("/movies")
    public List<Movie> getMovies() {
        return movieRepo.findAll();
    }

    @GetMapping("/theatres")
    public List<Theatre> getTheatres() {
        return theatreRepo.findAll();
    }

    @GetMapping("/seats/{theatreId}")
    public List<Seat> getSeats(@PathVariable Long theatreId) {
        return seatRepo.findByTheatreId(theatreId);
    }

    // ---------------------- BOOKING ENDPOINTS ----------------------

    private ResponseEntity<Map<String, String>> processBooking(Long userId, Long movieId, Long theatreId, Long seatId) {
        try {
            // Fetching entities from the database
            User user = userRepo.findById(userId).orElse(null);
            Movie movie = movieRepo.findById(movieId).orElse(null);
            Theatre theatre = theatreRepo.findById(theatreId).orElse(null);
            Seat seat = seatRepo.findById(seatId).orElse(null);

            // Initialize response map
            Map<String, String> response = new HashMap<>();

            // Check for invalid entities or already booked seat
            if (user == null) {
                response.put("message", "User not found.");
                return ResponseEntity.badRequest().body(response);
            }
            if (movie == null) {
                response.put("message", "Movie not found.");
                return ResponseEntity.badRequest().body(response);
            }
            if (theatre == null) {
                response.put("message", "Theatre not found.");
                return ResponseEntity.badRequest().body(response);
            }
            if (seat == null) {
                response.put("message", "Seat not found.");
                return ResponseEntity.badRequest().body(response);
            }
            if ("BOOKED".equalsIgnoreCase(seat.getStatus())) {
                response.put("message", "Seat already booked!");
                return ResponseEntity.badRequest().body(response);
            }

            // Mark seat as booked
            seat.setStatus("BOOKED");
            seatRepo.save(seat);

            // Create new booking
            Booking booking = new Booking();
            booking.setUser(user);
            booking.setMovie(movie);
            booking.setTheatre(theatre);
            booking.setSeat(seat);
            booking.setBookingTime(LocalDateTime.now());
            booking.setPaymentStatus("PAID");

            // Save booking to the database
            bookingRepo.save(booking);

            // Success response
            response.put("message", "Booking Confirmed!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Handle any unexpected errors and log them
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Booking failed: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    // Handle form-based booking request
    @PostMapping(value = "/confirm", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<Map<String, String>> bookTicketForm(
            @RequestParam Long userId,
            @RequestParam Long movieId,
            @RequestParam Long theatreId,
            @RequestParam Long seatId) {
        // Log the received data for debugging
        System.out.println("Received booking form data: userId=" + userId + ", movieId=" + movieId + ", theatreId=" + theatreId + ", seatId=" + seatId);
        return processBooking(userId, movieId, theatreId, seatId);
    }

    // Handle JSON-based booking request
    @PostMapping(value = "/confirm/json", consumes = "application/json")
    public ResponseEntity<Map<String, String>> bookTicketJson(@RequestBody BookingRequest bookingRequest) {
        // Log the received data for debugging
        System.out.println("Received booking JSON data: " + bookingRequest);
        return processBooking(
                bookingRequest.getUserId(),
                bookingRequest.getMovieId(),
                bookingRequest.getTheatreId(),
                bookingRequest.getSeatId());
    }

    // Save new user (not for booking, but for user management)
    @PostMapping("/booking/save")
    public ResponseEntity<String> saveUser(@RequestBody User user) {
        userRepo.save(user);
        return ResponseEntity.ok("User saved");
    }

    // ---------------------- LOGIN ENDPOINTS ----------------------

    @PostMapping(value = "/login/form", consumes = "application/x-www-form-urlencoded")
    public String loginForm(
            @RequestParam String email,
            @RequestParam String password) {
        return authenticateUser(email, password);
    }

    @PostMapping(value = "/login/json", consumes = "application/json")
    public String loginJson(@RequestBody User loginUser) {
        return authenticateUser(loginUser.getEmail(), loginUser.getPassword());
    }

    // Authenticate user
    private String authenticateUser(String email, String password) {
        User user = userRepo.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user.getRole().equalsIgnoreCase("ADMIN") ? "Welcome Admin" : "Welcome User";
        }
        return "Invalid credentials";
    }

    // ---------------------- DTO CLASS ----------------------

    public static class BookingRequest {
        private Long userId;
        private Long movieId;
        private Long theatreId;
        private Long seatId;

        public Long getUserId() {
            return userId;
        }
        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getMovieId() {
            return movieId;
        }
        public void setMovieId(Long movieId) {
            this.movieId = movieId;
        }

        public Long getTheatreId() {
            return theatreId;
        }
        public void setTheatreId(Long theatreId) {
            this.theatreId = theatreId;
        }

        public Long getSeatId() {
            return seatId;
        }
        public void setSeatId(Long seatId) {
            this.seatId = seatId;
        }

        @Override
        public String toString() {
            return "BookingRequest{" +
                    "userId=" + userId +
                    ", movieId=" + movieId +
                    ", theatreId=" + theatreId +
                    ", seatId=" + seatId +
                    '}';
        }
    }
}
