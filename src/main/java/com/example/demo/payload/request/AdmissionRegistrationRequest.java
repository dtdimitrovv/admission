package com.example.demo.payload.request;

import jakarta.validation.constraints.NotBlank;

public record AdmissionRegistrationRequest(@NotBlank String username,
                                           @NotBlank String password,
                                           ProfileRegistrationRequest profileRegistrationRequest) {
}