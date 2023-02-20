package ru.netology.transfermoneyservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.netology.transfermoneyservice.exception.ConfirmException;
import ru.netology.transfermoneyservice.exception.InputDataException;
import ru.netology.transfermoneyservice.exception.TransferException;
import ru.netology.transfermoneyservice.model.response.ErrorResponse;

import java.util.concurrent.atomic.AtomicInteger;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {
    private final AtomicInteger id = new AtomicInteger();

    @ExceptionHandler(InputDataException.class)
    public ResponseEntity<ErrorResponse> handleInputData(InputDataException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), incrementAndGetID()));
    }


    @ExceptionHandler(TransferException.class)
    public ResponseEntity<ErrorResponse> handleTransfer(TransferException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage(), incrementAndGetID()));
    }


    @ExceptionHandler(ConfirmException.class)
    public ResponseEntity<ErrorResponse> handleConfirmation(ConfirmException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), incrementAndGetID()));
    }


    private int incrementAndGetID() {
        return id.incrementAndGet();
    }

}
