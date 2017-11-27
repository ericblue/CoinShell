package io.coinshell.authentication;

import io.coinshell.session.jwt.JwtClaimsProvider;
import io.coinshell.session.jwt.JwtParserProvider;
import io.coinshell.session.jwt.JwtTokenService;
import io.coinshell.session.jwt.NowProvider;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;


public class JwtAuthenticationFilter extends GenericFilterBean {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private JwtTokenService jwtTokenService;

    public JwtAuthenticationFilter() {
        super();
        this.jwtTokenService = new JwtTokenService(
                new JwtParserProvider.JwtBuilderProvider(),
                new JwtParserProvider(),
                new JwtClaimsProvider(),
                new NowProvider()
        );
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain filterChain
    )
            throws IOException, ServletException {


        logger.debug("Called JwtAuthenticationFilter.doFilter");

        val token = ((HttpServletRequest) request).getHeader("Authorization");
        if (token != null) {
            val cleanedToken = token.replace("Bearer ", "");

            logger.debug("Cleaned token = " + cleanedToken);

            val userId = jwtTokenService.getUserIdFromToken(cleanedToken);

            logger.debug("User ID = " + userId);

            Authentication authentication = null;
            if (userId.isPresent()) {
                authentication = new UsernamePasswordAuthenticationToken(
                        userId.get(),
                        null, Collections.emptyList());
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            logger.debug("No Authorization header present, skipping... ");
            SecurityContextHolder.getContext().setAuthentication(null);

        }

        filterChain.doFilter(request, response);
    }
}

