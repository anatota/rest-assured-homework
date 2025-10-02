package ge.tbc.testautomation.data.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PlanetResultDetail {
    private PlanetProperties properties;

    @JsonProperty("_id")
    private String id;

    private String description;
    private String uid;

    @JsonProperty("__v")
    private int version;
}
