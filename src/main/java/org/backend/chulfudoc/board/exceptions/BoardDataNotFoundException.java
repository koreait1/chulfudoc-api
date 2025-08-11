package org.backend.chulfudoc.board.exceptions;

import org.backend.chulfudoc.global.exceptions.BadRequestException;

public class BoardDataNotFoundException extends BadRequestException {

    public BoardDataNotFoundException() {
        super("NotFound.boardData");
        setErrorCode(true);
    }

}