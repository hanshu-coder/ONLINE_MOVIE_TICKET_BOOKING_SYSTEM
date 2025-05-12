package com.moviebooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moviebooking.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
   User findByEmail(String email);
}