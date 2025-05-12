package com.moviebooking.service.impl;

import com.moviebooking.entity.User;
import com.moviebooking.repository.UserRepository;
import com.moviebooking.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public String registerUser(User user) {
        user.setRole("USER");
        userRepo.save(user);
        return "User registered successfully!";
    }

    @Override
    public String loginUser(User loginUser) {
        User user = userRepo.findByEmail(loginUser.getEmail());
        if (user != null && user.getPassword().equals(loginUser.getPassword())) {
            return user.getRole().equals("ADMIN") ? "admin" : "user";
        }
        return "Invalid credentials";
    }
}
