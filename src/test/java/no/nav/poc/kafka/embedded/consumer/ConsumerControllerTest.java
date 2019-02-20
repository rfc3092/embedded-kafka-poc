package no.nav.poc.kafka.embedded.consumer;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;

import java.util.Arrays;
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

    private static final ConsumerContent ONE = new ConsumerContent(1, "One");
    private static final ConsumerContent TWO = new ConsumerContent(2, "Two");
    private static final ConsumerContent THREE = new ConsumerContent(3, "Three");

    @LocalServerPort
    private int port;

    @MockBean
    ReceivingService mockedReceivingService;

    @Before
    public void before() {

        when(mockedReceivingService.receive())
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
