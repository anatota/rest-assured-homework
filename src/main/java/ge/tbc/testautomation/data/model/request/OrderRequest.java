package ge.tbc.testautomation.data.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequest {
    private Long id;
    private Long petId;
    private Integer quantity;
    private String shipDate;
    private String status;
    private Boolean complete;
}
