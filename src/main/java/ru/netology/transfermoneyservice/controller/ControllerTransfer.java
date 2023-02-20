package ru.netology.transfermoneyservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.transfermoneyservice.model.request.ConfirmRequest;
import ru.netology.transfermoneyservice.model.request.TransferRequest;
import ru.netology.transfermoneyservice.model.response.TransferAndConfirmResponse;
import ru.netology.transfermoneyservice.service.Service;

@RestController
@AllArgsConstructor
@CrossOrigin
public class ControllerTransfer {
    private final Service service;

    @PostMapping("/transfer")
    public TransferAndConfirmResponse postTransfer(@RequestBody TransferRequest transferRequest) {
        return service.postTransfer(transferRequest);
    }

    @PostMapping("/confirmOperation")
    public TransferAndConfirmResponse postConfirmOperation(@RequestBody ConfirmRequest confirmRequest) {
        return service.postConfirmOperation(confirmRequest);
    }
}
