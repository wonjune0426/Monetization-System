package com.example.monetization.system.exception;

public class VideoNotFoundException extends RuntimeException{
    public VideoNotFoundException(String message){
        super(message);
    }
}
