package com.moviebooking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moviebooking.entity.Seat;

public interface SeatRepository extends JpaRepository<Seat, Long> {
     List<Seat> findByTheatreId(Long theatreId);
}