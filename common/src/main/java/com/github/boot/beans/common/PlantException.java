package com.github.boot.beans.common;

public class PlantException extends RuntimeException {


    private Integer code;

    public PlantException() {
    }

    public PlantException(String message) {
        super(message);
    }

    public PlantException(String message,Integer code) {
        super(message);
        this.code = code;
    }

    public PlantException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlantException(Throwable cause) {
        super(cause);
    }

    public PlantException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
