package com.itransition.payment.core.service;

import com.itransition.payment.core.dto.AuthResponse;

public interface SecurityService {

    AuthResponse authorize();
}
