package ge.tbc.testautomation.data.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlanetProperties {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private LocalDateTime created;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private LocalDateTime edited;

    private String climate;
    private String surface_water;
    private String name;
    private String diameter;
    private String rotation_period;
    private String terrain;
    private String gravity;
    private String orbital_period;
    private String population;
    private String url;
}
