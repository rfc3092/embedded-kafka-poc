package no.nav.poc.kafka.embedded;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import no.nav.poc.kafka.AppConfig;
import no.nav.poc.kafka.avro.SomeAvroContent;
import no.nav.poc.kafka.embedded.common.SomeJsonContent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("embedded-kafka")
@Import(MockSchemaRegistryClientConfig.class)
@EmbeddedKafka(controlledShutdown = true)
@DirtiesContext
@Slf4j
public class ProducerConsumerTest {

    private static final Random random = new Random();

    @LocalServerPort
    private int port;

    @Autowired
    AppConfig config;

    @Autowired
    SchemaRegistryClient schemaRegistryClient;

    @Autowired
    ProducerFactory<String, SomeAvroContent> producerFactory;

    @Before
    public void before()
        throws Exception {

        schemaRegistryClient.register(config.getTopics().getHot() + "-value", SomeAvroContent.getClassSchema());
        schemaRegistryClient.register(config.getTopics().getCold() + "-value", SomeAvroContent.getClassSchema());
        Thread.sleep(2000); // Let embedded Kafka get ready.

    }


    @Test
    public void testSendAndReceiveOfMultipleMessages() {

        List<SomeJsonContent> sent = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {

            int celcius = random.nextInt(61) - 30; // -30 to 30.
            String message = "This will probably go into the '" + (celcius >= config.getLimit() ? "hot" : "cold") + "' topic...";
            SomeJsonContent content = SomeJsonContent
                .builder()
                .celcius(celcius)
                .message(message)
                .build();
            log.info("Preparing to send {}", content);
            SomeJsonContent response = given()
                .port(port)
                .contentType(JSON)
                .accept(JSON)
                .body(content)
                .post("/producer")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .as(SomeJsonContent.class);
            log.info("Got UUID {} in response", response.getUuid());
            sent.add(response);

        }

        SomeJsonContent[] received = given()
            .port(port)
            .accept(JSON)
            .get("/consumer")
            .then()
            .assertThat()
            .statusCode(200)
            .contentType(JSON)
            .extract()
            .body()
            .as(SomeJsonContent[].class);

        assertThat(sent.size(), equalTo(received.length));
        assertThat(sent, containsInAnyOrder(received));

    }

}
