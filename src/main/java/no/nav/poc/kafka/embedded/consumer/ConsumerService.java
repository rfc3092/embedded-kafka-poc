package no.nav.poc.kafka.embedded.consumer;

import java.util.ArrayList;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.poc.kafka.AppConfig;
import no.nav.poc.kafka.avro.SomeAvroContent;
import no.nav.poc.kafka.embedded.common.SomeJsonContent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
// TODO: Thread safety.
public class ConsumerService {

    private final ArrayList<SomeJsonContent> cache = new ArrayList<>();
    private final AppConfig config;

    Collection<SomeJsonContent> receive() {
        return cache;
    }

    // TODO: Custom error handler.
    @KafkaListener(topics = "${my.config.topics.hot}")
    public void hotReceiver(SomeAvroContent content) {
        log.info("Received content {} from topic {}", content, config.getTopics().getHot());
        cache.add(mapToDomain(content));
        log.info("Cache size {}", cache.size());
    }

    // TODO: Custom error handler.
    @KafkaListener(topics = "${my.config.topics.cold}")
    public void coldReceiver(SomeAvroContent content) {
        log.info("Received content {} from topic {}", content, config.getTopics().getCold());
        cache.add(mapToDomain(content));
        log.info("Cache size {}", cache.size());

    }

    private static SomeJsonContent mapToDomain(SomeAvroContent content) {
        return SomeJsonContent
            .builder()
            .celcius(content.getCelcius())
            .message(content.getMessage())
            .build();
    }

}
