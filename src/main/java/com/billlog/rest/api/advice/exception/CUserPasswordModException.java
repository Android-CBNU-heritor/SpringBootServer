package com.billlog.rest.api.advice.exception;

public class CUserPasswordModException extends RuntimeException {
    public CUserPasswordModException(String msg, Throwable t) {
        super(msg, t);
    }

    public CUserPasswordModException(String msg) {
        super(msg);
    }

    public CUserPasswordModException() {
        super();
    }
}
