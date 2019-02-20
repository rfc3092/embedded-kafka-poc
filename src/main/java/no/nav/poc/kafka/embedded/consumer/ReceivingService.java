package no.nav.poc.kafka.embedded.consumer;

import java.util.ArrayList;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.poc.kafka.ApplicationConfig;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
// TODO: Thread safety.
public class ReceivingService {

    private final ArrayList<ConsumerContent> cache = new ArrayList<>();
    private final ApplicationConfig config;

    Collection<ConsumerContent> receive() {

        return cache;

    }

    @KafkaListener(topics = "${my.config.topics.hot}")
    public void hotReceiver(String message) {

        ConsumerContent content = ConsumerContent
            .builder()
            .celcius(1)
            .message(message)
            .build();
        log.info("Received content {} from topic {}", content, config.getTopics().getHot());
        cache.add(content);

    }

    @KafkaListener(topics = "${my.config.topics.cold}")
    public void coldReceiver(String message) {

        ConsumerContent content = ConsumerContent
            .builder()
            .celcius(-1)
            .message(message)
            .build();
        log.info("Received content {} from topic {}", content, config.getTopics().getCold());
        cache.add(content);

    }

}
