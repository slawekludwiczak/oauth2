package com.ludigi.oauth2_resource_server.security.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {
    private static final int EXP_TIME_SEC = 30 * 24 * 60 * 60;
    private final JWSAlgorithm jwsAlgorithm = JWSAlgorithm.RS256;
    private final JWSSigner signer;

    public JwtService(@Value("classpath:rs256.key") Resource privateKeyFile) {
        try {
            String privateKeyContent = privateKeyFile.getContentAsString(StandardCharsets.UTF_8);
            privateKeyContent = privateKeyContent.replaceAll("\\n", "").replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "");
            PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = kf.generatePrivate(keySpecPKCS8);
            signer = new RSASSASigner(privateKey);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
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