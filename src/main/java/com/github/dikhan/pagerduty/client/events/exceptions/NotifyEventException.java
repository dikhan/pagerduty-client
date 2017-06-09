package com.github.dikhan.pagerduty.client.events.exceptions;

public class NotifyEventException extends Exception {

    public NotifyEventException(Throwable cause) {
        super(cause);
    }

    public NotifyEventException(String message) {
        super(message);
    }
}
