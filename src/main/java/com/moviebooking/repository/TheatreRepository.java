package com.moviebooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moviebooking.entity.Theatre;

public interface TheatreRepository extends JpaRepository<Theatre, Long> {
}