package com.infy.springboot_assessment.controller;


import com.infy.springboot_assessment.exception.EmployeeAlreadyExistException;
import com.infy.springboot_assessment.exception.EmployeeDoNotExistException;
import com.infy.springboot_assessment.exception.RestErrorObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    private final static Logger logger =  LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestErrorObject> handleRootException(Exception exception) {
        logger.error("Root Exception handler: ", exception);
        RestErrorObject restErrorObject = new RestErrorObject();
        restErrorObject.setErrorCode(HttpStatus.BAD_REQUEST.value());
        restErrorObject.setErrorMessage(exception.getMessage());
        return new ResponseEntity<>(restErrorObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmployeeAlreadyExistException.class)
    public ResponseEntity<RestErrorObject> handleEmployeeAlreadyExistsException(EmployeeAlreadyExistException exception) {
        logger.error("EmployeeAlreadyExistException handler: ", exception);
        RestErrorObject restErrorObject = new RestErrorObject();
        restErrorObject.setErrorCode(HttpStatus.CONFLICT.value());
        restErrorObject.setErrorMessage(exception.getMessage());
        return new ResponseEntity<>(restErrorObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmployeeDoNotExistException.class)
    public ResponseEntity<RestErrorObject> handleEmployeeDoNotExistException(EmployeeDoNotExistException exception) {
        logger.error("EmployeeDoNotExistException handler: ", exception);
        RestErrorObject restErrorObject = new RestErrorObject();
        restErrorObject.setErrorCode(HttpStatus.NOT_FOUND.value());
        restErrorObject.setErrorMessage(exception.getMessage());
        return new ResponseEntity<>(restErrorObject, HttpStatus.BAD_REQUEST);
    }
}
