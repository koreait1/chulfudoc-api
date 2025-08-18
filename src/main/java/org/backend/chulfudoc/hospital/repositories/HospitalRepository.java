package org.backend.chulfudoc.hospital.repositories;

import org.backend.chulfudoc.hospital.entities.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
}
