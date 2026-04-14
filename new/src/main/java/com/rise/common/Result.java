package com.rise.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Result {
    private static final String SUCCESS = "0";
    private static final String ERROR = "-1";

    private  String message;
    private  String code;
    private  Object data;

    public static Result success() {
        Result result = new Result();
        result.setCode(SUCCESS);
        return result;
    }
    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(SUCCESS);
        result.setData(data);
        return result;
    }

    public static Result error(String message) {
        Result result = new Result();
        result.setCode(ERROR);
        result.setMessage(message);
        return result;
    }



}
