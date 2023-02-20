package ru.netology.transfermoneyservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.netology.transfermoneyservice.exception.InputDataException;
import ru.netology.transfermoneyservice.model.Card;
import ru.netology.transfermoneyservice.model.request.ConfirmRequest;
import ru.netology.transfermoneyservice.model.request.TransferRequest;
import ru.netology.transfermoneyservice.model.response.TransferAndConfirmResponse;
import ru.netology.transfermoneyservice.repository.CardRepository;

@org.springframework.stereotype.Service
@AllArgsConstructor
@Slf4j
public class Service {
    private final CardRepository repository;

    public TransferAndConfirmResponse postTransfer(TransferRequest transferRequest) {
        final Card cardFrom = repository.getCard(transferRequest.getCardFromNumber());
        final Card cardTo = repository.getCard(transferRequest.getCardToNumber());

        if (cardFrom == null && cardTo != null) {
            log.error("Карта отправителя {} не найдена", cardFrom.getCardNumber());
            throw new InputDataException("Карта отправителя не найдена");
        } else if (cardFrom != null && cardTo == null) {
            log.error("Карта получателя {} не найдена", cardTo.getCardNumber());
            throw new InputDataException("Карта получателя не найдена");
        } else if (cardFrom == null && cardTo == null) {
            log.error("Карта отправителя {} и карта получателя {} не найдены", cardFrom.getCardNumber(), cardTo.getCardNumber());
            throw new InputDataException("Карта отправителя и получателя не найдены");
        }

        if (cardFrom == cardTo) {
            log.error("Указаны идентичные номера карт {} = {}", cardFrom.getCardNumber(), cardTo.getCardNumber());
            throw new InputDataException("Карта отправителя не найдена");
        }

        final String cardFromValidTill = cardFrom.getCardValidTill();
        final String cardFromCVV = cardFrom.getCardCVV();
        final String transferRequestCardFromValidTill = transferRequest.getCardFromValidTill();
        final String transferRequestCardFromCVV = transferRequest.getCardFromCVV();

        if (!cardFromValidTill.equals(transferRequestCardFromValidTill) && cardFromCVV.equals(transferRequestCardFromCVV)) {
            log.info("Указан неверный срок действия карты");
            throw new InputDataException("Указан неверный срок действия карты");
        } else if (cardFromValidTill.equals(transferRequestCardFromValidTill) && !cardFromCVV.equals(transferRequestCardFromCVV)) {
            log.error("Указан не верный CVV код");
            throw new InputDataException("Указан не верный CVV код");
        } else if (!cardFromValidTill.equals(transferRequestCardFromValidTill) && !cardFromCVV.equals(transferRequestCardFromCVV)) {
            log.error("Указаны не верные срок действия и CVV код карты");
            throw new InputDataException("Указаны не верный срок действия и CVV код карты");
        }

        if (cardFrom.getAmount().getValue() < transferRequest.getAmount().getValue()) {
            log.error("Недостаточно средств на карте {}", cardFrom.getCardNumber());
            throw new InputDataException("Недостаточно средств на карте");
        } else if (cardFrom.getAmount().getValue() <= 0) {
            log.error("Сумма должна быть больше, чем {}", cardFrom.getAmount().getValue());
            throw new InputDataException("Недостаточно средств на карте");
        }

        final String transferID = Integer.toString(repository.incrementAndGetOperationID());
        repository.putTransfer(transferID, transferRequest);
        repository.putCodes(transferID, "0000");

        return new TransferAndConfirmResponse(transferID);

    }

    public TransferAndConfirmResponse postConfirmOperation(ConfirmRequest confirmRequest) {
        final String operationID = confirmRequest.getOperationId();

        final TransferRequest transferRequest = repository.removeTransfer(operationID);
        if (transferRequest == null) {
            log.error("операция не найдена");
            throw new InputDataException("операция не найдена");
        }

        final String code = repository.removeCode(operationID);
        if (!confirmRequest.getCode().equals(code) || code.isEmpty()) {
            log.error("не верный код");
            throw new InputDataException("неверный код");
        }

        final Card cardFrom = repository.getCard(transferRequest.getCardFromNumber());
        final Card cardTo = repository.getCard(transferRequest.getCardToNumber());

        final float cardFromAmount = cardFrom.getAmount().getValue();
        final float cardToAmount = cardTo.getAmount().getValue();
        final float transferAmount = transferRequest.getAmount().getValue();
        final float commission = (float) (transferAmount * 0.01);
        final String currency = transferRequest.getAmount().getCurrency();

        cardFrom.getAmount().setValue(cardFromAmount - transferAmount - commission);
        cardTo.getAmount().setValue(cardToAmount + transferAmount);

        log.info("Перевод успешно осуществлен. Номер операции {}, № карты отправителя {}, № карты получателя {}, " +
                        "сумма перевода {}, валюта перевода {}, комиссия за перевод {}", operationID, transferRequest.getCardFromNumber(),
                transferRequest.getCardToNumber(), transferAmount, currency, commission);

        return new TransferAndConfirmResponse(operationID);
    }

}









