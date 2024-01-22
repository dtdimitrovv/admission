package com.example.demo.payload.request;

import javax.validation.constraints.NotBlank;

public record NewPasswordRequest(@NotBlank String password) {
}
