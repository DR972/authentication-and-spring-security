package com.epam.esm.jwt;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static io.jsonwebtoken.lang.Strings.hasText;

/**
 * Filter {@code JWTFilter} used for token validation.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 * @see OncePerRequestFilter
 */
@Component
public class JWTFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    /**
     * JWTProvider jwtProvider.
     */
    private final JWTProvider jwtProvider;
    /**
     * UserDetailsService customerDetailsService.
     */
    private final UserDetailsService customerDetailsService;

    /**
     * The constructor creates a JWTFilter object
     *
     * @param jwtProvider            JWTProvider jwtProvider
     * @param customerDetailsService UserDetailsService customerDetailsService
     */
    @Autowired
    public JWTFilter(JWTProvider jwtProvider, UserDetailsService customerDetailsService) {
        this.jwtProvider = jwtProvider;
        this.customerDetailsService = customerDetailsService;
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) {
        String token = getTokenFromRequest(request);
        if (token != null && jwtProvider.validateToken(token)) {
            String customerLogin = jwtProvider.getLoginFromToken(token);
            UserDetails customerDetails = customerDetailsService.loadUserByUsername(customerLogin);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(customerDetails, null, customerDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        return hasText(bearer) && bearer.startsWith(BEARER) ? bearer.substring(7) : null;
    }
}
