package com.example.demo.controller;

import com.example.demo.entity.Admission;
import com.example.demo.payload.request.AdmissionDetailsModificationRequest;
import com.example.demo.payload.request.AdmissionRegistrationRequest;
import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.NewPasswordRequest;
import com.example.demo.payload.response.AdmissionRegistrationResponse;
import com.example.demo.payload.response.LoginResponse;
import com.example.demo.service.AdmissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class AdmissionController {

    private final AdmissionService admissionService;

    public AdmissionController(AdmissionService admissionService) {
        this.admissionService = admissionService;
    }

    @PostMapping("/trainee-registration")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public AdmissionRegistrationResponse registerTrainee(@RequestBody @Valid AdmissionRegistrationRequest request) {
        return this.admissionService.registerTrainee(request);
    }

    @PostMapping("/trainer-registration")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public AdmissionRegistrationResponse registerTrainer(@RequestBody @Valid AdmissionRegistrationRequest request) {
        return this.admissionService.registerTrainer(request);
    }

    @PatchMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        return this.admissionService.login(request);
    }

    @PatchMapping("/password-modification")
    @PreAuthorize("isAuthenticated()")
    public void changePassword(@AuthenticationPrincipal Admission admission,
                               @RequestBody @Valid NewPasswordRequest newPasswordRequest) {
        this.admissionService.changePassword(admission, newPasswordRequest);
    }

    @PatchMapping("/password-modification/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void changePassword(@PathVariable Long id,
                               @RequestBody @Valid NewPasswordRequest newPasswordRequest) {
        this.admissionService.changePassword(id, newPasswordRequest);
    }

    @PutMapping("/details-modification/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void modifyDetails(@RequestBody AdmissionDetailsModificationRequest admissionDetailsModificationRequest,
                              @PathVariable Long id) {
        this.admissionService.modifyDetails(admissionDetailsModificationRequest, id);
    }
}
