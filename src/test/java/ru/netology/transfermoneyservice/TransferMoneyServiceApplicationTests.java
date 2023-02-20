package ru.netology.transfermoneyservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import ru.netology.transfermoneyservice.model.response.TransferAndConfirmResponse;

import java.util.Objects;

import static ru.netology.transfermoneyservice.TestData.OPERATION_ID;
import static ru.netology.transfermoneyservice.TestData.TRANSFER_REQUEST_1;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransferMoneyServiceApplicationTests {

    private static final String HOST_WITHOUT_PORT = "http://localhost:";
    private static final String TRANSFER = "/transfer";
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

}
