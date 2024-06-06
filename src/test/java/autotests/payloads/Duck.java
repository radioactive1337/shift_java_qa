package autotests.payloads;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class Duck {
    @JsonProperty
    private Integer id;
    @JsonProperty
    private String color;
    @JsonProperty
    private Double height;
    @JsonProperty
    private String material;
    @JsonProperty
    private String sound;
    @JsonProperty
    private WingsState wingsState;
}
