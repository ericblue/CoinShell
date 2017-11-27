package io.coinshell.session.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtClaimsProvider {
    public Claims getClaims() {
        return Jwts.claims();
    }
}