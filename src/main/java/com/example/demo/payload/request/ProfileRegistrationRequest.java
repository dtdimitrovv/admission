package com.example.demo.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProfileRegistrationRequest(@NotBlank String firstName,
                                         @NotBlank String lastName,
                                         String middleName,
                                         @Email @NotNull String email,
                                         AddressRegistrationRequest addressRegistrationRequest) {
}