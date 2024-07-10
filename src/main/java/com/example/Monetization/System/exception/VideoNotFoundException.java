package com.example.Monetization.System.exception;

public class VideoNotFoundException extends RuntimeException{
    public VideoNotFoundException(String message){
        super(message);
    }
}
