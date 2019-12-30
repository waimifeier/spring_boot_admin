package com.github.boot.beans.common;


import cn.hutool.http.HttpStatus;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by apple on 2018/9/24.
 */
@Data
@ToString
public class JSONReturn implements Serializable {

    private static boolean SUCCESS = true;
    private static boolean FAILURE = false;

    private Boolean head;
    private Object content;
    private Integer status;
    private String message;


    private JSONReturn() {
    }



    private JSONReturn(Boolean head, Object content, Integer status, String message) {
        this.head = head;
        this.content = content;
        this.status = status;
        this.message = message;
    }

    public static JSONReturn build(boolean head, Object content, Integer status, String message) {
        return new JSONReturn(head, content, status, message);
    }

    public static JSONReturn buildSuccess(Object content, Integer status) {
        return build(SUCCESS, content, status, "成功");
    }

    public static JSONReturn buildSuccess(Object content) {
        return buildSuccess(content, HttpStatus.HTTP_OK, "成功");
    }

    public static JSONReturn buildSuccess(Object content, String message) {
        return buildSuccess(content, HttpStatus.HTTP_OK, message);
    }

    public static JSONReturn buildSuccess(Object content, Integer status, String message) {
        return buildSuccess(content, HttpStatus.HTTP_OK);
    }

    public static JSONReturn buildSuccessEmptyBody() {
        return buildSuccess(null);
    }

    public static JSONReturn buildFailure(String message, Integer status) {
        return build(FAILURE, null, status, message);
    }

    public static JSONReturn buildFailure(String message) {
        return buildFailure(message, HttpStatus.HTTP_INTERNAL_ERROR);
    }

    public static JSONReturn buildFailureContent(Object content, int state, String message) {
        return build(FAILURE, content, state, message);
    }

    public static JSONReturn buildFailureEmptyBody() {
        return buildSuccess(null);
    }

}
