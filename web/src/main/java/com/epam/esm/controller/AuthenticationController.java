package com.epam.esm.controller;

import com.epam.esm.dto.CustomerCredentialDto;
import com.epam.esm.dto.CustomerDto;
import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.jwt.JWTProvider;
import com.epam.esm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Class {@code AuthenticationController} is an API endpoint that allows you to perform operations for authorization.
 * Annotated by {@link RestController} with no parameters to provide an answer in application/json.
 *
 * @author Dzmitry Rozmysl
 * @since 1.0
 */
@RestController
public class AuthenticationController {
    /**
     * JWTProvider jwtProvider.
     */
    private final JWTProvider jwtProvider;
    /**
     * AuthenticationManager authentication Manager.
     */
    private final AuthenticationManager authenticationManager;
    /**
     * HateoasAdder<CustomerDto> customerHateoasAdder.
     */
    private final HateoasAdder<CustomerDto> hateoasAdder;
    /**
     * CustomerService customerService.
     */
    private final CustomerService customerService;

    /**
     * The constructor creates a AuthenticationController object
     *
     * @param customerService       CustomerService customerService
     * @param hateoasAdder          HateoasAdder<CustomerDto> hateoasAdder
     * @param jwtProvider           JWTProvider jwtProvider
     * @param authenticationManager AuthenticationManager authentication Manager
     */
    @Autowired
    public AuthenticationController(CustomerService customerService, HateoasAdder<CustomerDto> hateoasAdder, JWTProvider jwtProvider, AuthenticationManager authenticationManager) {
        this.customerService = customerService;
        this.hateoasAdder = hateoasAdder;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Method for registration new Customer.
     * Annotated by {@link Valid} provides validation of the fields of the CustomerDto object when registration.
     *
     * @param customer CustomerDto
     * @return created CustomerDto
     */
    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDto registerCustomer(@Valid @RequestBody CustomerDto customer) {
        CustomerDto customerDto = customerService.createCustomer(customer);
        hateoasAdder.addLinks(customerDto);
        return customerDto;
    }

    /**
     * Method for authorizing an existing Customer.
     * Annotated by {@link Valid} provides validation of the fields of the CustomerCredentialDto object when authorization.
     *
     * @param customerCredentialDto user credentials
     * @return user credentials with unique token
     */
    @PostMapping("/authorization")
    @ResponseStatus(HttpStatus.OK)
    public CustomerCredentialDto authorizeCustomer(CustomerCredentialDto customerCredentialDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(customerCredentialDto.getEmail(), customerCredentialDto.getPassword()));
        customerCredentialDto.setToken(jwtProvider.generateToken(customerCredentialDto.getEmail()));
        return customerCredentialDto;
    }
}
