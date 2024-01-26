package com.cozy.exceptions;

import com.cozy.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundHandler(NotFoundException notFoundException) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), notFoundException.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse conflictHandler(ConflictException conflictException) {
        return new ErrorResponse(HttpStatus.CONFLICT.value(), conflictException.getMessage());
    }

    @ExceptionHandler(ImageLimitExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse imageLimitExceededtHandler(ImageLimitExceededException imageLimitExceededException) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), imageLimitExceededException.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse emailAlreadyExistsExceededtHandler(EmailAlreadyExistsException emailAlreadyExistsException) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), emailAlreadyExistsException.getMessage());
    }

    @ExceptionHandler(AccountDisabledException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse accountDisabledExceededtHandler(AccountDisabledException accountDisabledException) {
        return new ErrorResponse(HttpStatus.FORBIDDEN.value(), accountDisabledException.getMessage());
    }

    @ExceptionHandler(InvalidOtpException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidOtpExceptiontHandler(InvalidOtpException invalidOtpException) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), invalidOtpException.getMessage());
    }

    @ExceptionHandler(EmailSendingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse emailSendingExceptiontHandler(EmailSendingException emailSendingException) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), emailSendingException.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequestExceededtHandler(BadRequestException badRequestException) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), badRequestException.getMessage());
    }
}
