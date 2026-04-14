package com.rise.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomException extends RuntimeException{

    private String msg;


}
