package org.sergeydevjava.exception;

public class LinkShortenerException extends RuntimeException {
    LinkShortenerException(String errorMessage) {
        super(errorMessage);
    }

    LinkShortenerException(String errorMessage, Exception exception) {
        super(errorMessage, exception);
    }
}
