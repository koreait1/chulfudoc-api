package org.backend.chulfudoc.member.exceptions;

import org.backend.chulfudoc.global.exceptions.NotFoundException;

public class MemberNotFoundException extends NotFoundException {
    public MemberNotFoundException() {

        super("NotFound.member");
        setErrorCode(true);
    }

}