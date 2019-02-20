package no.nav.poc.kafka.embedded.consumer;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
class ConsumerContent {

    private int celcius;

    @NotNull
    private String message;

    @Override
    public String toString() {
        return "{" + celcius + "," + message + "}";
    }

}
