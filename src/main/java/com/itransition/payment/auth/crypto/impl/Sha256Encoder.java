package com.itransition.payment.auth.crypto.impl;

import com.itransition.payment.auth.crypto.Encoder;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class Sha256Encoder implements Encoder {

    @Override
    public String encode(String plainString) {
       return DigestUtils.sha256Hex(plainString);
    }
}
