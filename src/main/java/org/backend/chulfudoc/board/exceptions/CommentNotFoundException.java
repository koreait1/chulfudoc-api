package org.backend.chulfudoc.board.exceptions;

import org.backend.chulfudoc.global.exceptions.BadRequestException;

public class CommentNotFoundException extends BadRequestException {

    public CommentNotFoundException() {

        super("NotFound.comment");
        setErrorCode(true);

    }
}
