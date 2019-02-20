package no.nav.poc.kafka.embedded;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

import java.util.Random;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import no.nav.poc.kafka.ApplicationConfig;
import no.nav.poc.kafka.embedded.producer.ProducerContent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("embedded-kafka")
@EmbeddedKafka
@DirtiesContext
@Slf4j
public class ProducerConsumerTest {

    private static final Random random = new Random();

    @LocalServerPort
    private int port;

    @Autowired
    ApplicationConfig config;

    @Test
    public void testSendAndReceiveOfMultipleMessages() {

        for (int i = 0; i < 10; i++) {

            int celcius = random.nextInt(61) - 30; // -30 to 30.
            String message = "This will probably go into the '" + (celcius >= config.getLimit() ? "hot" : "cold") + "' topic...";
            ProducerContent outgoing = ProducerContent
                .builder()
                .celcius(celcius)
                .message(message)
                .build();
            log.info("Preparing to send {}", outgoing);
            UUID response = given()
                .port(port)
                .contentType(JSON)
                .body(outgoing)
                .post("/producer")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .as(UUID.class);
            log.info("Received UUID {} in response", response);

        }

        // TODO: Validate response against what was sent.
        log.info(given()
            .port(port)
            .accept(JSON)
            .get("/consumer")
            .then()
            .assertThat()
            .statusCode(200)
            .extract()
            .body()
            .asString());

    }

}
