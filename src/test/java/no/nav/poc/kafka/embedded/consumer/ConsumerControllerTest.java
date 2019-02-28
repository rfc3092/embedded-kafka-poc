package no.nav.poc.kafka.embedded.consumer;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import no.nav.poc.kafka.embedded.common.SomeJsonContent;
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
public class ConsumerControllerTest {

    private static final SomeJsonContent ONE = SomeJsonContent
        .builder()
        .celcius(1)
        .message("One")
        .build();
    private static final SomeJsonContent TWO = SomeJsonContent
        .builder()
        .celcius(2)
        .message("Two")
        .build();
    private static final SomeJsonContent THREE = SomeJsonContent
        .builder()
        .celcius(3)
        .message("Three")
        .build();

    @LocalServerPort
    private int port;

    @MockBean
    ConsumerService mockedConsumerService;

    @Before
    public void before() {

        when(mockedConsumerService.receive())
            .thenReturn(Arrays.asList(ONE, TWO, THREE));

    }

    @Test
    public void controllerShouldReturnMockedContentFromUnderlyingService() {

        given()
            .port(port)
            .when()
            .accept(JSON)
            .get("/consumer")
            .then()
            .assertThat()
            .statusCode(200)
            .body("message", hasSize(3))
            .body("message", containsInAnyOrder(ONE.getMessage(), TWO.getMessage(), THREE.getMessage()));

    }


}
