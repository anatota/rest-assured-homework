package ge.tbc.testautomation.data.model.response;

import lombok.Data;

@Data
public class OrderResponse {
    private Long id;
    private Long petId;
    private Integer quantity;
    private String shipDate;
    private String status;
    private Boolean complete;
}
