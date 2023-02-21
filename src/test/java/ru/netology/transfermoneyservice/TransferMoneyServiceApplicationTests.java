package ru.netology.transfermoneyservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.netology.transfermoneyservice.model.Amount;
import ru.netology.transfermoneyservice.model.request.ConfirmRequest;
import ru.netology.transfermoneyservice.model.request.TransferRequest;
import ru.netology.transfermoneyservice.model.response.TransferAndConfirmResponse;

import java.util.Objects;

import static ru.netology.transfermoneyservice.TestData.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransferMoneyServiceApplicationTests {

    private static final String HOST_WITHOUT_PORT = "http://localhost:";
    private static final String TRANSFER = "/transfer";

    private static final String CONFIRM_OPERATION = "/confirmOperation";
    private static final int PORT = 5500;

    @Autowired
    TestRestTemplate testRestTemplate;

    private static final GenericContainer<?> container = new GenericContainer<>("transfer")
            .withExposedPorts(PORT);

    @BeforeAll
    public static void startContainer() {
        container.start();
    }

    @Test
    void contextLoads() {
        ResponseEntity<TransferAndConfirmResponse> forTransfer = testRestTemplate
                .postForEntity(HOST_WITHOUT_PORT + container.getMappedPort(PORT) + TRANSFER, TRANSFER_REQUEST_1, TransferAndConfirmResponse.class);
        Assertions.assertEquals(Objects.requireNonNull(forTransfer.getBody()).getOperationId(), OPERATION_ID);
    }

    @Test
    void testHttpStatusOkRequestTransfer(){
        ResponseEntity<String> response = testRestTemplate.postForEntity(HOST_WITHOUT_PORT + container.getMappedPort(PORT) +
                TRANSFER, TRANSFER_REQUEST_1, String.class);

        HttpStatus expected = HttpStatus.OK;

        HttpStatus actual = (HttpStatus) response.getStatusCode();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testHttpStatusBadRequestTransfer() {
        TransferRequest badTransferRequest = new TransferRequest("1000000000000000", "10/24",
                "111", "2000000000000000", new Amount(1_000_000, "руб"));

        ResponseEntity<String> response = testRestTemplate.postForEntity(HOST_WITHOUT_PORT + container.getMappedPort(PORT) +
                TRANSFER, badTransferRequest, String.class);

        HttpStatus expected = HttpStatus.BAD_REQUEST;

        HttpStatus actual = (HttpStatus) response.getStatusCode();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testConfirmOperation() {
        ResponseEntity<String> response = testRestTemplate.postForEntity(HOST_WITHOUT_PORT + container.getMappedPort(PORT) +
                CONFIRM_OPERATION, CONFIRM_REQUEST, String.class);

        String expected = "{\"operationId\":\"1\"}";

        String actual = response.getBody();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testHttpStatusBadConfirmOperation() {
        ConfirmRequest confirmRequest = new ConfirmRequest("25", "0000");
        ResponseEntity<String> response = testRestTemplate.postForEntity(HOST_WITHOUT_PORT + container.getMappedPort(PORT) +
                CONFIRM_OPERATION, confirmRequest, String.class);

        HttpStatus expected = HttpStatus.BAD_REQUEST;

        HttpStatus actual = (HttpStatus) response.getStatusCode();

        Assertions.assertEquals(expected, actual);

    }

}
