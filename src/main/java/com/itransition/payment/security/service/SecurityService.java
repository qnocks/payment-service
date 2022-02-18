package com.itransition.payment.security.service;

import com.itransition.payment.security.dto.AuthResponse;

public interface SecurityService {

    AuthResponse authorize();
}
