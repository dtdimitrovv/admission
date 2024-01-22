package com.example.demo.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

public record AdmissionRegistrationRequest(@NotBlank String username,
                                           @NotBlank String password,
                                           ProfileRegistrationRequest profileRegistrationRequest) {
}
// TODO - make profile registration mandatory
