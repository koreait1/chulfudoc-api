package org.backend.chulfudoc.aed.repositories;

import org.backend.chulfudoc.aed.entities.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
}
