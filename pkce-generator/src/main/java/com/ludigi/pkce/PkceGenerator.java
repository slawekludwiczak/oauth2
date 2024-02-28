package com.ludigi.pkce;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Create random string
 * Generate SHA-256 hash
 * Encode with Base64URL
 */
class PkceGenerator {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String source = "Th7UHJdLswIYQxwSg29DbK1a_d9o41uNMTRmuH0PM8zyoMAQ";
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] sha256 = digest.digest(
                source.getBytes(StandardCharsets.UTF_8));
        byte[] base64Bytes = Base64.getUrlEncoder().encode(sha256);
        String hash = new String(base64Bytes);
        System.out.println(hash);
    }
}

//plain Th7UHJdLswIYQxwSg29DbK1a_d9o41uNMTRmuH0PM8zyoMAQ
//hash hKpKupTM391pE10xfQiorMxXarRKAHRhTfH_xkGf7U4
