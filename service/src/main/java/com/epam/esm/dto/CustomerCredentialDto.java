package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The class {@code CustomerCredentialDto} represents user credentials.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCredentialDto {
    /**
     * String email.
     */
    private String email;
    /**
     * String password.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    /**
     * String token.
     */
    private String token;
}
