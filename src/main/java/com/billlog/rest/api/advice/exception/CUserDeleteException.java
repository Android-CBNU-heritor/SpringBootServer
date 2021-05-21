package com.billlog.rest.api.advice.exception;

public class CUserDeleteException extends RuntimeException{
    public CUserDeleteException(String msg, Throwable t) {
        super(msg, t);
    }
    public CUserDeleteException(String msg) {
        super(msg);
    }
    public CUserDeleteException() {
        super();
    }
}
