package no.nav.poc.kafka.embedded.common;

import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class SomeJsonContent {

    private int celcius;

    @NotEmpty
    private String message;

}
