package com.ludigi.oauth2_resource_server.security.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {
    private static final int EXP_TIME_SEC = 30 * 24 * 60 * 60;
    private final JWSAlgorithm jwsAlgorithm = JWSAlgorithm.HS256;
    private final JWSSigner signer;

    public JwtService(@Value("${jws.secretKey}") String sharedKey) {
        try {
            signer = new MACSigner(sharedKey.getBytes());
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public String createSignedJWT(String username, List<String> authorities) {
        JWSHeader header = new JWSHeader(jwsAlgorithm);
        LocalDateTime nowPlus1Hour = LocalDateTime.now().plusSeconds(EXP_TIME_SEC);
        Date expirationDate = Date.from(nowPlus1Hour.atZone(ZoneId.systemDefault()).toInstant());
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .expirationTime(expirationDate)
                .claim("scope", authorities)
                .build();
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        try {
            signedJWT.sign(signer);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return signedJWT.serialize();
    }

}