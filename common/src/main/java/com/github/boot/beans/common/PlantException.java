package com.github.boot.beans.common;

import cn.hutool.http.HttpStatus;

public class PlantException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String message;
    private int code = HttpStatus.HTTP_INTERNAL_ERROR;

    public PlantException(String message) {
        super(message);
        this.message = message;
    }

    public PlantException(String message, int code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public PlantException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    public PlantException(String message, int code, Throwable e) {
        super(message, e);
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}


