package com.citadele.homeassignment.controller;

import com.citadele.homeassignment.controller.exception.ErrorResponse;
import com.citadele.homeassignment.controller.exception.LoanApplicationException;
import com.citadele.homeassignment.controller.exception.LoanApplicationValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LoanApplicationControllerAdvice {
    @ExceptionHandler(LoanApplicationValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(LoanApplicationValidationException ex) {
        return new ErrorResponse("An error occurred: " + ex.getMessage());
    }

    @ExceptionHandler(LoanApplicationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(LoanApplicationException ex) {
        return new ErrorResponse("Oops, something went wrong. Please try again later.");
    }
}
