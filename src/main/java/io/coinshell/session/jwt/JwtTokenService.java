package io.coinshell.session.jwt;

import io.coinshell.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JwtTokenService {

    private final String secretKey = "9cQDrP*<F!}X_}pZEJ:<?KewL>s[R2?U";
    private final JwtParserProvider.JwtBuilderProvider jwtBuilderProvider;
    private final JwtParserProvider jwtParserProvider;
    private final JwtClaimsProvider jwtClaimsProvider;
    private final NowProvider nowProvider;

    @Autowired
    public JwtTokenService(
            JwtParserProvider.JwtBuilderProvider jwtBuilderProvider,
            JwtParserProvider jwtParserProvider,
            JwtClaimsProvider jwtClaimsProvider,
            NowProvider nowProvider
    ) {
        this.jwtBuilderProvider = jwtBuilderProvider;
        this.jwtParserProvider = jwtParserProvider;
        this.jwtClaimsProvider = jwtClaimsProvider;
        this.nowProvider = nowProvider;
    }

    public String getTokenForUser(User user) {
        Claims claims = jwtClaimsProvider.getClaims();
        claims.setSubject(user.getId().toString());
        claims.setIssuedAt(nowProvider.getNow());
        return jwtBuilderProvider.getJwtBuilder().setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Optional<String> getUserIdFromToken(String token) {
        return Optional.ofNullable(
                jwtParserProvider.getJwtParser()
                        .setSigningKey(secretKey)
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject()
        );
    }
}