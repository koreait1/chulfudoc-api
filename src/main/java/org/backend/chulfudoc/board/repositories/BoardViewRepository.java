package org.backend.chulfudoc.board.repositories;

import org.backend.chulfudoc.board.entities.BoardView;
import org.backend.chulfudoc.board.entities.BoardViewId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BoardViewRepository extends JpaRepository<BoardView, BoardViewId>, QuerydslPredicateExecutor<BoardView> {

}