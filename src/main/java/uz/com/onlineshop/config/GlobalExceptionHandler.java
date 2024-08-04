package uz.com.onlineshop.config;

import jakarta.mail.AuthenticationFailedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uz.com.onlineshop.exception.*;
import uz.com.onlineshop.response.StandardResponse;
import uz.com.onlineshop.response.Status;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {RequestValidationException.class})
    public ResponseEntity<StandardResponse<String>>
    requestValidationExceptionHandler(
            RequestValidationException e
    ){
        return ResponseEntity.status(400).body(StandardResponse.<String>builder().status(Status.ERROR).message(e.getMessage()).build());
    }

    @ExceptionHandler(value = {AuthenticationFailedException.class})
    public ResponseEntity<StandardResponse<String>> authenticationFailedExceptionHandler(
            AuthenticationFailedException e
    ){
        return ResponseEntity.status(401).body(StandardResponse.<String>builder().status(Status.ERROR).message(e.getMessage()).build());
    }
    @ExceptionHandler(value = {DataNotFoundException.class})
    public ResponseEntity<StandardResponse<String>> dataNotFoundExceptionHandler(
            DataNotFoundException e){
        return ResponseEntity.status(404).body(StandardResponse.<String>builder().status(Status.ERROR).message(e.getMessage()).build());

    }
    @ExceptionHandler(value = {UserBadRequestException.class})
    public ResponseEntity<StandardResponse<String>> userBadRequestExceptionHandler(
            UserBadRequestException e){
        return ResponseEntity.status(400).body(StandardResponse.<String>builder().status(Status.ERROR).message(e.getMessage()).build());
    }

    @ExceptionHandler(value = {DataHasAlreadyExistException.class})
    public ResponseEntity<StandardResponse<String>> dataHasAlreadyExistException(
            DataHasAlreadyExistException e){
        return ResponseEntity.status(400).body(StandardResponse.<String>builder().status(Status.ERROR).message(e.getMessage()).build());
    }
    @ExceptionHandler(value = {NotAcceptableException.class})
    public ResponseEntity<StandardResponse<String>> notAcceptableException(
            NotAcceptableException e){
        return ResponseEntity.status(400).body(StandardResponse.<String>builder().status(Status.ERROR).message(e.getMessage()).build());
    }
}
