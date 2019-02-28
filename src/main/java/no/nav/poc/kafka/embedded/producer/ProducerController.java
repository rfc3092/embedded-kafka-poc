package no.nav.poc.kafka.embedded.producer;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.poc.kafka.embedded.common.SomeJsonContent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    path = "/producer",
    consumes = APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
@Slf4j
class ProducerController {

    private final ProducerService service;

    @PostMapping
    public UUID produce(@Valid @RequestBody SomeJsonContent content) {
        return service.send(content);
    }

}
