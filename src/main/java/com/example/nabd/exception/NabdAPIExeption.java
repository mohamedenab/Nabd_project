package com.example.nabd.exception;


import org.springframework.http.HttpStatus;

public class NabdAPIExeption extends RuntimeException{
    private String message;
    private HttpStatus httpStatus;

    public NabdAPIExeption(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
