package com.epam.esm.service.impl;

import com.epam.esm.dao.CustomerDao;
import com.epam.esm.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

/**
 * Class {@code CustomerDetailsServiceImpl} is implementation of interface {@link UserDetailsService}
 * and intended to work with {@link UserDetails} objects necessary for spring security.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@Service
@Transactional
public class CustomerDetailsServiceImpl implements UserDetailsService {
    private static final String ROLE_PREFIX = "ROLE_";
    /**
     * CustomerDao customerDao.
     */
    private final CustomerDao customerDao;

    /**
     * The constructor creates a CustomerDetailsServiceImpl object
     *
     * @param customerDao CustomerDao customerDao
     */
    @Autowired
    public CustomerDetailsServiceImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<Customer> user = customerDao.findCustomerByEmail(email);
        if (!user.isPresent()) {
            throw new BadCredentialsException("ex.badUser");
        }
        return User.builder()
                .username(user.get().getEmail())
                .password(user.get().getPassword())
                .roles(user.get().getRole().name())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(ROLE_PREFIX + user.get().getRole().toString())))
                .build();
    }
}
