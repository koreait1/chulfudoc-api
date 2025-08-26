package org.backend.chulfudoc.board.services.configs;

import lombok.RequiredArgsConstructor;
import org.backend.chulfudoc.board.entities.Board;
import org.backend.chulfudoc.board.exceptions.BoardNotFoundException;
import org.backend.chulfudoc.board.repositories.BoardDataRepository;
import org.backend.chulfudoc.board.repositories.BoardRepository;
import org.backend.chulfudoc.member.libs.MemberUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BoardConfigDeleteService {

    private final BoardRepository boardRepository;
    private final BoardDataRepository boardDataRepository;
    private final MemberUtil memberUtil;

    /**
     * 게시판(보드) 소프트 삭제
     * @param bid 보드ID(=PK)
     * @param softDelete true면 해당 보드의 모든 게시글도 소프트 삭제
     */
    @Transactional
    public void softDeleteBoard(String bid, boolean softDelete) {
        Board board = boardRepository.findById(bid).orElseThrow(BoardNotFoundException::new);

        // 관리자만 허용
        if (!memberUtil.isAdmin()) {
            throw new org.backend.chulfudoc.global.exceptions.UnAuthorizedException("삭제 권한이 없습니다.");
        }

        // 소프트 삭제: 사용중지 + 삭제시각 기록
        board.setActive(false);
        board.setDeletedAt(LocalDateTime.now());
        boardRepository.save(board);
    }
}
