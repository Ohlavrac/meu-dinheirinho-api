package com.ohlavrac.meu_dinheirinho_api.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.ohlavrac.meu_dinheirinho_api.domain.entities.users.UserDetailsImpl;

@Service
public class TokenService {
    @Value("${api.security.token.key}")
    private String SECRET;

    private String ISSUER;
    
    public String generateToken(UserDetailsImpl user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            String token = JWT.create()
                            .withIssuer(ISSUER)
                            .withSubject(user.getUsername())
                            .withExpiresAt(generateExpirationDate())
                            .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new JWTCreationException("Authentication Error", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);

            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTDecodeException exception) {
            return null;
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
