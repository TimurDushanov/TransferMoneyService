package ru.netology.transfermoneyservice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.transfermoneyservice.model.Amount;
import ru.netology.transfermoneyservice.model.Card;
import ru.netology.transfermoneyservice.model.request.ConfirmRequest;
import ru.netology.transfermoneyservice.model.request.TransferRequest;
import ru.netology.transfermoneyservice.model.response.TransferAndConfirmResponse;
import ru.netology.transfermoneyservice.repository.CardRepository;
import ru.netology.transfermoneyservice.service.Service;

public class ServiceTest {
    static Service sut;
    CardRepository cardRepository = Mockito.mock(CardRepository.class);

    @BeforeEach
    public void InitAndStart() {
        sut = new Service(cardRepository);
    }

    @AfterEach
    public void finished() {
        sut = null;
    }

    @Test
    public void testPostTransfer() {
        Card cardFromNumber = new Card("1000000000000000", "1025", "123",
                new Amount(100, "руб"));
        Card cardToNumber = new Card("2000000000000000", "1025", "123",
                new Amount(10, "руб"));

        long transferAmount = 100L;

        Mockito.when(cardRepository.getCard("1000000000000000"))
                .thenReturn(cardFromNumber);

        Mockito.when(cardRepository.getCard("2000000000000000"))
                .thenReturn(cardToNumber);

        TransferRequest transfer = new TransferRequest("1000000000000000", "1025", "123",
                "2000000000000000", new Amount(transferAmount, "RUR"));

        final TransferAndConfirmResponse expected = new TransferAndConfirmResponse("1");

        Mockito.when(cardRepository.incrementAndGetOperationID()).thenReturn(Integer.valueOf("1"));

        TransferAndConfirmResponse actual = sut.postTransfer(transfer);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testPostConfirmOperation(){
        final ConfirmRequest confirmRequest = new ConfirmRequest("1", "0000");
        final String operationID = confirmRequest.getOperationId();
        long transferAmount = 100L;
        TransferRequest transfer = new TransferRequest("1000000000000000", "1025", "123",
                "2000000000000000", new Amount(transferAmount, "RUR"));

        Mockito.when(cardRepository.removeTransfer(operationID)).thenReturn(transfer);

        Card cardFromNumber = new Card("1000000000000000", "1025", "123",
                new Amount(100, "руб"));
        Card cardToNumber = new Card("2000000000000000", "1025", "123",
                new Amount(10, "руб"));

        Mockito.when(cardRepository.getCard("1000000000000000"))
                .thenReturn(cardFromNumber);

        Mockito.when(cardRepository.getCard("2000000000000000"))
                .thenReturn(cardToNumber);


        Mockito.when(cardRepository.removeCode("1")).thenReturn("0000");

        final TransferAndConfirmResponse expected = new TransferAndConfirmResponse("1");

        TransferAndConfirmResponse actual = sut.postConfirmOperation(confirmRequest);


        Assertions.assertEquals(expected, actual);

    }


}
