package com.billlog.rest.api.advice.exception;

public class CUserModifyException extends RuntimeException{
    public CUserModifyException(String msg, Throwable t) {
        super(msg, t);
    }

    public CUserModifyException(String msg) {
        super(msg);
    }

    public CUserModifyException() {
        super();
    }
}
