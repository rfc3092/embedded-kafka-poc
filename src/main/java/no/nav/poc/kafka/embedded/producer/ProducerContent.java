package no.nav.poc.kafka.embedded.producer;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProducerContent {

    private int celcius;

    @NotNull
    private String message;

    @Override
    public String toString() {
        return "{" + celcius + "," + message + "}";
    }

}
