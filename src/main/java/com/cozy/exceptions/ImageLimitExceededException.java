package com.cozy.exceptions;

public class ImageLimitExceededException extends RuntimeException {

    public ImageLimitExceededException(String message) {
        super(message);
    }
}
