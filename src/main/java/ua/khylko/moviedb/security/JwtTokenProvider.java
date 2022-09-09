package ua.khylko.moviedb.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${jwt_secret}")
    private String secret;

    public String generateToken(String username) {
        return JWT
                .create()
                .withSubject("USER DETAILS")
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withIssuer("MOVIEDB by Mykyta Khylko")
                .withExpiresAt(Date.from(ZonedDateTime.now().plusMinutes(1440).toInstant()))
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT
                                .require(Algorithm.HMAC256(secret))
                                .withSubject("USER DETAILS")
                                .withIssuer("MOVIEDB by Mykyta Khylko")
                                .build();
        DecodedJWT decoder = verifier.verify(token);
        return decoder.getClaim("username").asString();
    }
}
