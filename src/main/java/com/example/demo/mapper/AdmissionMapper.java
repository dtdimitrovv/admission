package com.example.demo.mapper;

import com.example.demo.entity.Admission;
import com.example.demo.entity.Role;
import com.example.demo.payload.request.AdmissionDetailsModificationRequest;
import com.example.demo.payload.request.AdmissionRegistrationRequest;
import com.example.demo.payload.response.AdmissionRegistrationResponse;
import com.example.demo.payload.response.LoginResponse;
import org.apache.kafka.common.quota.ClientQuotaAlteration;
import org.mapstruct.*;

import java.util.Optional;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AdmissionMapper {
    Admission map(AdmissionRegistrationRequest request, Set<Role> roles);

    LoginResponse map(String token);

    AdmissionRegistrationResponse map(Admission admission);

    @Mapping(target = "roles",
            expression = "java(handleRoles(admission, admissionDetailsModificationRequest.rolesToAdd(), " +
                    "admissionDetailsModificationRequest.rolesToRemove()))")
    Admission map(AdmissionDetailsModificationRequest admissionDetailsModificationRequest,
                  @MappingTarget Admission admission);

    default Set<Role> handleRoles(Admission admission, Set<Role> rolesToAdd, Set<Role> rolesToRemove) {
        Optional.ofNullable(rolesToRemove)
                .ifPresent(roles -> admission.getRoles().removeAll(roles));

        Optional.ofNullable(rolesToAdd)
                .ifPresent(roles -> admission.getRoles().addAll(roles));

        return admission.getRoles();
    }
}
