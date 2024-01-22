package com.example.demo.repository;

import com.example.demo.entity.Admission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AdmissionRepository extends JpaRepository<Admission, Long> {
    Optional<Admission> findByUsername(String username);

    @Query("""
            SELECT admission FROM Admission admission
            JOIN FETCH admission.roles
            WHERE admission.id = :id
            """)
    Optional<Admission> findById(Long id);
}
