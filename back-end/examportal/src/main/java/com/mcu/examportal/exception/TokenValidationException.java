package com.mcu.examportal.exception;

import lombok.Data;

@Data
public class TokenValidationException  extends RuntimeException{
    public  TokenValidationException(){}
    public  TokenValidationException(String message){super(message);}
}
