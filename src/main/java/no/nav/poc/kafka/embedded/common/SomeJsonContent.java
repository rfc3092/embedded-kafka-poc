package no.nav.poc.kafka.embedded.common;

import java.util.UUID;
import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class SomeJsonContent {

    @Setter
    private UUID uuid;

    private final int celcius;

    @NotEmpty
    private final String message;

}
