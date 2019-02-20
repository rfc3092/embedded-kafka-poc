package no.nav.poc.kafka;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "my.config")
public class ApplicationConfig {

    private int limit = 0;

    @NotNull
    private Topics topics;

    public int getLimit() {
        return limit;
    }

    /**
     * Values lower than the limit will go into the "cold" topic.
     *
     * @param limit The limit.
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Topics getTopics() {
        return topics;
    }

    public void setTopics(Topics topics) {
        this.topics = topics;
    }

    public static class Topics {

        @NotEmpty
        private String hot;

        @NotEmpty
        private String cold;

        public String getHot() {
            return hot;
        }

        /**
         * Set name of "hot" topic.
         *
         * @param hot Topic name.
         */
        public void setHot(@NotEmpty String hot) {
            this.hot = hot;
        }

        public String getCold() {
            return cold;
        }

        /**
         * Set name of "cold" topic.
         *
         * @param cold Topic name.
         */
        public void setCold(@NotEmpty String cold) {
            this.cold = cold;
        }
    }

}
