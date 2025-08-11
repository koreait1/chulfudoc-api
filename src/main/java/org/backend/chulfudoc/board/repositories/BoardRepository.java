package org.backend.chulfudoc.board.repositories;

import org.backend.chulfudoc.board.entities.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BoardRepository extends JpaRepository<Board, String>, QuerydslPredicateExecutor<Board> {

    boolean existsByBid(String bid);

}
