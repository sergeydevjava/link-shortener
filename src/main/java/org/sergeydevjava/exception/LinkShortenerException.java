package org.sergeydevjava.exception;

public class LinkShortenerException extends RuntimeException {
    public LinkShortenerException(String errorMessage) {
        super(errorMessage);
    }

    public LinkShortenerException(String errorMessage, Exception exception) {
        super(errorMessage, exception);
    }
}
