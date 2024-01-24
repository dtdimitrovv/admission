package com.example.demo.repository;

import com.example.authorizationValidator.repository.AuthenticationEntityRepository;
import com.example.demo.entity.Admission;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AdmissionRepository extends AuthenticationEntityRepository<Admission, Long> {
    Optional<Admission> findByUsername(String username);

    @Query("""
            SELECT admission FROM Admission admission
            JOIN FETCH admission.roles
            WHERE admission.id = :id
            """)
    Optional<Admission> findById(Long id);
}
