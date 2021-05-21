package com.billlog.rest.api.advice.exception;

public class CUserEmailNotFoundException extends RuntimeException {
    public CUserEmailNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
    public CUserEmailNotFoundException(String msg) {
        super(msg);
    }
    public CUserEmailNotFoundException() {
        super();
    }
}