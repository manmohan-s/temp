package com.infy.springboot_assessment.exception;

public class EmployeeDoNotExistException extends Exception{
    public EmployeeDoNotExistException(Long id){
        super("Employee do not exists in our system for id: "+id);
    }
}
