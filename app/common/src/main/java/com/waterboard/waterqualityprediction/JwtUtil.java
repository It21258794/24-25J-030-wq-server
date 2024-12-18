package com.waterboard.waterqualityprediction;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Payload;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtUtil {

    public static String encode(JWTContent jwtContent, String jwtKey) {
        JWTCreator.Builder builder = com.auth0.jwt.JWT.create();
        if (jwtContent.getPayload() != null) {
            for (Map.Entry<String, String> entry : jwtContent.getPayload().entrySet()) {
                builder = builder.withClaim(entry.getKey(), entry.getValue());
            }
        }
        String token = builder.withSubject(jwtContent.getMainSubject())
                .withExpiresAt(DateUtils.addMilliseconds(DateTimeUtils.nowAsDate(), (int) jwtContent.getExpiredIn()))
                .sign(Algorithm.HMAC256(jwtKey));
        return token;
    }

    public static String encodeWithExpiration(JWTContent jwtContent, String jwtKey) {
        JWTCreator.Builder builder = com.auth0.jwt.JWT.create();
        if (jwtContent.getPayload() != null) {
            for (Map.Entry<String, String> entry : jwtContent.getPayload().entrySet()) {
                builder = builder.withClaim(entry.getKey(), entry.getValue());
            }
        }
        Instant instant = Instant.now().plusMillis((long) jwtContent.getExpiredIn());
        String token = builder.withSubject(jwtContent.getMainSubject())
                .withExpiresAt(Date.from(instant))
                .sign(Algorithm.HMAC256(jwtKey));
        return token;
    }

    public static JWTContent decode(String token, String jwtKey) {
        try {
            JWTContent jwtContent = JWTContent.builder().build();
            Algorithm algorithm = Algorithm.HMAC256(jwtKey);
            JWTVerifier verifier = com.auth0.jwt.JWT.require(algorithm).build();
            DecodedJWT content = verifier.verify(token);
            jwtContent.setMainSubject(content.getSubject());
            Map<String, String> claims = new HashMap<>();
            for (Map.Entry<String, Claim> entry : content.getClaims().entrySet()) {
                claims.put(entry.getKey(), entry.getValue().asString());
            }
            jwtContent.setPayload(claims);
            return jwtContent;
        } catch (JWTVerificationException exp) {
            log.error("cannot decode jwt token, error={}", exp.getMessage());
            throw exp;
        }
    }

    public static JWTContent decodeWithoutSecret(String token) {
        try {
            JWTContent jwtContent = JWTContent.builder().build();
            Payload payload = com.auth0.jwt.JWT.decode(token);
            jwtContent.setMainSubject(payload.getSubject());
            Map<String, String> claims = new HashMap<>();
            for (Map.Entry<String, Claim> entry : payload.getClaims().entrySet()) {
                claims.put(entry.getKey(), entry.getValue().asString());
            }
            jwtContent.setPayload(claims);
            return jwtContent;
        } catch (JWTVerificationException exp) {
            log.error("cannot decode jwt token, error={}", exp.getMessage());
            return JWTContent.builder().build();
        }
    }

}
