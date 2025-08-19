package com.fintech.loanportal.service;

import com.fintech.loanportal.dto.RegisterRequest;
import com.fintech.loanportal.entity.User;

public interface UserService {
    User registerUser(RegisterRequest registerRequest);
    User getUserByEmail(String email);
    boolean existsByEmail(String email);
}
