package com.itransition.payment.auth.service;

import com.itransition.payment.auth.dto.LoginRequest;
import com.itransition.payment.auth.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest);

    // TODO: method signature can be changed when the method will implemented
    void logout();
}
