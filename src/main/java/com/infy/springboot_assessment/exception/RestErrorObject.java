package com.infy.springboot_assessment.exception;

import lombok.Data;

@Data
public class RestErrorObject
{
    private int errorCode;
    private String errorMessage;
}
