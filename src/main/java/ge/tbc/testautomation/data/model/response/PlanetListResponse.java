package ge.tbc.testautomation.data.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlanetListResponse {
    private String message;
    private int total_records;
    private int total_pages;
    private String previous;
    private String next;
    private List<PlanetResult> results;
    private String apiVersion;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private LocalDateTime timestamp;
}
