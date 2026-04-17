package co.edu.cun.tienda.tienda.adapter.rest.v1.controller.DTO;

import lombok.Data;

@Data
public class AdapterProducts {
    private Long identificator;
    private String productName;
    private String productDescription;
    private Double productPrice;
    private String productImage;
    private String productCategory;
    private PersonExternalAdapter personas;
}
