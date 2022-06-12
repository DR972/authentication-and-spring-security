package com.epam.esm.config;

import com.epam.esm.exception.AccessDeniedHandlerEntryPoint;
import com.epam.esm.exception.AuthenticationHandlerEntryPoint;
import com.epam.esm.jwt.JWTFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

/**
 * Class {@code WebSecurityConfig} contains spring security configuration.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";
    /**
     * JWTFilter jwtFilter.
     */
    private final JWTFilter jwtFilter;
    /**
     * AuthenticationHandlerEntryPoint authentication Handler Entry Point.
     */
    private final AuthenticationHandlerEntryPoint authenticationHandlerEntryPoint;
    /**
     * AccessDeniedHandlerEntryPoint access Denied Handler Entry Point.
     */
    private final AccessDeniedHandlerEntryPoint accessDeniedHandlerEntryPoint;

    /**
     * The constructor creates a WebSecurityConfig object
     *
     * @param jwtFilter                       JWTFilter jwtFilter
     * @param authenticationHandlerEntryPoint AuthenticationHandlerEntryPoint authentication Handler Entry Point
     * @param accessDeniedHandlerEntryPoint   AccessDeniedHandlerEntryPoint access Denied Handler Entry Point
     */
    @Autowired
    public WebSecurityConfig(JWTFilter jwtFilter, AuthenticationHandlerEntryPoint authenticationHandlerEntryPoint,
                             AccessDeniedHandlerEntryPoint accessDeniedHandlerEntryPoint) {
        this.jwtFilter = jwtFilter;
        this.authenticationHandlerEntryPoint = authenticationHandlerEntryPoint;
        this.accessDeniedHandlerEntryPoint = accessDeniedHandlerEntryPoint;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers(GET, "/certificates/**", "/tags/**").permitAll()
                .antMatchers(POST, "/registration", "/authorization").permitAll()
                .antMatchers(POST, "/orders").fullyAuthenticated()
                .anyRequest().hasRole(ADMIN)
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandlerEntryPoint).authenticationEntryPoint(authenticationHandlerEntryPoint)
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
