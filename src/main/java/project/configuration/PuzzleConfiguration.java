package project.configuration;


import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by michaelhoglan on 9/7/15.
 */
public class PuzzleConfiguration extends Configuration {
        @NotEmpty
        private String defaultName = "N Puzzle Challenge";

        @JsonProperty
        public String getDefaultName() {
            return defaultName;
        }

        @JsonProperty
        public void setDefaultName(String name) {
            this.defaultName = name;
        }

}
