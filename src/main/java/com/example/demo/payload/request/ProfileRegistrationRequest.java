package com.example.demo.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record ProfileRegistrationRequest (@NotBlank String firstName,
                                          @NotBlank String lastName,
                                          String middleName,
                                          @Email @NotNull String email,
                                           AddressRegistrationRequest addressRegistrationRequest) {
}
// TODO - make addressRegistrationRequest not null
