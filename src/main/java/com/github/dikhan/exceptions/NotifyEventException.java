package com.github.dikhan.exceptions;

public class NotifyEventException extends Exception {

    public NotifyEventException(Throwable cause) {
        super(cause);
    }

    public NotifyEventException(String message) {
        super(message);
    }
}
