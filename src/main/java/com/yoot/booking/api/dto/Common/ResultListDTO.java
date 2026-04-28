package com.yoot.booking.api.dto.Common;

import java.util.List;

public class ResultListDTO<T> {

    private boolean success;
    private String message;
    private List<T> data;
    private PaginationDTO pagination;

    public ResultListDTO() {}

    public ResultListDTO(boolean success, String message, List<T> data, PaginationDTO pagination) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.pagination = pagination;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<T> getData() {
        return data;
    }

    public PaginationDTO getPagination() {
        return pagination;
    }

    public static <T> ResultListDTO<T> success(List<T> data, String msg, PaginationDTO pagination) {
        return new ResultListDTO<>(true, msg, data, pagination);
    }

    public static <T> ResultListDTO<T> success(List<T> data, String msg) {
        return new ResultListDTO<>(true, msg, data, null);
    }

    public static <T> ResultListDTO<T> fail(String msg) {
        return new ResultListDTO<>(false, msg, null, null);
    }
}