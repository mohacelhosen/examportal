package com.mcu.examportal.exception;

public class InvalidRequestException extends RuntimeException{
    public InvalidRequestException(){}
    public InvalidRequestException(String message){super(message);}
}
