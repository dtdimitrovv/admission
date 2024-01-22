package com.example.demo.payload.request;

import com.example.demo.entity.Role;

import java.util.Set;

public record AdmissionDetailsModificationRequest(Set<Role> rolesToAdd,
                                                  Set<Role> rolesToRemove) {
}
