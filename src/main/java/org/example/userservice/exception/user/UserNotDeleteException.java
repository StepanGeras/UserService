package org.example.userservice.exception.user;

public class UserNotDeleteException extends RuntimeException{

    public UserNotDeleteException(String message) {
        super(message);
    }
}
