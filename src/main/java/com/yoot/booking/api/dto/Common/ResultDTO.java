package com.yoot.booking.api.dto.Common;

public class ResultDTO<T> {

    private boolean success;
    private String message;
    private T data;

    public ResultDTO() {
    }

    public ResultDTO(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public static <T> ResultDTO<T> success(T data, String msg) {
        return new ResultDTO<>(true, msg, data);
    }

    public static <T> ResultDTO<T> success(T data) {
        return new ResultDTO<>(true, "", data);
    }

    public static <T> ResultDTO<T> fail(String msg) {
        return new ResultDTO<>(false, msg, null);
    }
}