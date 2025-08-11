package org.backend.chulfudoc.board.exceptions;

import org.backend.chulfudoc.global.exceptions.NotFoundException;

public class BoardNotFoundException extends NotFoundException {

    public BoardNotFoundException() {

        super("NotFound.board");
        setErrorCode(true);
    }

}
