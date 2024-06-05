package autotests.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class Duck {
    @JsonProperty
    private String color;
    @JsonProperty
    private int height;
    @JsonProperty
    private String material;
    @JsonProperty
    private String sound;
    @JsonProperty
    private WingsState wingsState;
}
