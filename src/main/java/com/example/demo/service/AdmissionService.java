package com.example.demo.service;

import com.example.demo.entity.Admission;
import com.example.demo.payload.request.AdmissionDetailsModificationRequest;
import com.example.demo.payload.request.AdmissionRegistrationRequest;
import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.NewPasswordRequest;
import com.example.demo.payload.response.AdmissionRegistrationResponse;
import com.example.demo.payload.response.LoginResponse;

public interface AdmissionService {
    AdmissionRegistrationResponse registerTrainee(AdmissionRegistrationRequest request);

    AdmissionRegistrationResponse registerTrainer(AdmissionRegistrationRequest request);

    LoginResponse login(LoginRequest loginRequest);

    void changePassword(Admission admission, NewPasswordRequest newPasswordRequest);

    void changePassword(Long id, NewPasswordRequest newPasswordRequest);

    void modifyDetails(AdmissionDetailsModificationRequest admissionDetailsModificationRequest, Long id);
}
