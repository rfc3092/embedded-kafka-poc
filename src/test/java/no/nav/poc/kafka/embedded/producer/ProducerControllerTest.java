package no.nav.poc.kafka.embedded.producer;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;
import no.nav.poc.kafka.embedded.consumer.ReceivingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProducerControllerTest {

    @LocalServerPort
    private int port;

    @MockBean
    private ReceivingService mockedReceivingService; // Not used, but mocked out to avoid Kafka setup issues.

    @MockBean
    private SendingService mockedSendingService;

    @Before
    public void before() {

        when(mockedSendingService.send(any(ProducerContent.class)))
            .thenReturn(UUID.randomUUID());

    }

    @Test
    public void controllerShouldAcceptValidContent() {

        ProducerContent content = ProducerContent
            .builder()
            .celcius(0)
            .message("Some message...")
            .build();

        given()
            .port(port)
            .accept(JSON)
            .contentType(JSON)
            .body(content)
            .post("/producer")
            .then()
            .assertThat()
            .statusCode(200);

    }

    @Test
    public void controllerShouldNotAcceptEmptyContent() {

        given()
            .port(port)
            .when()
            .accept(JSON)
            .contentType(JSON)
            .body("")
            .post("/producer")
            .then()
            .assertThat()
            .statusCode(400);

    }

    @Test
    public void controllerShouldNotAcceptIncompleteContent() {

        given()
            .port(port)
            .when()
            .accept(JSON)
            .contentType(JSON)
            .body(ProducerContent.builder().build())
            .post("/producer")
            .then()
            .assertThat()
            .statusCode(400);

    }

}
