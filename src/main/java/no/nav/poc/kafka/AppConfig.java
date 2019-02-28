package no.nav.poc.kafka;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "my.config")
@Getter
@Setter
public class AppConfig {

    private int limit = 0;

    @NotNull
    private Topics topics;

    @Getter
    @Setter
    public static class Topics {

        @NotEmpty
        private String hot;

        @NotEmpty
        private String cold;

    }

}
