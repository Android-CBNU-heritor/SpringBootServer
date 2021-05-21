package com.billlog.rest.api.advice.exception;

public class CInfoArticleNotFoundException extends RuntimeException {

    public CInfoArticleNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public CInfoArticleNotFoundException(String msg) {
        super(msg);
    }

    public CInfoArticleNotFoundException() {
        super();
    }

}
