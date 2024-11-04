package org.sergeydevjava.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, value = HttpStatus.NOT_FOUND)
public class NotFoundException extends LinkShortenerException {

    public NotFoundException(String errorMessage) {
        super(errorMessage);
    }

}
