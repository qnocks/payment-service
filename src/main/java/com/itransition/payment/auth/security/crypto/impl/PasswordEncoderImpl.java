package com.itransition.payment.auth.security.crypto.impl;

import com.itransition.payment.auth.security.crypto.CredentialsEncoder;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncoderImpl implements PasswordEncoder {

    private static final String SHA_PREFIX = "{SHA}";
    private final CredentialsEncoder credentialsEncoder;

    @Override
    public String encode(CharSequence rawPassword) {
        return SHA_PREFIX + credentialsEncoder.encode(String.valueOf(rawPassword));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        val password = credentialsEncoder.encode(String.valueOf(rawPassword));
        return password.equals(encodedPassword);
    }
}
