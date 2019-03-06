package no.nav.poc.kafka.embedded.consumer;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.poc.kafka.AppConfig;
import no.nav.poc.kafka.avro.SomeAvroContent;
import no.nav.poc.kafka.embedded.common.SomeJsonContent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
// TODO: Thread safety.
public class ConsumerService {

    private final ConcurrentLinkedQueue<SomeJsonContent> cache = new ConcurrentLinkedQueue<>();
    private final AppConfig config;

    Collection<SomeJsonContent> receive() {
        return cache;
    }

    // TODO: Custom error handler.
    @KafkaListener(topics = "${my.config.topics.hot}")
    @KafkaListener(
        clientIdPrefix = "consumeHot",
        topics = "${my.config.topics.hot}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void hotReceiver(ConsumerRecord<String, SomeAvroContent> content) {
        log.info("Received content {} from topic {}", content, config.getTopics().getHot());
        cache.add(mapToDomain(content.value()));
    }

    // TODO: Custom error handler.
    @KafkaListener(
        clientIdPrefix = "consumeCold",
        topics = "${my.config.topics.cold}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void coldReceiver(ConsumerRecord<String, SomeAvroContent> content) {
        log.info("Received content {} from topic {}", content, config.getTopics().getCold());
        cache.add(mapToDomain(content.value()));
    }

    private static SomeJsonContent mapToDomain(SomeAvroContent content) {
        return SomeJsonContent
            .builder()
            .uuid(UUID.fromString(content.getUuid()))
            .celcius(content.getCelcius())
            .message(content.getMessage())
            .build();
    }

}
