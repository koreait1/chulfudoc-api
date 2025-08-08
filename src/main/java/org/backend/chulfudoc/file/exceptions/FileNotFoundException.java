package org.backend.chulfudoc.file.exceptions;

import org.backend.chulfudoc.global.exceptions.NotFoundException;

public class FileNotFoundException extends NotFoundException {

    public FileNotFoundException() {
        super("NotFound.file");
        setErrorCode(true);
    }

}
