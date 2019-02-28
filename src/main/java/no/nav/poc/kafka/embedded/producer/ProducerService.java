package no.nav.poc.kafka.embedded.producer;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.poc.kafka.AppConfig;
import no.nav.poc.kafka.avro.SomeAvroContent;
import no.nav.poc.kafka.embedded.common.SomeJsonContent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
class ProducerService {

    private final AppConfig config;
    private final KafkaTemplate<String, SomeAvroContent> template;

    UUID send(SomeJsonContent content) {

        UUID id = UUID.randomUUID();
        String topic = getTopic(content.getCelcius());
        log.info("Sending content {} to topic {}", content, topic);

        template
            .send(topic, id.toString(), mapFromDomain(content))
            .addCallback(
                s -> log.error("Successfully sent message with result {}", s),
                f -> log.error("Failed to send message", f.getCause())
            );
        return id;

    }

    private String getTopic(int celcius) {
        return celcius >= config.getLimit() ? config.getTopics().getHot() : config.getTopics().getCold();
    }

    private static SomeAvroContent mapFromDomain(SomeJsonContent content) {
        return SomeAvroContent
            .newBuilder()
            .setCelcius(content.getCelcius())
            .setMessage(content.getMessage())
            .build();
    }

}
