package com.mcu.examportal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotRegisterException.class)
    public  ResponseEntity<ExceptionModel> userNotRegister(UserNotRegisterException unre){
        ExceptionModel model = new ExceptionModel();
        model.setCode("DB-101");
        model.setExceptionName(unre.getMessage());
        return  new ResponseEntity<>(model, HttpStatus.ALREADY_REPORTED);
    }
    @ExceptionHandler(IncompleteUserInfoException.class)
    public  ResponseEntity<ExceptionModel> userNotRegister(IncompleteUserInfoException incompleteUserInfo){
        ExceptionModel model = new ExceptionModel();
        model.setCode("InCompleteUserInfo");
        model.setExceptionName(incompleteUserInfo.getMessage());
        return  new ResponseEntity<>(model, HttpStatus.ALREADY_REPORTED);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public  ResponseEntity<ExceptionModel> invalidRequest(InvalidRequestException invalidRequestException){
        ExceptionModel model = new ExceptionModel();
        model.setCode("IR-404");
        model.setExceptionName(invalidRequestException.getMessage());
        return  new ResponseEntity<>(model, HttpStatus.ALREADY_REPORTED);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public  ResponseEntity<ExceptionModel> resourcesNotFound(ResourceNotFoundException resourceNotFoundException){
        ExceptionModel model = new ExceptionModel();
        model.setCode("RNF-404");
        model.setExceptionName(resourceNotFoundException.getMessage());
        return  new ResponseEntity<>(model, HttpStatus.ALREADY_REPORTED);
    }
    @ExceptionHandler(TokenValidationException.class)
    public  ResponseEntity<ExceptionModel> resourcesNotFound(TokenValidationException tokenValidationException){
        ExceptionModel model = new ExceptionModel();
        model.setCode("TokenInvalid-404");
        model.setExceptionName(tokenValidationException.getMessage());
        return  new ResponseEntity<>(model, HttpStatus.ALREADY_REPORTED);
    }
}
