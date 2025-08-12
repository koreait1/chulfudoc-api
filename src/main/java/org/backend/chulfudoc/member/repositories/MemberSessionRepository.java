package org.backend.chulfudoc.member.repositories;

import org.backend.chulfudoc.member.entities.MemberSession;
import org.springframework.data.repository.CrudRepository;

public interface MemberSessionRepository extends CrudRepository<MemberSession, String> {

}