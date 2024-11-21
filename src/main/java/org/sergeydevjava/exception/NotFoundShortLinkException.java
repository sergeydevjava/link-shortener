package org.sergeydevjava.exception;

public class NotFoundShortLinkException extends LinkShortenerException {
    public NotFoundShortLinkException(String errorMessage) {
        super(errorMessage);
    }
}
