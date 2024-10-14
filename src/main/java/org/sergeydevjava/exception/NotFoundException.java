package org.sergeydevjava.exception;

public class NotFoundException extends LinkShortenerException {

    public NotFoundException(String errorMessage) {
        super(errorMessage);
    }

}
