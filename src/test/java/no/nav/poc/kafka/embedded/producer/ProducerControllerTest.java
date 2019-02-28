package no.nav.poc.kafka.embedded.producer;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;
import no.nav.poc.kafka.embedded.common.SomeJsonContent;
import no.nav.poc.kafka.embedded.consumer.ConsumerService;
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
    private ConsumerService mockedConsumerService; // Not used, but mocked out to avoid Kafka setup issues.

    @MockBean
    private ProducerService mockedProducerService;

    @Before
    public void before() {

        when(mockedProducerService.send(any(SomeJsonContent.class)))
            .thenReturn(UUID.randomUUID());

    }

    @Test
    public void controllerShouldAcceptValidContent() {

        SomeJsonContent content = SomeJsonContent
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
            .body(SomeJsonContent.builder().build())
            .post("/producer")
            .then()
            .assertThat()
            .statusCode(400);

    }

}
