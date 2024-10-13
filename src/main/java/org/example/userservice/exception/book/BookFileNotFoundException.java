package org.example.userservice.exception.book;

public class BookFileNotFoundException extends RuntimeException {

    public BookFileNotFoundException(String message) {
        super(message);
    }

    public BookFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
