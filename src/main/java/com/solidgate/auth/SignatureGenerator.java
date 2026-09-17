package com.solidgate.auth;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class SignatureGenerator {

    private SignatureGenerator() {
    }

    public static String generate(String publicKey, String jsonBody, String secretKey) {
        String text = publicKey + jsonBody + publicKey;
        byte[] hashedBytes = Hashing.hmacSha512(secretKey.getBytes(StandardCharsets.UTF_8))
                .hashString(text, StandardCharsets.UTF_8)
                .toString()
                .getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(hashedBytes);
    }
}
