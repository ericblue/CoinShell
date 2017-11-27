package io.coinshell.session.jwt;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtParserProvider {
    public JwtParser getJwtParser() {
        return Jwts.parser();
    }

    @Component
    public static class JwtBuilderProvider {
        public JwtBuilder getJwtBuilder() {
            return Jwts.builder();
        }
    }
}