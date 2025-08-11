package org.backend.chulfudoc.board.exceptions;

import org.backend.chulfudoc.global.exceptions.UnAuthorizedException;

public class GuestPasswordCheckException extends UnAuthorizedException {

    public GuestPasswordCheckException() {

        super("Required.guest.password");
        setErrorCode(true);

    }
}
