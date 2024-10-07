package com.infy.springboot_assessment.exception;

public class EmployeeAlreadyExistException extends Exception{
    public EmployeeAlreadyExistException(Long id){
        super("Employee already exists in our system for id: "+id);
    }
}
