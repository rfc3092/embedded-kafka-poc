package no.nav.poc.kafka.embedded;

import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import no.nav.poc.kafka.avro.SomeAvroContent;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@TestConfiguration
public class MockSchemaRegistryClientConfig {

    @Bean
    SchemaRegistryClient schemaRegistryClient() {
        return new MockSchemaRegistryClient();
    }

    @Bean
    KafkaAvroSerializer kafkaAvroSerializer(SchemaRegistryClient client, KafkaProperties properties) {
        return new KafkaAvroSerializer(client, properties.buildProducerProperties());
    }

    // TODO: This ain't pretty.
    @SuppressWarnings("unchecked")
    @Bean
    ProducerFactory producerFactory(KafkaProperties properties, KafkaAvroSerializer serializer) {
        return new DefaultKafkaProducerFactory(
            properties.buildProducerProperties(),
            new StringSerializer(),
            serializer
        );
    }

    @Bean
    KafkaTemplate<String, SomeAvroContent> kafkaTemplate(ProducerFactory<String, SomeAvroContent> factory) {
        return new KafkaTemplate<>(factory);
    }

    @Bean
    KafkaAvroDeserializer kafkaAvroDeserializer(SchemaRegistryClient client, KafkaProperties properties) {
        return new KafkaAvroDeserializer(client, properties.buildConsumerProperties());
    }

    // TODO: This ain't pretty.
    @SuppressWarnings("unchecked")
    @Bean
    ConsumerFactory consumerFactory(KafkaProperties properties, KafkaAvroDeserializer deserializer) {
        return new DefaultKafkaConsumerFactory(
            properties.buildConsumerProperties(),
            new StringDeserializer(),
            deserializer
        );
    }

}
