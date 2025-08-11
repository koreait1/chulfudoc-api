package org.backend.chulfudoc.board.services.configs;

import lombok.RequiredArgsConstructor;
import org.backend.chulfudoc.board.controllers.RequestBoardConfig;
import org.backend.chulfudoc.board.entities.Board;
import org.backend.chulfudoc.board.repositories.BoardRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
@RequiredArgsConstructor
public class BoardConfigUpdateService {

    private final ModelMapper mapper;
    private final BoardRepository boardRepository;

    public void process(RequestBoardConfig form) {

        Board item = mapper.map(form, Board.class);
        boardRepository.saveAndFlush(item);
    }
}
