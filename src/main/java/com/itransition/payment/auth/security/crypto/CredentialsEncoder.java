package com.itransition.payment.auth.security.crypto;

public interface CredentialsEncoder {

    String encode(String plainString);
}
