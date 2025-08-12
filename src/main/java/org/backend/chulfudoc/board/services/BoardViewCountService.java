package org.backend.chulfudoc.board.services;

import lombok.RequiredArgsConstructor;
import org.backend.chulfudoc.board.entities.BoardData;
import org.backend.chulfudoc.board.entities.BoardView;
import org.backend.chulfudoc.board.entities.QBoardView;
import org.backend.chulfudoc.board.repositories.BoardDataRepository;
import org.backend.chulfudoc.board.repositories.BoardViewRepository;
import org.backend.chulfudoc.member.libs.MemberUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
@RequiredArgsConstructor
public class BoardViewCountService {
    private final BoardDataRepository boardDataRepository;
    private final BoardViewRepository boardViewRepository;
    private final MemberUtil memberUtil;

    public void update(Long seq) {
        try {
            int hash = memberUtil.getUserHash();
            BoardView item = new BoardView();
            item.setHash(hash);
            item.setSeq(seq);
            boardViewRepository.saveAndFlush(item);
            QBoardView boardView = QBoardView.boardView;

            int viewCount = (int)boardViewRepository.count(boardView.seq.eq(seq));
            BoardData boardData = boardDataRepository.findById(seq).orElse(null);
            if (boardData != null) {
                boardData.setViewCount(viewCount);
                boardDataRepository.saveAndFlush(boardData);
            }

        } catch (Exception e) {}
    }
}
