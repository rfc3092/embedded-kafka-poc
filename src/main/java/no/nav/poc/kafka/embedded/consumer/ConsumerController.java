package no.nav.poc.kafka.embedded.consumer;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    path = "/consumer",
    produces = APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
class ConsumerController {

    private final ReceivingService service;

    @GetMapping
    public Collection<ConsumerContent> getMessages() {
        return service.receive();
    }

}
