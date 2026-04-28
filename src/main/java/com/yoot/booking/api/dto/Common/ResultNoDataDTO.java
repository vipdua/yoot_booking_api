package com.yoot.booking.api.dto.Common;

public class ResultNoDataDTO{

    private boolean success;
    private String message;

    public ResultNoDataDTO() {}

    public ResultNoDataDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static ResultNoDataDTO success(String msg) {
        return new ResultNoDataDTO(true, msg);
    }

    public static ResultNoDataDTO fail(String msg) {
        return new ResultNoDataDTO(false, msg);
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }

}