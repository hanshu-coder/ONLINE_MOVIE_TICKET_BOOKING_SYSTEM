package com.moviebooking.controller;

import com.moviebooking.entity.User;
import com.moviebooking.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booking") // Match this with frontend URL
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    // 🔐 Login endpoint for Admin and Users
    @PostMapping(value = "/login", consumes = "application/json")
    public Map<String, String> login(@RequestBody User loginUser) {
        Map<String, String> response = new HashMap<>();

        // Log login attempt (for debugging)
        System.out.println("Login attempt with email: " + loginUser.getEmail());

        // ✅ Check hardcoded Admin credentials
        if ("admin@example.com".equalsIgnoreCase(loginUser.getEmail()) &&
            "admin123".equals(loginUser.getPassword()) &&
            "admin".equalsIgnoreCase(loginUser.getRole())) {

            response.put("status", "success");
            response.put("role", "admin");
            response.put("message", "Welcome Admin");
            return response;
        }

        // 🔍 Find user from the database
        User user = userRepo.findByEmail(loginUser.getEmail());

        if (user != null) {
            // Check if password matches
            if (user.getPassword().equals(loginUser.getPassword())) {
                // Check if role matches
                if (loginUser.getRole().equalsIgnoreCase(user.getRole())) {
                    response.put("status", "success");
                    response.put("role", user.getRole().equalsIgnoreCase("ADMIN") ? "admin" : "user");
                    response.put("message", "Welcome " + user.getRole());
                } else {
                    response.put("status", "error");
                    response.put("message", "Invalid credentials or incorrect role");
                }
            } else {
                response.put("status", "error");
                response.put("message", "Invalid credentials or incorrect role");
            }
        } else {
            response.put("status", "error");
            response.put("message", "User not found with email: " + loginUser.getEmail());
        }

        return response;
    }
}
