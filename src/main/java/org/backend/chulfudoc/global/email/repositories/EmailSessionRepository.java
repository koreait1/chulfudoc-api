package org.backend.chulfudoc.global.email.repositories;

import org.backend.chulfudoc.global.email.entities.EmailSession;
import org.springframework.data.repository.CrudRepository;

public interface EmailSessionRepository extends CrudRepository<EmailSession, String> {
}
