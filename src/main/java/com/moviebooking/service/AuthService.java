
package com.moviebooking.service;

import com.moviebooking.entity.User;

public interface AuthService {
    String registerUser(User user);
    String loginUser(User loginUser);
}
