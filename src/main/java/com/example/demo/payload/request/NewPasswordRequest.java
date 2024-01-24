package com.example.demo.payload.request;

import jakarta.validation.constraints.NotBlank;

public record NewPasswordRequest(@NotBlank String password) {
}
